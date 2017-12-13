package com.test.web.render.queue;

import java.util.Arrays;

import com.test.web.buffers.BitOperations;
import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IMarkRenderOperations;
import com.test.web.render.common.IRenderOperations;

final class LongBuf extends BitOperations implements IDelayedRenderer, IMarkRenderOperations {
	
	private static final int OPCODE_SHIFT = 56;
	
	private final IRenderQueueDebugListener debugListener;
	private long [] buf;
	private int offset;

	// store strings here
	private final DuplicateDetectingStringStorageBuffer repeatableBuffer;
	
	// set to non-null if this is primary buffer
	private final LongBuf secondary;
	
	LongBuf(DuplicateDetectingStringStorageBuffer repeatableBuffer, LongBuf secondary, IRenderQueueDebugListener debugListener) {
		this.repeatableBuffer = repeatableBuffer;
		this.secondary = secondary;
		this.debugListener = debugListener;
		this.buf = new long[10000];
	}

	private void putLong(long value) {
		if (offset == buf.length) {
			// expand buffer, we do not bother about linked list buffers
			this.buf = Arrays.copyOf(buf, buf.length * 2);
		}
		
		final int o = offset;

		buf[offset ++] = value;
		
		System.out.format("put long %016x at %d in \n", value , o);
	}

	@Override
	public void setFgColor(int r, int g, int b) {
		
		if (debugListener != null) {
			debugListener.onSetFgColor(offset, isPrimary(), r, g, b);
		}
		
		putLong(
				  RenderOp.SET_FG_COLOR.getOpCode() << OPCODE_SHIFT
				| r << 16
				| g << 8
				| b);
	}

	@Override
	public void setBgColor(int r, int g, int b) {

		if (debugListener != null) {
			debugListener.onSetBgColor(offset, isPrimary(), r, g, b);
		}

		putLong(
				  RenderOp.SET_BG_COLOR.getOpCode() << OPCODE_SHIFT
				| r << 16
				| g << 8
				| b);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		
		if (debugListener != null) {
			debugListener.onDrawLine(offset, isPrimary(), x1, y1, x2, y2);
		}
		
		putLong(RenderOp.SET_FG_COLOR.getOpCode() << OPCODE_SHIFT);
		putLong(x1 << 32 | y1);
		putLong(x2 << 32 | y2);
	}

	@Override
	public void setFont(IFont font) {

		if (debugListener != null) {
			debugListener.onSetFont(offset, isPrimary(), font);
		}

		final int fontStringIdx = putRepeatableString(font.getFontFamily());

		putLong(
				  RenderOp.SET_FONT.getOpCode() << OPCODE_SHIFT
				| font.getFontSize() << 40
				| font.getStyleFlags() << 32
				| fontStringIdx);
		
	}

	@Override
	public void drawText(int x, int y, String text) {
		
		if (debugListener != null) {
			debugListener.onDrawText(offset, isPrimary(), x, y, text);
		}
		
		final int textIdx = putString(text);
		
		putLong(RenderOp.DRAW_TEXT.getOpCode() << OPCODE_SHIFT | textIdx);
		putLong(x << 32 | y);
	}
	
	// Queued render operations
	@Override
	public int mark() {
		if (isSecondary()) {
			throw new IllegalStateException("no secondary buffer");
		}
		
		if (debugListener != null) {
			debugListener.onMark(offset);
		}

		final int markOffset = offset;
		
		// put one long, we will add index into secondary buffer later on
		putLong(RenderOp.MARK.getOpCode() << OPCODE_SHIFT | unsignedIntToLong(0xFFFFFFFF));

		return markOffset;
	}

	@Override
	public IMarkRenderOperations getOperationsForMark(int markOffset) {

		if (isSecondary()) {
			throw new IllegalStateException("no secondary buffer");
		}
		
		if (debugListener != null) {
			debugListener.onGetOperationsForMark(markOffset);
		}

		final long markCode = buf[markOffset];

		final long opcodeOrdinal = markCode >> OPCODE_SHIFT;
		
		if (opcodeOrdinal != RenderOp.MARK.ordinal()) {
			throw new IllegalStateException("Not a mark op: " + opcodeOrdinal);
		}

		if ((markCode & 0xFFFFFFFF) != 0xFFFFFFFFL) {
			throw new IllegalStateException(String.format("already rendered at mark: %08x", markCode));
		}
		
		// store index to where we start rendering in secondary buffer
		buf[markOffset] = RenderOp.MARK.getOpCode() << OPCODE_SHIFT | secondary.getOffset();

		return secondary;
	}
	
	@Override
	public void endOperationsForMark() {
		if (isPrimary()) {
			throw new IllegalStateException("should only be called on secondary buffer");
		}
		
		if (debugListener != null) {
			debugListener.onEndOperationsForMark(offset);
		}
		
		putLong(RenderOp.MARK_END.ordinal() << OPCODE_SHIFT);
	}

	@Override
	public void close() {
		
	}

	@Override
	public int getOffset() {
		return offset;
	}

	private int putString(String s) {
		// just add to repeatable buffer so does not have to handle two different index spaces
		return repeatableBuffer.add(s);
	}

	// put string that might repeat itself
	private int putRepeatableString(String s) {
		return repeatableBuffer.add(s);
	}
	
	// Replay onto operations in correct order
	@Override
	public void replay(IRenderOperations ops, int offset, int endOffset, IFontLookup fontLookup) {

		if (debugListener != null) {
			debugListener.onReplay(ops, isPrimary(), offset, endOffset, fontLookup);
		}
		
		boolean done = false;

		do {

			if (isPrimary() && (offset >= this.offset || (endOffset != IDelayedRenderer.OFFSET_NONE && offset >= endOffset))) {
				done = true;
			}
			else {
				final long l = buf[offset];
		
				final RenderOp renderOp = RenderOp.values()[(int)(l >> OPCODE_SHIFT)];
				
				if (debugListener != null) {
					debugListener.onReplayOp(offset, isPrimary(), renderOp);
				}
				
				switch (renderOp) {
				case SET_FG_COLOR: {
					final int r = (int)((l >> 16) & 0xFFL);
					final int g = (int)((l >> 8) & 0xFFL);
					final int b = (int)((l >> 0) & 0xFFL);
					ops.setFgColor(r, g, b);
					break;
				}
					
				case SET_BG_COLOR: {
					final int r = (int)((l >> 16) & 0xFFL);
					final int g = (int)((l >> 8) & 0xFFL);
					final int b = (int)((l >> 0) & 0xFFL);
					ops.setBgColor(r, g, b);
					break;
				}
				
				case DRAW_LINE:
					final long c1 = buf[offset + 1	];
					final long c2 = buf[offset + 2];
					ops.drawLine(
							(int)(c1 >> 32), (int)(c1 & 0xFFFFFFFF),
							(int)(c2 >> 32), (int)(c2 & 0xFFFFFFFF));
					break;
			
				case SET_FONT:
					final int fontSize = (int)((l >> 40) & 0xFFFF);
					final int styleFlags = (int)((l >> 32) & 0xFF);
					final String fontFamily = this.repeatableBuffer.getString((int)(l & 0xFFFFFFFF));
				
					ops.setFont(fontLookup.getFont(fontFamily, fontSize, styleFlags));
					break;
					
				case DRAW_TEXT:
					final int t = (int)(l & 0xFFFFFFFF);
					final long c = buf[offset + 1	];
					
					ops.drawText(
							(int)(c >> 32), (int)(c & 0xFFFFFFFF),
							repeatableBuffer.getString(t));
					break;
				
				case MARK:
					if (isSecondary()) {
						throw new IllegalStateException("not primary buffer");
					}
					
					final int markOffset = (int)(l & 0xFFFFFFFF);
					
					if (markOffset == 0xFFFFFFFF) {
						throw new IllegalStateException("mark offset not set");
					}
	
					// replay in secondary buffer
					secondary.replay(ops, markOffset, fontLookup);
					break;
					
				case MARK_END:
					if ( ! isSecondary() ) {
						throw new IllegalStateException("mark end in primary buf");
					}
					done = true;
					break;
	
				default:
					throw new IllegalStateException("Unknown render op " + renderOp);
				}
				
				offset += renderOp.getNumLongs();
			}
		} while (!done);
	}
	
	private boolean isPrimary() {
		return secondary != null;
	}
	
	private boolean isSecondary() {
		return secondary == null;
	}
}

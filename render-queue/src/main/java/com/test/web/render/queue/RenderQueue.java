package com.test.web.render.queue;

import com.neaterbits.util.buffers.DuplicateDetectingStringStorageBuffer;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IMarkRenderOperations;
import com.test.web.render.common.IRenderOperations;

// Collectes everything in a buffer of rendering operations that can be replayed
// This allows for passing rendering commands accross process boundaries and to split rendering
// operations across multiple threads
//
// So the queue also works a binary protocol for rendering operations
// Consists of a list of buffers
// each rendering operation is a long where 8 MSBs is opcode

class RenderQueue implements IDelayedRenderer {

	private final 	LongBuf primary;

	RenderQueue(IRenderQueueDebugListener debugListener) {
		
		final DuplicateDetectingStringStorageBuffer stringStorageBuffer = new DuplicateDetectingStringStorageBuffer();
		
		final LongBuf secondary = new LongBuf(stringStorageBuffer, null, debugListener);
		
		this.primary = new LongBuf(stringStorageBuffer, secondary, debugListener);
	}

	@Override
	public void setFgColor(int r, int g, int b) {
		primary.setFgColor(r, g, b);
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		primary.setBgColor(r, g, b);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		primary.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void setFont(IFont font) {
		primary.setFont(font);
	}

	@Override
	public void drawText(int x, int y, String text) {
		primary.drawText(x, y, text);
	}

	@Override
	public int mark() {
		return primary.mark();
	}

	@Override
	public IMarkRenderOperations getOperationsForMark(int markOffset) {
		return primary.getOperationsForMark(markOffset);
	}

	@Override
	public void close() {
		
	}

	@Override
	public int getOffset() {
		return primary.getOffset();
	}

	@Override
	public void replay(IRenderOperations ops, int startOffset, int endOffset, IFontLookup fontLookup) {
		primary.replay(ops, startOffset, endOffset, fontLookup);
	}
}

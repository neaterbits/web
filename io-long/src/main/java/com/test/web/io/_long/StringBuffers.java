package com.test.web.io._long;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import com.test.web.buffers.BaseBuffers;
import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.LoadStream;
import com.test.web.io.common.StreamStatus;
import com.test.web.io.common.Tokenizer;
import com.test.web.types.BigDecimalConversion;
import com.test.web.types.DecimalSize;
import com.test.web.types.IEnum;
import com.test.web.util.StringUtils;

public class StringBuffers extends BaseBuffers<char[][], char[]> implements CharInput, Tokenizer {
	
	private static final boolean DEBUG = false;
	
	private static final String PREFIX = "StringBuffers:";

	private static final int BUF_NO_BITS = 24; // Buffer no
	private static final int OFFSET_BITS = 16; // Offset into each buffer to start of string
	private static final int LENGTH_BITS = 24; // Length of string from 
	
	private static long stringRef(int bufIdx, int idx, int length) {
		return    (((long)bufIdx) << (OFFSET_BITS + LENGTH_BITS))
				| (((long)idx)    << LENGTH_BITS)
				| (long)length;
	}

	private static int bufNo(long stringRef) {
		return (int)(stringRef >> (OFFSET_BITS + LENGTH_BITS));  
	}

	private static int bufOffset(long stringRef) {
		return (int)(stringRef >> LENGTH_BITS);  
	}

	private static int length(long stringRef) {
		return (int)(stringRef & ((1 << LENGTH_BITS) - 1));  
	}

	private static final int INITIAL_BUFFERS = 100;
	private static final int BUFFER_SIZE = (1 << OFFSET_BITS) - 1;
	
	private static final int MAX_BUFFERS = (1 << BUF_NO_BITS) - 1;
	
	// Read 100 bytes at a time max so that can parse as we read from socket
	private static final int READ_CHUNK = 100;
	

	private final LoadStream reader;

	private long curWritePos;
	private long curReadPos;
	
	
	public StringBuffers(LoadStream inputStream) {
		super(new char [INITIAL_BUFFERS][], INITIAL_BUFFERS, MAX_BUFFERS, OFFSET_BITS);
		
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream == null");
		}
		
		this.reader = inputStream;
		
		this.curReadPos = 0;
		this.curWritePos = 0;
	}

	@Override
	public int readNext() throws IOException {

		// Always try to read more data to check whether we're blocked
		// so that parser calling us should block here until some dependency is met but we may continue reading here
		// into internal buffer
		
		for (;;) {
			long status = readMoreData();
			
			if (StreamStatus.isBlocked(status)) {
				// We're blocked on dependency so must continue reading until unblocked
				// We must return here from LoadStream.read() in order to pass new buffers
				// to continue reading data until we have reached EOF
			}
			else if (StreamStatus.isEOF(status)) {
				// EOF but may have buffered data, so just break
				break;
			}
			else {
				// We are neither blocked nor EOF, may return read data
				break;
			}
		}

		final int ret;
		
		if (curReadPos == curWritePos) {
			// EOF since we have tried read data from stream and also have no more characters in buffer
			ret = -1;
		}
		else {
			ret = getOneCharacterFromBuffer();
		}
		
		if (DEBUG) {
			System.out.format(PREFIX + " readNext: %x %x\n", curReadPos, curWritePos);
		}
		
		return ret;
	}
	
	private int getOneCharacterFromBuffer() {
		// Must figure out the readpos and read from there
		final int bufNo = bufNo(curReadPos);
		final int bufOffset = bufOffset(curReadPos);

		if (bufOffset >= BUFFER_SIZE) {
			throw new IllegalStateException("bufOffset >= BUFFER_SIZE");
		}

		final char c = buffers[bufNo][bufOffset];

		// Increment bufOffset
		if (bufOffset == BUFFER_SIZE - 1) {
			// Go to start of next buffer
			this.curReadPos = stringRef(bufNo + 1, 0, 0); 
		}
		else {
			if (DEBUG) {
				System.out.format(PREFIX + " readNext before: %d %d %x %x\n", bufNo, bufOffset, curReadPos, curWritePos);
			}
			this.curReadPos = stringRef(bufNo, bufOffset + 1, 0);
		}

		final int ret = (int)c;
		
		return ret;
	}
	
	@Override
	public String peek(int num) throws IOException {
		// Hack since class is not threadsafe anyway so can just reset curReadPos
		final long originalReadPos = curReadPos;
		
		final StringBuilder sb = new StringBuilder();
		
		int left = num;
		
		while (left > 0) {
			final int val = readNext();
				
			if (val < 0) {
				break; // EOF
			}
			
			sb.append((char)val);
			
			-- left;
		}
		
		this.curReadPos = originalReadPos;
		
		return sb.toString();
	}
	
	@Override
	public int peek() throws IOException {
		// Hack since class is not threadsafe anyway so can just reset curReadPos
		final long originalReadPos = curReadPos;

		final int val = readNext();
		
		this.curReadPos = originalReadPos;

		return val;
	}

	private long readMoreData() throws IOException {
		// Read buffer-size data at current write pos
		int bufNo = bufNo(curWritePos);
		int bufOffset = bufOffset(curWritePos);
		
		final int leftInBuffer = BUFFER_SIZE - bufOffset;
		
		if (leftInBuffer == 0) {
			// Switch to next buffer
			++ bufNo;
			bufOffset = 0;

			expandBuffers(bufNo);
		}
		else if (buffers[bufNo] == null) {
			// Not initialized yet
			buffers[bufNo] = allocateBuffer(BUFFER_SIZE);
		}
		
		// Now have a bufNo, bufOffset and leftInBuffer, read into buffer
		// read data into buffer
		final int toRead = Math.min(READ_CHUNK, leftInBuffer);
		
		
		final long status = reader.read(buffers[bufNo], bufOffset, toRead);
		
		
		if (!StreamStatus.isEOF(status)) {
			final int charsRead = StreamStatus.getElementsRead(status);
			
			// Add to write pos according to bytes read
			if (charsRead > 0) {
				// This is chars read in bytes, make sure we add to offset, 
				this.curWritePos = stringRef(bufNo, bufOffset + charsRead, 0);
			}
		}

		return status;
	}
	
	@Override
	public long getReadPos() {
		return curReadPos;
	}
	
	@Override
	public long getStringRef(long startPos, long endPos, int startOffset, int endSkip) {

		final long pos = startPos;

		int bufNo = bufNo(pos);
		int bufOffset = bufOffset(pos);
		
		if (bufOffset >= BUFFER_SIZE) {
			throw new IllegalArgumentException("buf offset beyond buffer");
		}
		
		int length = getTokenLength(bufNo, bufOffset);

		if (startOffset > 0) {
			length -= startOffset;
		
			while (startOffset > 0) {
				if (startOffset > BUFFER_SIZE) {
					++ bufNo;
					startOffset -= BUFFER_SIZE;
				}
				else {
					if (startOffset + bufOffset >= BUFFER_SIZE) {
						// within next buffer
						++ bufNo;
						bufOffset = BUFFER_SIZE - startOffset;
					}
					else {
						// within current buffer
						bufOffset += startOffset;
					}

					startOffset = 0;
				}
			}
			
			if (bufOffset == BUFFER_SIZE - 1) {
				++ bufNo;
				bufOffset = 0;
			}	
		}
		
		length -= endSkip;
		
		return stringRef(bufNo, bufOffset, length);
	}

	public long getPos() {
		
		// Current read pos
		final int bufNo = bufNo(curReadPos);
		final int bufOffset = bufOffset(curReadPos);

		return bufNo == 0 ? bufOffset : (bufNo - 1) * (long)BUFFER_SIZE;
	}
	
	@Override
	public void rewindOneCharacter(int val) {
		
		if (val < 0) {
			throw new IllegalArgumentException("Rewinding EOF");
		}
		
		int bufNo = bufNo(curReadPos);
		int bufOffset = bufOffset(curReadPos);
		
		if (bufOffset == 0) {
			if (bufNo == 0) {
				throw new IllegalStateException("at start of first buf");
			}
			
			-- bufNo;
			bufOffset = BUFFER_SIZE - 1;
		}
		else {
			-- bufOffset;
		}
		
		final long newPos = stringRef(bufNo, bufOffset, 0);
		
		final char c = characterAt(newPos);
		if (c != (char)val) {
			throw new IllegalStateException("character mismatch: " + c + "/" + (char)val);
		}
		
		this.curReadPos = newPos;
	}

	@Override
	public void rewind(int numCharacters) {
		int bufNo = bufNo(curReadPos);
		int bufOffset = bufOffset(curReadPos);

		for (int i = 0; i < numCharacters; ++ i) {
			if (bufOffset == 0) {
				if (bufNo == 0) {
					throw new IllegalStateException("at start of first buf");
				}
				
				-- bufNo;
				bufOffset = BUFFER_SIZE - 1;
			}
			else {
				-- bufOffset;
			}
		}
		
		final long newPos = stringRef(bufNo, bufOffset, 0);

		this.curReadPos = newPos;
	}

	@Override
	protected char[] get(char[][] array, int bufNo) {
		return array[bufNo];
	}

	@Override
	protected void set(char[][] array, int bufNo, char[] buf) {
		array[bufNo] = buf;
	}

	@Override
	protected char[] allocateBuffer(int length) {
		return new char[length];
	}

	@Override
	protected char[][] copy(char[][] array, int newLength) {
		return Arrays.copyOf(array, newLength);
	}

	@Override
	public <E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, long stringRef, boolean caseSensitive) {
		final E [] values = enumClass.getEnumConstants();
		
		for (E e : values) {
			if (equals(e.getName(), stringRef, caseSensitive)) {
				return e;
			}
		}
		
		return null;
	}

	@Override
	public boolean equalsIgnoreCase(String s, long stringRef) {
		return equals(s, stringRef, false);
	}
	
	private boolean equals(String s, long pos, boolean caseSensitive) {

		boolean equals = true;
		
		int bufNo = bufNo(pos);
		int bufOffset = bufOffset(pos);
		char [] buf = buffers[bufNo];

		final int readBufNo = bufNo(curReadPos);
		final int readBufOffest = bufOffset(curReadPos);
		
		final int length = s.length();
		
		for (int i = 0; i < length; ++ i) {
			
			if (bufOffset == readBufOffest && bufNo == readBufNo) {
				equals = false;
				break;
			}
			
			final char c = buf[bufOffset];
			if (caseSensitive) {
				if (c != s.charAt(i)) {
					equals = false;
					break;
				}
			}
			else {
				if (Character.toUpperCase(c) != Character.toUpperCase(s.charAt(i))) {
					equals = false;
					break;
				}
			}

			if (bufOffset == BUFFER_SIZE - 1) {
				++ bufNo;
				bufOffset = 0;
				buf = buffers[bufNo];
			}
			else {
				++ bufOffset;
			}
		}
		
		return equals;
	}

	private int getTokenLength(int bufNo, int bufOffset) {
		final int readBufNo = bufNo(curReadPos);
		final int readBufOffest = bufOffset(curReadPos);
		
		int length;
		
		switch (readBufNo - bufNo) {
		case 0:
			length = readBufOffest - bufOffset;
			break;
			
		case 1:
			length = BUFFER_SIZE - bufOffset + readBufOffest;
			break;
			
		default:
			// More than one complete buffer between this and write pos
			length = (readBufNo - bufNo - 1) * BUFFER_SIZE + BUFFER_SIZE - bufOffset + readBufOffest;
		}

		return length;
	}
	
	@Override
	public int addToBuffer(DuplicateDetectingStringStorageBuffer buffer, long pos) {

		int bufNo = bufNo(pos);
		int bufOffset = bufOffset(pos);
		int length = length(pos);

		char [] buf = buffers[bufNo];
		
		if (length < 0) {
			throw new IllegalStateException("length < 0");
		}
		
		final char [] tmp = toCharBuf(buf, bufNo, bufOffset, length);

		// TODO: could add character by character
		return buffer.add(tmp, 0, length);
	}
	
	private char [] toCharBuf(char [] buf, int bufNo, int bufOffset, int length) {
		final char [] tmp = new char[length];
		
		for (int i = 0; i < length; ++ i) {
			
			tmp[i] = buf[bufOffset];

			if (bufOffset == BUFFER_SIZE - 1) {
				++ bufNo;
				bufOffset = 0;
				buf = buffers[bufNo];
			}
			else {
				++ bufOffset;
			}
		}
		
		return tmp;
	}
	
	private char characterAt(long ref) {
		int bufNo = bufNo(ref);
		int bufOffset = bufOffset(ref);

		return buffers[bufNo][bufOffset];
	}
	
	public String getString(long ref) {
		final int length = length(ref);
		
		int bufNo = bufNo(ref);
		int bufOffset = bufOffset(ref);

		return new String(toCharBuf(buffers[bufNo], bufNo, bufOffset, length));
	}

	@Override
	public String asString(long stringRef) {
		return getString(stringRef);
	}

	@Override
	public Integer asInteger(long stringRef) {
		// TODO in-buffer
		return StringUtils.asIntegerOrNull(getString(stringRef));
	}

	@Override
	public int asDecimalSize(long stringRef) {
		final String s = asString(stringRef);
		
		return DecimalSize.encodeFromString(s);
	}
	
	@Override
	public BigDecimal asBigDecimal(long stringRef) {
		final String s = asString(stringRef);
		
		return BigDecimalConversion.fromString(s);
	}
	
	

	@Override
	public String asString(long startOffset, long endOffset) {
		final int bufNo = bufNo(startOffset);
		final int bufOffset = bufOffset(startOffset);
		
		
		final int endBufNo = bufNo(endOffset);
		final int endBufOffset = bufOffset(endOffset);
		
		int length;
		
		if (endBufNo < bufNo) {
			throw new IllegalArgumentException("endBufNo < bufNo");
		}
		else if (endBufNo == bufNo) {
			if (endBufOffset < bufOffset) {
				throw new IllegalArgumentException("endBufOffset < bufOffset");
			}
			
			length = endBufOffset- bufOffset;
		}
		else {
			length = BUFFER_SIZE - bufOffset + (BUFFER_SIZE * (endBufNo - bufNo - 1)) + endBufOffset;
		}

		final long stringRef = stringRef(bufNo, bufOffset, length);
		
		return getString(stringRef);
	}

	@Override
	public String toString() {
		return "StringBuffers [curWritePos=" + posString(curWritePos) + ", curReadPos=" + posString(curReadPos) +  "]";
	}
	
	private static String posString(long ref) {
		return "[" + bufNo(ref) + "," + bufOffset(ref) + "]";
	}
}

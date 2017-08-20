package com.test.web.io._long;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.test.web.buffers.BaseBuffers;
import com.test.web.io.common.CharInput;

public class StringBuffers extends BaseBuffers<char[][], char[]> implements CharInput {
	
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
	

	private final BufferedReader reader;

	private long curWritePos;
	private long curReadPos;
	
	
	public StringBuffers(InputStream inputStream) {
		super(new char [INITIAL_BUFFERS][], INITIAL_BUFFERS, MAX_BUFFERS, OFFSET_BITS);
		
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream == null");
		}
		
		this.reader = new BufferedReader(new InputStreamReader(inputStream));
		
		this.curReadPos = 0;
		this.curWritePos = 0;
	}

	@Override
	public int readNext() throws IOException {
		
		// Read next character, may read from input stream
		if (curReadPos == curWritePos) {
			readData();
		}
		
		final int ret;
		
		if (curReadPos == curWritePos) {
			// EOF
			ret = -1;
		}
		else {
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

			ret = (int)c;
		}
		
		if (DEBUG) {
			System.out.format(PREFIX + " readNext: %x %x\n", curReadPos, curWritePos);
		}
		
		return ret;
	}
	
	
	private int readData() throws IOException {
		// Read buffer-size data at curent write pos
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
			// Not intialized yet
			buffers[bufNo] = allocateBuffer(BUFFER_SIZE);
		}
		
		// Now have a bufNo, bufOffset and leftInBuffer, read into buffer
		// read data into buffer
		final int toRead = Math.min(READ_CHUNK, leftInBuffer);
		
		final int charsRead = reader.read(buffers[bufNo], bufOffset, toRead);
		
		// Add to write pos according to bytes read
		if (charsRead > 0) {
			// This is chars read in bytes, make sure we add to offset, 
			this.curWritePos = stringRef(bufNo, bufOffset + charsRead, 0);
		}
		
		return charsRead;
	}

	@Override
	public long getPos() {
		
		// Current read pos
		final int bufNo = bufNo(curReadPos);
		final int bufOffset = bufOffset(curReadPos);

		return bufNo == 0 ? bufOffset : (bufNo - 1) * (long)BUFFER_SIZE;
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
}

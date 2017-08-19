package com.test.web.io._long;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.test.web.io.common.CharInput;

public class StringBuffers implements CharInput {
	
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
		return (int)(stringRef >> (OFFSET_BITS + LENGTH_BITS));  
	}

	private static int length(long stringRef) {
		return (int)(stringRef & ((1 << LENGTH_BITS) - 1));  
	}

	private static final int INITIAL_BUFFERS = 100;
	private static final int BUFFER_SIZE = (1 << OFFSET_BITS) - 1;
	
	private static final int MAX_BUFFERS = (1 << BUF_NO_BITS) - 1;
	
	// Read 100 bytes at a time max so that can parse as we read from socket
	private static final int READ_CHUNK = 100;
	
	private char [][] buffers;

	private final BufferedReader reader;

	private long curWritePos;
	private long curReadPos;
	
	
	StringBuffers(InputStream inputStream) {
		
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream == null");
		}
		
		this.buffers = new char [INITIAL_BUFFERS][];

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
				this.curReadPos = stringRef(bufNo, bufOffset + 1, 0);
			}

			ret = (int)c;
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
			
			if (bufNo >= buffers.length) {
				// Increase buffers array
				if (bufNo >= MAX_BUFFERS) {
					// More than max buffers
					throw new IllegalStateException("bufNo >= MAX_BUFFERS");
				}
				
				final int numBuffers = Math.min(MAX_BUFFERS, buffers.length * 2);
				
				this.buffers = Arrays.copyOf(buffers, numBuffers);
				
			}
			
			if (buffers[bufNo] == null) {
				buffers[bufNo] = new char[BUFFER_SIZE];
			}
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
}

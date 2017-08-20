package com.test.web.buffers;

/**
 * A buffer of chunks consisting of longs.
 * 
 * Store element refs as integers (so 4GB limit)
 * We allocate 16 bits for buffer no and 16 bits for buffer offset in returned indices.
 * 
 * @author nhl
 *
 */

import static com.test.web.buffers.BitOperations.max;

public class LongBuffersIntegerIndex extends LongBuffers {
	
	private static final int OFFSET_BITS = 16;
	private static final int BUFNO_BITS = 16;
	
	private static final int BUFFER_SIZE = max(OFFSET_BITS);
	private static final int MAX_BUFFERS = max(BUFNO_BITS);
	
	private static final int bufNo(int ref) {
		return ref >> 16;
	}
	
	private static final int bufOffset(int ref) {
		return ref & 0x0000FFFF;
	}
	
	private int writePos;
	
	protected LongBuffersIntegerIndex() {
		super(100, MAX_BUFFERS, OFFSET_BITS);
		this.writePos = 0;
	}
	
	protected final long [] buf(int ref) {
		return buffers[bufNo(ref)];
	}

	protected final int offset(int ref) {
		return bufOffset(ref);
	}
	
	
	/**
	 * Allocate a chunk of given size
	 * 
	 * @param size size of chunk to allocate
	 * 
	 * @return encoded index to start of block
	 */
	protected final int allocate(int size) {
		
		if (size > BUFFER_SIZE) {
			throw new IllegalArgumentException("size > BUFFER_SIZE");
		}
		
		int offset = bufOffset(writePos);
		int bufNo = bufNo(writePos);
		
		if (offset + size > BUFFER_SIZE) {
			// No room in current buffer, must allocate a new one
			++ bufNo;
			offset = 0;
			
			expandBuffers(bufNo);
		}
		else if (buffers[bufNo] == null) {
			buffers[bufNo] = allocateBuffer(BUFFER_SIZE);
		}
		
		final int ret = bufNo << BUFNO_BITS | offset;
		
		this.writePos = ret + size;
		
		return ret;
	}
}

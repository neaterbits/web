package com.test.web.buffers;

import java.util.Arrays;

public abstract class LongBuffers extends BaseBuffers<long [][], long[]> {

	protected LongBuffers(int numInitialBuffers, int maxBuffers, int bufferSizeBits) {
		super(new long[numInitialBuffers][], numInitialBuffers, maxBuffers, bufferSizeBits);
	}

	@Override
	protected long[] get(long[][] array, int bufNo) {
		return array[bufNo];
	}

	@Override
	protected void set(long[][] array, int bufNo, long[] buf) {
		array[bufNo] = buf;
	}

	@Override
	protected long[] allocateBuffer(int length) {
		return new long[length];
	}

	@Override
	protected long[][] copy(long[][] array, int newLength) {
		return Arrays.copyOf(array, newLength);
	}
}

package com.test.web.buffers;

public class BitOperations {
	public static int max(int numBits) {
		return (1 << numBits) - 1;
	}

	public static int maskToInt(long l, int shiftBits, int numBits) {
		return (int) ((l >> shiftBits) & ((1 << numBits) - 1));
	}
}

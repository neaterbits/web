package com.test.web.buffers;

public class BufferUtil extends BitOperations {
	
	protected static class Flag {
		private final int numBits;
		private final Class<? extends Enum<?>> enumClass;
		
		public Flag(int numBits, Class<? extends Enum<?>> enumClass) {
			this.numBits = numBits;
			this.enumClass = enumClass;
		}
	}
	
	protected static void verifyFlags(Flag [] flags, int maxBits) {
		int sumBits = 0; 
		for (Flag flag : flags) {
			if (1 << flag.numBits < flag.enumClass.getEnumConstants().length) {
				throw new IllegalStateException("Not enough room for all bits in " + flag.enumClass);
			}
			
			sumBits += flag.numBits;
		}
		
		if (sumBits > maxBits) {
			throw new IllegalStateException("more than " + maxBits + " bits in flags");
		}
	}
}

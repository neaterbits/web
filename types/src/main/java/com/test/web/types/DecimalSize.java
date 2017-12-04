package com.test.web.types;

// Decimal-size for HTML sizes, does not required BigDecial, we just specify as a int and a shift
// We do not need BigInteger or unlimited precision
// Can also encode this as an int for storage
public class DecimalSize {
	private final int value;
	private int scale;
	
	public DecimalSize(int beforeComma, int afterComma) {
		// Multiply up with the values after comma
		int v = beforeComma;

		int numDigitsAfterComma = 0;
		int n = afterComma;
		
		while (n != 0) {
			++ numDigitsAfterComma;
			
			n /= 10;
			
			v *= 10;
		}
		
		v += afterComma;
		
		this.value = v;
		this.scale = numDigitsAfterComma;
	}
	
	public int encodeAsInt() {
		if (value > 1 << 24) {
			throw new IllegalStateException("No room for value");
		}

		if (scale > 1 << 8) {
			throw new IllegalStateException("No room for scale");
		}
		
		return value << 24 | scale;
	}

	public static int encodeAsInt(int intValue) {
		return intValue << 24 | 0;
	}
	
	public static int decodeToInt(int encoded) {
		if ((encoded & 0x000000FF) != 0) {
			throw new IllegalStateException("encoded int is decimal");
		}
		
		return encoded >> 24;
	}
}

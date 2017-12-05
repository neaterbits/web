package com.test.web.types;

// Decimal-size for HTML sizes, does not required BigDecial, we just specify as a int and a shift
// We do not need BigInteger or unlimited precision
// Can also encode this as an int for storage
public class DecimalSize {

	private static final int SCALE_BITS = 8;
	private static final int SCALE_MASK = (1 << SCALE_BITS) - 1;
	
	public static final int NONE = -1;
	
	private final int value;
	private int scale;
	
	public DecimalSize(int beforeComma, String afterComma) {
		// Multiply up with the values after comma
		int v = beforeComma;

		int numDigitsAfterComma = afterComma.length();
		
		for (int i = 0; i < numDigitsAfterComma; ++ i) {
			
			final char c = afterComma.charAt(i);
			
			if (!Character.isDigit(c)) {
				throw new IllegalArgumentException("Not a digit: '" + c + "'");
			}
			
			final int digit = c - '0';
			
			v *= 10;
			v += digit;
		}
		
		this.value = v;
		this.scale = numDigitsAfterComma;
	}
	
	public int encodeAsInt() {
		if (value > 1 << (32 - SCALE_BITS)) {
			throw new IllegalStateException("No room for value");
		}

		if (scale > 1 << SCALE_BITS) {
			throw new IllegalStateException("No room for scale");
		}
		
		return value << SCALE_BITS | scale;
	}

	public static int encodeAsInt(int intValue) {
		return intValue << SCALE_BITS | 0;
	}
	
	public static int decodeToInt(int encoded) {
		if ((encoded & SCALE_MASK) != 0) {
			throw new IllegalStateException("encoded int is decimal");
		}
		
		return encoded >> SCALE_BITS;
	}
	
	
	public static String decodeToString(int encoded) {
		int scale = encoded & SCALE_MASK;
		int beforeComma = encoded >> SCALE_BITS;

		final StringBuilder afterCommaString = new StringBuilder(scale);
		
		afterCommaString.setLength(scale);
	
		final String ret;
		
		if (scale != 0) {
			for (int i = scale -1 ; i >= 0; -- i) {
				// get last digit we're processing
				final int remainder = beforeComma % 10;
	
				afterCommaString.setCharAt(i, (char)('0' + remainder));
				
				// shift one decimal point to the left
				beforeComma /= 10;
			}

			ret = String.format("%d.%s", beforeComma, afterCommaString.toString());
		}
		else {
			ret = String.valueOf(beforeComma);
		}
		
		return ret;
	}
}

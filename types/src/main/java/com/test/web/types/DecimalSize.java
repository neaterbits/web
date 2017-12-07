package com.test.web.types;

import java.util.function.BiFunction;

// Decimal-size for HTML sizes, does not required BigDecial, we just specify as a int and a shift
// We do not need BigInteger or unlimited precision
// Can also encode this as an int for storage
public class DecimalSize {

	private static final int SCALE_BITS = 8;
	private static final int SCALE_MASK = (1 << SCALE_BITS) - 1;
	
	public static final int NONE = -1;
	
	private final int value;
	private int scale;

	public static DecimalSize of(int beforeComma) {
		return new DecimalSize(beforeComma, "");
	}

	public static DecimalSize of(int beforeComma, String afterComma) {
		return new DecimalSize(beforeComma, afterComma);
	}

	public static DecimalSize of(String value) {
		final int commaIndex = value.indexOf('.');
		
		final DecimalSize ret;
		if (commaIndex >= 0) {
			ret = of(Integer.parseInt(value.substring(0,  commaIndex)), value.substring(commaIndex + 1));
		}
		else {
			ret = of(Integer.parseInt(value));
		}

		return ret;
	}
	
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
			throw new IllegalStateException("encoded int is decimal with scale " + (encoded & SCALE_MASK) + ": " + decodeToString(encoded));
		}
		
		return encoded >> SCALE_BITS;
	}
	
	private static <T> T decode(int encoded, BiFunction<Integer, String, T> callback) {
		int scale = encoded & SCALE_MASK;
		int beforeComma = encoded >> SCALE_BITS;

		final StringBuilder afterCommaString = new StringBuilder(scale);
		
		afterCommaString.setLength(scale);
	
		final String afterComma;
		
		if (scale != 0) {
			for (int i = scale -1 ; i >= 0; -- i) {
				// get last digit we're processing
				final int remainder = beforeComma % 10;
	
				afterCommaString.setCharAt(i, (char)('0' + remainder));
				
				// shift one decimal point to the left
				beforeComma /= 10;
			}
			
			afterComma = afterCommaString.toString();
		}
		else {
			afterComma = null;
		}
		
		return callback.apply(beforeComma, afterComma);
	}
	
	public static String decodeToString(int encoded) {
		return decode(encoded, (beforeComma, afterComma) -> {
			return afterComma != null
					? String.format("%d.%s", beforeComma, afterComma)
					: String.valueOf(beforeComma);
		});
	}
	
	public static DecimalSize decode(int encoded) {
		return decode(encoded, (beforeComma, afterComma) -> {
			return afterComma != null
					? new DecimalSize(beforeComma, afterComma)
					: new DecimalSize(beforeComma, "");
		});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + scale;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DecimalSize other = (DecimalSize) obj;
		if (scale != other.scale)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DecimalSize [value=" + value + ", scale=" + scale + "]";
	}
}

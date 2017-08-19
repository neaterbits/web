package com.test.web.css._long;

import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.CSStyle;
import com.test.web.css.common.CSSDisplay;

/**
 * For storing styling information in a long [] array for this purpose
 * 
 * CSS is stored as a set of long indices containing all the information 
 * 
 * 0 - flags based on CSS styles for what styles are added
 * 1 - width and height
 * 2 - 4 bits display, 
 * 		-- 
 * 
 * @author nhl
 *
 */



public class LongCSS {
	
	public static final int CSS_LONGS = 35;
	
	private static int max(int numBits) {
		return (1 << numBits)  - 1;
	}
	
	private static final int DISPLAY_BITS = 4;
	private static final int UNIT_BITS = 2;
	
	private static final int WITDH_BITS = 30;
	private static final int HEIGHT_BITS = 30;

	private static final int WIDTH_MAX = max(WITDH_BITS);
	private static final int HEIGHT_MAX = max(HEIGHT_BITS);

	static {
		if (1 << DISPLAY_BITS < CSSDisplay.values().length) {
			throw new IllegalStateException("Not enough room for all display bits");
		}
		
		if (1 << UNIT_BITS < CSSUnit.values().length) {
			throw new IllegalStateException("Not enough room for all unit bits");
		}
		
		if (WITDH_BITS + HEIGHT_BITS + UNIT_BITS * 2 != Long.valueOf(64)) {
			throw new IllegalStateException("Expected 64 bits for with and height");
		}
	}

	// Max number of longs needed to store CSS information
	public static final int CSS_MAX = 35;

	private static void addStyle(CSStyle style, long [] buf, int offset) {
		// Set bit for style present
		buf[offset + 0] |= 1 << style.ordinal();
	}

	public static boolean hasStyle(CSStyle style, long [] buf, int offset) {
		return (buf[offset + 1] & (long)style.ordinal()) != 0L;
	}

	
	public static void addWidth(long [] buf, int offset, int width, CSSUnit unit) {
		
		if (width > WIDTH_MAX) {
			throw new IllegalArgumentException("width > WIDTH_MAX: " + width);
		}
		
		addStyle(CSStyle.WIDTH, buf, offset);
		
		buf[offset + 1] =   buf[offset + 1]
				 | ((long)width) << (UNIT_BITS + HEIGHT_BITS + UNIT_BITS)
				 | ((long)unit.ordinal()) << (HEIGHT_BITS + UNIT_BITS); 
	}

	public static void addHeight(long [] buf, int offset, int height, CSSUnit unit) {

		if (height > HEIGHT_MAX) {
			throw new IllegalArgumentException("height > HEIGHT_MAX: " + height);
		}
		
		addStyle(CSStyle.HEIGHT, buf, offset);
		
		buf[offset + 1] =   buf[offset + 1]
				 | ((long)height) << UNIT_BITS
				 | ((long)unit.ordinal()); 
	}

	public static int getWidth(long [] buf, int offset) {
		return (int)buf[offset + 1] >> (UNIT_BITS  + HEIGHT_BITS + UNIT_BITS);
	}

	public static CSSUnit getWidthUnit(long [] buf, int offset) {
		final int ordinal = (int)((buf[offset + 1] >> (HEIGHT_BITS + UNIT_BITS)) & ((1 << UNIT_BITS) - 1));

		return CSSUnit.values()[ordinal];
	}

	public static int getHeight(long [] buf, int offset) {
		return (int)(buf[offset + 1] >> (UNIT_BITS) & ((1 << HEIGHT_BITS) - 1));
	}

	public static CSSUnit getHeightUnit(long [] buf, int offset) {
		final int ordinal = (int)(buf[offset + 1] & ((1 << UNIT_BITS) - 1));
		
		return CSSUnit.values()[ordinal];
	}
}

package com.test.web.css._long;

import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.CSStyle;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.Justify;
import com.test.web.parse.css.CSSOverflow;
import com.test.web.parse.css.CSSPosition;
import com.test.web.parse.css.CSSTextAlign;
import com.test.web.css.common.CSSDisplay;
import com.test.web.css.common.CSSFloat;

import static com.test.web.buffers.BitOperations.max;
import static com.test.web.buffers.BitOperations.maskToInt;

/**
 * For storing styling information in a long [] array for this purpose
 * 
 * CSS is stored as a set of long indices containing all the information 
 * 
 * Simple CSS
 * 
 * Base types:
 * 
 * color, 25 bits (1 bit flag RGB or builtin, 24bits RGB encoded or index into builtin colors)
 * width/height
 * 
 * margin and padding, 1 long (16 bits each direction, Short.MAX_VALUE means auto)
 * 
 * 16 bits per direction: units, 1 bit auto, rest is size
 *  
 * 
 *   
 * Indices for basic elements:
 * 
 * 0 - flags based on CSS styles for what styles are added
 * 1 - width and height
 * 2 - margin
 * 3 - padding
 * 4 - 4 bits display, 25 bits font color, 25 bits background color 
 * 
 * @author nhl
 *
 */



public class LongCSS {
	
	private static CSSUnit maskToUnit(long l, int shiftBits, int numBits) {
		return CSSUnit.values()[maskToInt(l, shiftBits, numBits)];
	}

	private static boolean maskToBoolean(long l, int shiftBits) {
		return maskToInt(l, shiftBits, 1) != 0;
	}
	
	private static Justify maskToJustify(long l, int shiftBits) {

		final int ordinal = maskToInt(l, shiftBits, 2);
		
		return Justify.values()[ordinal];
	}

	private static final int IDX_STYLES = 0; // Which styles are enabled
	private static final int IDX_FLAGS = 1; // various flags (display, text-align, ...)
	private static final int IDX_DIMENSIONS = 2; // width/height
	private static final int IDX_MARGIN = 3;
	private static final int IDX_PADDING = 4;
	
	private static final int NUM_COMPACT = IDX_PADDING + 1;
	
	
	private static int bits(Class<?> enumClass) {
		final int numConstants = enumClass.getEnumConstants().length;
		
		if (numConstants == 0) {
			throw new IllegalArgumentException("No constants in enum " + enumClass);
		}
		
		// How many bits are required for this value?
		int numBits = 0;

		for (int i = 31; i > 0; -- i) {
			if ((numConstants & (1 << i)) != 0) {
				numBits = i;
				break;
			}
		}

		return numBits;
	}
	
	// For flags, could compute this but keep as numbers and verify so that compiler can optimize arithmetic for constant-values
	private static final int DISPLAY_BITS = 5;
	private static final int POSITION_BITS = 3;
	private static final int TEXT_ALIGN_BITS = 3;
	private static final int FLOAT_BITS = 3;
	private static final int OVERFLOW_BITS = 3;
	
	
	private static final int DISPLAY_SHIFT 	= POSITION_BITS + TEXT_ALIGN_BITS + FLOAT_BITS + OVERFLOW_BITS;
	private static final int POSITION_SHIFT = 				  TEXT_ALIGN_BITS + FLOAT_BITS + OVERFLOW_BITS;
	private static final int TEXT_ALIGN_SHIFT = 				  		        FLOAT_BITS + OVERFLOW_BITS;
	private static final int FLOAT_SHIFT = 				  		        					 OVERFLOW_BITS;
	private static final int OVERFLOW_SHIFT = 0;

	private static final int DISPLAY_MASK 		= ((1 << DISPLAY_BITS 		+ 1) - 1) << DISPLAY_SHIFT;
	private static final int POSITION_MASK		= ((1 << POSITION_BITS 		+ 1) - 1) << POSITION_SHIFT;
	private static final int TEXT_ALIGN_MASK 	= ((1 << TEXT_ALIGN_BITS 	+ 1) - 1) << TEXT_ALIGN_SHIFT;
	private static final int FLOAT_MASK 		= ((1 << FLOAT_BITS 		+ 1) - 1) << FLOAT_SHIFT;
	private static final int OVERFLOW_MASK 		= ((1 << OVERFLOW_BITS 		+ 1) - 1) << OVERFLOW_SHIFT;

	private static final int DISPLAY_CLEAR 		= ~DISPLAY_MASK;
	private static final int POSITION_CLEAR		= ~POSITION_MASK;
	private static final int TEXT_ALIGN_CLEAR 	= ~TEXT_ALIGN_MASK;
	private static final int FLOAT_CLEAR 		= ~FLOAT_MASK;
	private static final int OVERFLOW_CLEAR 	= ~OVERFLOW_MASK;
	
	private static class Flag {
		private final int numBits;
		private final Class<? extends Enum<?>> enumClass;
		
		public Flag(int numBits, Class<? extends Enum<?>> enumClass) {
			this.numBits = numBits;
			this.enumClass = enumClass;
		}
	}
	
	private static Flag [] flags = new Flag[] {
			new Flag(DISPLAY_BITS, 		CSSDisplay.class),
			new Flag(POSITION_BITS,		CSSPosition.class),
			new Flag(TEXT_ALIGN_BITS, 	CSSTextAlign.class),
			new Flag(FLOAT_BITS,		CSSFloat.class),
			new Flag(OVERFLOW_BITS,		CSSOverflow.class)
	};
	
	
	private static final int UNIT_BITS = 2;
	
	private static final int WITDH_BITS = 30;
	private static final int HEIGHT_BITS = 30;

	private static final int WIDTH_MAX = max(WITDH_BITS);
	private static final int HEIGHT_MAX = max(HEIGHT_BITS);
	
	private static final int JUSTIFY_SIZE_BITS = 13;
	private static final int JUSTIFY_TYPE_BITS = 2;

	static final int CSS_ENTITY_COMPACT = NUM_COMPACT;

	static {
		
		int sumBits = 0; 
		for (Flag flag : flags) {
			if (1 << flag.numBits < flag.enumClass.getEnumConstants().length) {
				throw new IllegalStateException("Not enough room for all bits in " + flag.enumClass);
			}
			
			sumBits += flag.numBits;
		}
		
		if (sumBits > 64) {
			throw new IllegalStateException("more that 64 bits in flags");
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
		buf[offset + IDX_STYLES] |= 1 << style.ordinal();
	}

	public static boolean hasStyle(CSStyle style, long [] buf, int offset) {
		return (buf[offset + IDX_STYLES] & (long)style.ordinal()) != 0L;
	}
	
	private static final int JUSTIFY_LEFT_SHIFT	 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 4) + JUSTIFY_SIZE_BITS * 3;
	private static final int JUSTIFY_LEFT_UNIT		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 3 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_LEFT_TYPE 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 3;
	private static final int JUSTIFY_RIGHT_SHIFT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 2;
	private static final int JUSTIFY_RIGHT_UNIT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 2 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_RIGHT_TYPE 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 2;
	private static final int JUSTIFY_TOP_SHIFT 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 1;
	private static final int JUSTIFY_TOP_UNIT 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 1 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_TOP_TYPE 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 1;
	private static final int JUSTIFY_BOTTOM_SHIFT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 0;
	private static final int JUSTIFY_BOTTOM_UNIT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 0) + JUSTIFY_SIZE_BITS * 0 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_BOTTOM_TYPE 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 0) + JUSTIFY_SIZE_BITS * 0;
	
	private static <PARAM> void getJustify(long encoded, ICSSJustify<PARAM> setter, PARAM param) {
		
		final int left 			= maskToInt(encoded, JUSTIFY_LEFT_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit leftUnit 	= maskToUnit(encoded, JUSTIFY_LEFT_UNIT, JUSTIFY_SIZE_BITS);
		final Justify leftAuto 	= maskToJustify(encoded, JUSTIFY_LEFT_TYPE);

		final int right 		= maskToInt(encoded, JUSTIFY_RIGHT_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit rightUnit = maskToUnit(encoded, JUSTIFY_RIGHT_UNIT, JUSTIFY_SIZE_BITS);
		final Justify rightAuto = maskToJustify(encoded, JUSTIFY_RIGHT_TYPE);

		final int top 			= maskToInt(encoded, JUSTIFY_TOP_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit topUnit 	= maskToUnit(encoded, JUSTIFY_TOP_UNIT, JUSTIFY_SIZE_BITS);
		final Justify topAuto 	= maskToJustify(encoded, JUSTIFY_TOP_TYPE);

		final int bottom 			= maskToInt(encoded, JUSTIFY_BOTTOM_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit bottomUnit 	= maskToUnit(encoded, JUSTIFY_BOTTOM_UNIT, JUSTIFY_SIZE_BITS);
		final Justify bottomAuto 	= maskToJustify(encoded, JUSTIFY_BOTTOM_TYPE);
		
		setter.set(param, left, leftUnit, leftAuto, top, topUnit, topAuto, right, rightUnit, rightAuto, bottom, bottomUnit, bottomAuto);
	}

	private static void setJustify(
			long [] buf, int offset,
			int left, CSSUnit leftUnit, Justify leftType,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType) {

		// Merge with existing values since this may be an update
		long encoded = buf[offset];
		
		if (leftType != Justify.NONE) {
			encoded &= 0x0000FFFFFFFFFFFFL;
			encoded |=    left 					<< JUSTIFY_LEFT_SHIFT
						| ordinal(leftUnit) 	<< JUSTIFY_LEFT_UNIT
						| leftType.ordinal() 	<< JUSTIFY_LEFT_TYPE;
			
		}
		
		if (rightType != Justify.NONE) {
			encoded &= 0xFFFF0000FFFFFFFFL;
			encoded |=     right 			 	<< JUSTIFY_RIGHT_SHIFT
						 | ordinal(rightUnit) 	<< JUSTIFY_RIGHT_UNIT
						 | rightType.ordinal() 	<< JUSTIFY_RIGHT_TYPE;
		}
		
		if (topType != Justify.NONE) {
			encoded &= 0xFFFFFFFF0000FFFFL;
			encoded |= 	  top   		    << JUSTIFY_TOP_SHIFT
						| ordinal(topUnit)	<< JUSTIFY_TOP_UNIT
						| topType.ordinal()	<< JUSTIFY_TOP_TYPE;
			
		}
		
		if (topType != Justify.NONE) {
			encoded &= 0xFFFFFFFFFFFF0000L;
			encoded |=	  bottom 			    << JUSTIFY_BOTTOM_SHIFT
						| ordinal(bottomUnit) 	<< JUSTIFY_BOTTOM_UNIT
						| bottomType.ordinal() 	<< JUSTIFY_BOTTOM_TYPE;

		}
		
		
		buf[offset] = encoded;
	}
	
	private static final int ordinal(CSSUnit unit) {
		return unit != null ? unit.ordinal() : 0;
	}
	
	public static void setMargin(long [] buf, int offset,
			int left, CSSUnit leftUnit, Justify leftType,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType) {
		
		setJustify(buf, offset + IDX_MARGIN, left, leftUnit, leftType, top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType);
	}
	
	public static void setPadding(long [] buf, int offset,
			int left, CSSUnit leftUnit, Justify leftType,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType) {
		
		setJustify(buf, offset + IDX_PADDING, left, leftUnit, leftType, top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType);
	}

	public static <PARAM> void getMargin(long [] buf, int offset, ICSSJustify<PARAM> setter, PARAM param) {
		getJustify(buf[offset + IDX_MARGIN], setter, param);
	}

	public static <PARAM> void getPadding(long [] buf, int offset, ICSSJustify<PARAM> setter, PARAM param) {
		getJustify(buf[offset + IDX_PADDING], setter, param);
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
	
	private static void updateFlag(long [] buf, int offset, int ordinal, int shift, long clear) {
		
		long encoded = buf[offset + IDX_FLAGS];
		
		encoded &= clear;
		
		encoded |= ((long)ordinal) << shift;
		
		buf[offset + IDX_FLAGS] = encoded;
	}
	
	private static int getFlag(long [] buf, int offset, int shift, long mask) {
		
		final long encoded = buf[offset + IDX_FLAGS];
		
		return (int)((encoded & mask) >> shift);
	}
	
	public static void setDisplay(long [] buf, int offset, CSSDisplay display) {
		updateFlag(buf, offset, display.ordinal(), DISPLAY_SHIFT, DISPLAY_CLEAR);
	}
	
	public static void setPosition(long [] buf, int offset, CSSPosition position) {
		updateFlag(buf, offset, position.ordinal(), POSITION_SHIFT, POSITION_CLEAR);
	}

	public static void setFloat(long [] buf, int offset, CSSFloat _float) {
		updateFlag(buf, offset, _float.ordinal(), FLOAT_SHIFT, FLOAT_CLEAR);
	}

	public static void setTextAlign(long [] buf, int offset, CSSTextAlign textAlign) {
		updateFlag(buf, offset, textAlign.ordinal(), TEXT_ALIGN_SHIFT, TEXT_ALIGN_CLEAR);
	}

	public static void setOverflow(long [] buf, int offset, CSSOverflow overflow) {
		updateFlag(buf, offset, overflow.ordinal(), OVERFLOW_SHIFT, OVERFLOW_CLEAR);
	}
	
	public static CSSDisplay getDisplay(long [] buf, int offset) {
		return CSSDisplay.values()[getFlag(buf, offset, DISPLAY_SHIFT, DISPLAY_MASK)];
	}
	
	public static CSSPosition getPosition(long [] buf, int offset) {
		return CSSPosition.values()[getFlag(buf, offset, POSITION_SHIFT, POSITION_MASK)];
	}
	
	public static CSSFloat getFloat(long [] buf, int offset) {
		return CSSFloat.values()[getFlag(buf, offset, FLOAT_SHIFT, FLOAT_MASK)];
	}
	
	public static CSSTextAlign getTextAlign(long [] buf, int offset) {
		return CSSTextAlign.values()[getFlag(buf, offset, TEXT_ALIGN_SHIFT, TEXT_ALIGN_MASK)];
	}
	
	public static CSSOverflow getOverflow(long [] buf, int offset) {
		return CSSOverflow.values()[getFlag(buf, offset, OVERFLOW_SHIFT, OVERFLOW_MASK)];
	}
}

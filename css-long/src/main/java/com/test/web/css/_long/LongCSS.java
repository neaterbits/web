package com.test.web.css._long;

import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.css.common.enums.CSSJustify;

import static com.test.web.buffers.BitOperations.max;

import com.test.web.buffers.BufferUtil;

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



public class LongCSS extends BufferUtil {
	
	private static CSSUnit maskToUnit(long l, int shiftBits, int numBits) {
		return CSSUnit.values()[maskToInt(l, shiftBits, numBits)];
	}

	private static boolean maskToBoolean(long l, int shiftBits) {
		return maskToInt(l, shiftBits, 1) != 0;
	}
	
	private static CSSJustify maskToJustify(long l, int shiftBits) {

		final int ordinal = maskToInt(l, shiftBits, 2);
		
		return CSSJustify.values()[ordinal];
	}

	private static final int IDX_STYLES = 0; // Which styles are enabled
	private static final int IDX_FLAGS = 1; // various flags (display, text-align, ...)
	private static final int IDX_POSITION = 2; // left/top
	private static final int IDX_DIMENSIONS = 3; // width/height
	private static final int IDX_MARGIN = 4;
	private static final int IDX_PADDING = 5;
	
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
	
	
	private static final int DISPLAY_SHIFT 		= POSITION_BITS + TEXT_ALIGN_BITS + FLOAT_BITS + OVERFLOW_BITS;
	private static final int POSITION_SHIFT  		= 				                TEXT_ALIGN_BITS + FLOAT_BITS + OVERFLOW_BITS;
	private static final int TEXT_ALIGN_SHIFT 	= 				  		        							 	 FLOAT_BITS + OVERFLOW_BITS;
	private static final int FLOAT_SHIFT 			= 				  		        					 									OVERFLOW_BITS;
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
	
	private static Flag [] flags = new Flag[] {
			new Flag(DISPLAY_BITS, 		CSSDisplay.class),
			new Flag(POSITION_BITS,		CSSPosition.class),
			new Flag(TEXT_ALIGN_BITS, 	CSSTextAlign.class),
			new Flag(FLOAT_BITS,		CSSFloat.class),
			new Flag(OVERFLOW_BITS,		CSSOverflow.class)
	};
	
	
	private static final int UNIT_BITS = 2;

	private static final int LEFT_BITS = 30;
	private static final int TOP_BITS = 30;

	private static final int LEFT_MAX = max(LEFT_BITS);
	private static final int TOP_MAX = max(TOP_BITS);
	
	private static final int WITDH_BITS = 30;
	private static final int HEIGHT_BITS = 30;

	private static final int WIDTH_MAX = max(WITDH_BITS);
	private static final int HEIGHT_MAX = max(HEIGHT_BITS);
	
	private static final int JUSTIFY_SIZE_BITS = 13;
	private static final int JUSTIFY_TYPE_BITS = 2;

	static final int CSS_ENTITY_COMPACT = NUM_COMPACT;

	static {

		verifyFlags(flags, 64);
		
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
		buf[offset + IDX_STYLES] |= (long)(1 << style.ordinal());
	}

	private static void clearStyle(CSStyle style, long [] buf, int offset) {
		// Set bit for style present
		buf[offset + IDX_STYLES] &= ~((long)(1 << style.ordinal()));
	}

	public static boolean hasStyle(CSStyle style, long [] buf, int offset) {
		return (buf[offset + IDX_STYLES] & (long)(1 << style.ordinal())) != 0L;
	}
	
	private static final int JUSTIFY_TOP_SHIFT	 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 4) + JUSTIFY_SIZE_BITS * 3;
	private static final int JUSTIFY_TOP_UNIT		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 3 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_TOP_TYPE 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 3;
	private static final int JUSTIFY_RIGHT_SHIFT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 3) + JUSTIFY_SIZE_BITS * 2;
	private static final int JUSTIFY_RIGHT_UNIT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 2 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_RIGHT_TYPE 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 2;
	private static final int JUSTIFY_BOTTOM_SHIFT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 2) + JUSTIFY_SIZE_BITS * 1;
	private static final int JUSTIFY_BOTTOM_UNIT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 1 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_BOTTOM_TYPE 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 1;
	private static final int JUSTIFY_LEFT_SHIFT 	= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 1) + JUSTIFY_SIZE_BITS * 0;
	private static final int JUSTIFY_LEFT_UNIT 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 0) + JUSTIFY_SIZE_BITS * 0 + JUSTIFY_TYPE_BITS;
	private static final int JUSTIFY_LEFT_TYPE 		= ((UNIT_BITS + JUSTIFY_TYPE_BITS) * 0) + JUSTIFY_SIZE_BITS * 0;
	
	private static <PARAM> void getJustify(long encoded, ICSSJustify<PARAM> setter, PARAM param) {
		
		final int top 			= maskToInt(encoded, JUSTIFY_TOP_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit topUnit 	= maskToUnit(encoded, JUSTIFY_TOP_UNIT, UNIT_BITS);
		final CSSJustify topJustify 	= maskToJustify(encoded, JUSTIFY_TOP_TYPE);

		final int right 		= maskToInt(encoded, JUSTIFY_RIGHT_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit rightUnit = maskToUnit(encoded, JUSTIFY_RIGHT_UNIT, UNIT_BITS);
		final CSSJustify rightJustify = maskToJustify(encoded, JUSTIFY_RIGHT_TYPE);

		final int bottom 			= maskToInt(encoded, JUSTIFY_BOTTOM_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit bottomUnit 	= maskToUnit(encoded, JUSTIFY_BOTTOM_UNIT, UNIT_BITS);
		final CSSJustify bottomJustify 	= maskToJustify(encoded, JUSTIFY_BOTTOM_TYPE);
		
		final int left 			= maskToInt(encoded, JUSTIFY_LEFT_SHIFT, JUSTIFY_SIZE_BITS);
		final CSSUnit leftUnit 	= maskToUnit(encoded, JUSTIFY_LEFT_UNIT, UNIT_BITS);
		final CSSJustify leftJustify 	= maskToJustify(encoded, JUSTIFY_LEFT_TYPE);

		setter.set(param, top, topUnit, topJustify, right, rightUnit, rightJustify, bottom, bottomUnit, bottomJustify, left, leftUnit, leftJustify);
	}

	private static void setJustify(
			long [] buf, int offset,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {

		// Merge with existing values since this may be an update
		long encoded = buf[offset];
		
		if (topType != CSSJustify.NONE) {
			encoded &= 0x0000FFFFFFFFFFFFL;
			encoded |= 	  sizeLong(top, topType) 	<< JUSTIFY_TOP_SHIFT
						| ordinalLong(topUnit)		<< JUSTIFY_TOP_UNIT
						| ordinalLong(topType)		<< JUSTIFY_TOP_TYPE;
			
		}

		if (rightType != CSSJustify.NONE) {
			encoded &= 0xFFFF0000FFFFFFFFL;
			encoded |=   sizeLong(right, rightType)		<< JUSTIFY_RIGHT_SHIFT
					   | ordinalLong(rightUnit) 		<< JUSTIFY_RIGHT_UNIT
					   | ordinalLong(rightType) 		<< JUSTIFY_RIGHT_TYPE;
		}
		
		
		if (bottomType != CSSJustify.NONE) {
			encoded &= 0xFFFFFFFF0000FFFFL;
			encoded |=	  sizeLong(bottom, bottomType)	<< JUSTIFY_BOTTOM_SHIFT
						| ordinalLong(bottomUnit) 		<< JUSTIFY_BOTTOM_UNIT
						| ordinalLong(bottomType) 		<< JUSTIFY_BOTTOM_TYPE;

		}
		
		
		if (leftType != CSSJustify.NONE) {
			encoded &= 0xFFFFFFFFFFFF0000L;
			encoded |=    sizeLong(left, leftType) 	<< JUSTIFY_LEFT_SHIFT
						| ordinalLong(leftUnit) 	<< JUSTIFY_LEFT_UNIT
						| ordinalLong(leftType) 	<< JUSTIFY_LEFT_TYPE;
		}
		
		buf[offset] = encoded;
	}
	
	private static final long sizeLong(int size, CSSJustify type) {
		return type == CSSJustify.SIZE ? size : 0;
	}

	private static final long ordinalLong(CSSUnit unit) {
		return unit != null ? unit.ordinal() : 0;
	}
	
	private static final long ordinalLong(CSSJustify type) {
		return type.ordinal();
	}
	
	private static void setStyle(long [] buf, int offset, CSSJustify type, CSStyle style) {
		if (type != CSSJustify.NONE) {
			addStyle(style, buf, offset);
		}
		else {
			// Keep current
			//clearStyle(style, buf, offset);
		}
	}
	
	public static void setMargin(long [] buf, int offset,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {

		setStyle(buf, offset, topType, CSStyle.MARGIN_TOP);
		setStyle(buf, offset, rightType, CSStyle.MARGIN_RIGHT);
		setStyle(buf, offset, bottomType, CSStyle.MARGIN_BOTTOM);
		setStyle(buf, offset, leftType, CSStyle.MARGIN_LEFT);
		
		setJustify(buf, offset + IDX_MARGIN, top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}
	
	public static void setPadding(long [] buf, int offset,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		setStyle(buf, offset, topType, CSStyle.PADDING_TOP);
		setStyle(buf, offset, rightType, CSStyle.PADDING_RIGHT);
		setStyle(buf, offset, bottomType, CSStyle.PADDING_BOTTOM);
		setStyle(buf, offset, leftType, CSStyle.PADDING_LEFT);

		setJustify(buf, offset + IDX_PADDING, top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	public static <PARAM> void getMargin(long [] buf, int offset, ICSSJustify<PARAM> setter, PARAM param) {
		getJustify(buf[offset + IDX_MARGIN], setter, param);
	}

	public static <PARAM> void getPadding(long [] buf, int offset, ICSSJustify<PARAM> setter, PARAM param) {
		getJustify(buf[offset + IDX_PADDING], setter, param);
	}

	public static void addLeft(long [] buf, int offset, int left, CSSUnit unit) {
		
		if (left > LEFT_MAX) {
			throw new IllegalArgumentException("left > LEFT_MAX: " + left);
		}
		
		addStyle(CSStyle.LEFT, buf, offset);
		
		long encoded =((long)left) << (UNIT_BITS + TOP_BITS + UNIT_BITS) | ((long)unit.ordinal()) << (TOP_BITS + UNIT_BITS); 
		
		buf[offset + IDX_POSITION] |= encoded;
	}

	public static void addTop(long [] buf, int offset, int top, CSSUnit unit) {

		if (top > TOP_MAX) {
			throw new IllegalArgumentException("top > TOP_MAX: " + top);
		}
		
		addStyle(CSStyle.TOP, buf, offset);
		
		long encoded = ((long)top) << UNIT_BITS | ((long)unit.ordinal());
		
		buf[offset + IDX_POSITION] |= encoded; 
	}

	
	public static void addWidth(long [] buf, int offset, int width, CSSUnit unit) {
		
		if (width > WIDTH_MAX) {
			throw new IllegalArgumentException("width > WIDTH_MAX: " + width);
		}
		
		addStyle(CSStyle.WIDTH, buf, offset);
		
		long encoded =((long)width) << (UNIT_BITS + HEIGHT_BITS + UNIT_BITS) | ((long)unit.ordinal()) << (HEIGHT_BITS + UNIT_BITS); 
		
		buf[offset + IDX_DIMENSIONS] |= encoded;
	}

	public static void addHeight(long [] buf, int offset, int height, CSSUnit unit) {

		if (height > HEIGHT_MAX) {
			throw new IllegalArgumentException("height > HEIGHT_MAX: " + height);
		}
		
		addStyle(CSStyle.HEIGHT, buf, offset);
		
		long encoded = ((long)height) << UNIT_BITS | ((long)unit.ordinal());
		
		buf[offset + IDX_DIMENSIONS] |= encoded; 
	}

	public static int getLeft(long [] buf, int offset) {
		final long left = buf[offset + IDX_POSITION] >> (UNIT_BITS  + TOP_BITS + UNIT_BITS);
		
		return (int)left;
	}

	public static CSSUnit getLeftUnit(long [] buf, int offset) {
		final int ordinal = (int)((buf[offset + IDX_POSITION] >> (TOP_BITS + UNIT_BITS)) & ((1 << UNIT_BITS) - 1));

		return CSSUnit.values()[ordinal];
	}

	public static int getTop(long [] buf, int offset) {
		final long top = buf[offset + IDX_POSITION] >> (UNIT_BITS) & ((1 << TOP_BITS) - 1);
		
		return (int)top;
	}

	public static CSSUnit getTopUnit(long [] buf, int offset) {
		final int ordinal = (int)(buf[offset + 1] & ((1 << UNIT_BITS) - 1));
		
		return CSSUnit.values()[ordinal];
	}

	public static int getWidth(long [] buf, int offset) {
		final long width = buf[offset + IDX_DIMENSIONS] >> (UNIT_BITS  + HEIGHT_BITS + UNIT_BITS);
		
		return (int)width;
	}

	public static CSSUnit getWidthUnit(long [] buf, int offset) {
		final int ordinal = (int)((buf[offset + IDX_DIMENSIONS] >> (HEIGHT_BITS + UNIT_BITS)) & ((1 << UNIT_BITS) - 1));

		return CSSUnit.values()[ordinal];
	}

	public static int getHeight(long [] buf, int offset) {
		final long height = buf[offset + IDX_DIMENSIONS] >> (UNIT_BITS) & ((1 << HEIGHT_BITS) - 1);
		
		return (int)height;
	}

	public static CSSUnit getHeightUnit(long [] buf, int offset) {
		final int ordinal = (int)(buf[offset + 1] & ((1 << UNIT_BITS) - 1));
		
		return CSSUnit.values()[ordinal];
	}
	
	private static void updateFlag(long [] buf, int offset, CSStyle style, int ordinal, int shift, long clear) {
		
		addStyle(style, buf, offset);
		
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
		updateFlag(buf, offset, CSStyle.DISPLAY, display.ordinal(), DISPLAY_SHIFT, DISPLAY_CLEAR);
	}
	
	public static void setPosition(long [] buf, int offset, CSSPosition position) {
		updateFlag(buf, offset, CSStyle.POSITION, position.ordinal(), POSITION_SHIFT, POSITION_CLEAR);
	}

	public static void setFloat(long [] buf, int offset, CSSFloat _float) {
		updateFlag(buf, offset, CSStyle.FLOAT, _float.ordinal(), FLOAT_SHIFT, FLOAT_CLEAR);
	}

	public static void setTextAlign(long [] buf, int offset, CSSTextAlign textAlign) {
		updateFlag(buf, offset, CSStyle.TEXT_ALIGN, textAlign.ordinal(), TEXT_ALIGN_SHIFT, TEXT_ALIGN_CLEAR);
	}

	public static void setOverflow(long [] buf, int offset, CSSOverflow overflow) {
		updateFlag(buf, offset,CSStyle.OVERFLOW, overflow.ordinal(), OVERFLOW_SHIFT, OVERFLOW_CLEAR);
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

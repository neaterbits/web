package com.test.web.css.common;

/**
 * For reading back CSS justify values back from buffer 
 * 
 * Decimal point sizes are encoded as integer that would require further processing
 * 
 * @author nhl
 *
 */

public interface ICSSJustify<PARAM> {

	void set(PARAM param, int left, CSSUnit leftUnit, Justify leftType,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType);
	
}

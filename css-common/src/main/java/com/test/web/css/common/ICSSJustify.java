package com.test.web.css.common;

import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSSJustify;

/**
 * For reading back CSS justify values back from buffer 
 * 
 * Decimal point sizes are encoded as integer that would require further processing
 * 
 * @author nhl
 *
 */

public interface ICSSJustify<PARAM> {

	void set(PARAM param,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType);
	
}

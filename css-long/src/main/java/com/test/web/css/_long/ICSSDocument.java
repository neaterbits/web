package com.test.web.css._long;

import com.test.web.css.common.CSSDisplay;
import com.test.web.css.common.CSSFloat;
import com.test.web.css.common.CSSTarget;
import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.CSStyle;
import com.test.web.css.common.ICSSJustify;
import com.test.web.parse.css.CSSOverflow;
import com.test.web.parse.css.CSSPosition;
import com.test.web.parse.css.CSSTextAlign;

/**
 * Read-only access to a CSS document, must be assumed to be thread-safe after parsing, ie. we will make sure to never update while rendering.
 *
 * @author nhl
 *
 */

public interface ICSSDocument<TARGET> {

	/**
	 * Check whether a particular style is set for a target in this document
	 * 
	 * @param target
	 * @param targetName
	 * @param style
	 * 
	 * @return true if set
	 */
	
	boolean isSet(CSSTarget target, String targetName, CSStyle style);

	TARGET get(CSSTarget target, String targetName);
	
	int getWidth(TARGET ref);
	
	CSSUnit getWidthUnit(TARGET ref);

	int getHeight(TARGET ref);
	
	CSSUnit getHeightUnit(TARGET ref);
	
	<PARAM> void getMargins(TARGET ref, ICSSJustify<PARAM> listener, PARAM param);

	<PARAM >void getPadding(TARGET ref, ICSSJustify<PARAM> listener, PARAM param);
	
	CSSDisplay getDisplay(TARGET ref);

	CSSPosition getPosition(TARGET ref);
	
	CSSFloat getFloat(TARGET ref);

	CSSTextAlign getTextAlign(TARGET ref);

	CSSOverflow getOverflow(TARGET ref);
}

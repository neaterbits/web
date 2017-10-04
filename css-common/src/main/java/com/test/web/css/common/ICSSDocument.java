package com.test.web.css.common;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;

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
	
	boolean isSet(TARGET ref, CSStyle style);
	
	int getLeft(TARGET ref);
	
	CSSUnit getLeftUnit(TARGET ref);

	int getTop(TARGET ref);
	
	CSSUnit getTopUnit(TARGET ref);
	
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
	
	String getFontFamily(TARGET ref);
	
	String getFontName(TARGET ref);
	
	int getFontSize(TARGET ref);
	
	short getZIndex(TARGET ref);
}

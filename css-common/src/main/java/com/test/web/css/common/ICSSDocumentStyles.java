package com.test.web.css.common;

import com.test.web.css.common.enums.CSSBackground;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;

public interface ICSSDocumentStyles<TARGET> {
	
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
	
	int getColorR(TARGET ref);
	
	int getColorG(TARGET ref);
	
	int getColorB(TARGET ref);

	int getColorA(TARGET ref);
	
	CSSForeground getColorType(TARGET ref);

	int getBgColorR(TARGET ref);
	
	int getBgColorG(TARGET ref);
	
	int getBgColorB(TARGET ref);

	int getBgColorA(TARGET ref);
	
	CSSBackground getBgColorType(TARGET ref);

	String getFontFamily(TARGET ref);
	
	String getFontName(TARGET ref);
	
	int getFontSize(TARGET ref);
	
	CSSUnit getFontSizeUnit(TARGET ref);
	
	CSSFontSize getFontSizeEnum(TARGET ref);

	int getFontWeightNumber(TARGET ref);

	CSSFontWeight getFontWeightEnum(TARGET ref);

	short getZIndex(TARGET ref);

	// min/max dims
	int getMinWidth(TARGET ref);
	
	CSSUnit getMinWidthUnit(TARGET ref);

	CSSMin getMinWidthType(TARGET ref);
	
	int getMinHeight(TARGET ref);
	
	CSSUnit getMinHeightUnit(TARGET ref);

	CSSMin getMinHeightType(TARGET ref);

	int getMaxWidth(TARGET ref);
	
	CSSUnit getMaxWidthUnit(TARGET ref);

	CSSMax getMaxWidthType(TARGET ref);
	
	int getMaxHeight(TARGET ref);
	
	CSSUnit getMaxHeightUnit(TARGET ref);

	CSSMax getMaxHeightType(TARGET ref);
}

package com.test.web.css.common;

import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSClear;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
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
import com.test.web.css.common.enums.CSSTextDecoration;
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
	
	CSSClear getClear(TARGET ref);

	CSSTextAlign getTextAlign(TARGET ref);

	CSSOverflow getOverflow(TARGET ref);

	CSSTextDecoration getTextDecoration(TARGET ref);

	int getColorR(TARGET ref);
	
	int getColorG(TARGET ref);
	
	int getColorB(TARGET ref);

	int getColorA(TARGET ref);
	
	CSSForeground getColorType(TARGET ref);
	
	int getNumBgLayers(TARGET ref);

	boolean isBgSet(TARGET ref, int bgLayer, CSStyle style);

	String getBgImageURL(TARGET ref, int bgLayer);
	
	CSSBackgroundImage getBgImage(TARGET ref, int bgLayer);
	
	// bg position
	int getBgPositionLeft(TARGET ref, int bgLayer); // set if unit is non null
	CSSUnit getBgPositionLeftUnit(TARGET ref, int bgLayer);

	int getBgPositionTop(TARGET ref, int bgLayer); // set if unit is non null
	CSSUnit getBgPositionTopUnit(TARGET ref, int bgLayer);

	CSSBackgroundPosition getBgPosition(TARGET ref, int bgLayer);
	
	// bg size
	int getBgWidth(TARGET ref, int bgLayer);
	CSSUnit getBgWidthUnit(TARGET ref, int bgLayer);
	
	int getBgHeight(TARGET ref, int bgLayer);
	CSSUnit getBgHeightUnit(TARGET ref, int bgLayer);
	
	CSSBackgroundSize getBgSize(TARGET ref, int bgLayer);
	
	// bg repeat
	CSSBackgroundRepeat getBgRepeat(TARGET ref, int bgLayer);
	
	// bg attachment
	CSSBackgroundAttachment getBgAttachment(TARGET ref, int bgLayer);
	
	// bg origin
	CSSBackgroundOrigin getBgOrigin(TARGET ref, int bgLayer);
	
	// bg clip
	CSSBackgroundOrigin getBgClip(TARGET ref, int bgLayer);
	
	// bg color
	/*
	int getBgColorR(TARGET ref, int bgLayer);
	
	int getBgColorG(TARGET ref, int bgLayer);
	
	int getBgColorB(TARGET ref, int bgLayer);

	int getBgColorA(TARGET ref, int bgLayer);
	
	CSSBackgroundColor getBgColorType(TARGET ref, int bgLayer);
	*/
	
	int getBgColorR(TARGET ref);

	int getBgColorG(TARGET ref);
	
	int getBgColorB(TARGET ref);

	int getBgColorA(TARGET ref);
	
	CSSBackgroundColor getBgColorType(TARGET ref);

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

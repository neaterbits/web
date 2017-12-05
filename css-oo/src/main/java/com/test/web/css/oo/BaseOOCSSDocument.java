package com.test.web.css.oo;

import org.assertj.core.description.TextDescription;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSBackground;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.css.CSSParserListener;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseOOCSSDocument 
		implements CSSParserListener<OOTokenizer, Void>, ICSSDocumentStyles<OOCSSElement> {

	private OOCSSElement curParseElement;

	/***************************************************** Access interface *****************************************************/ 
	
	@Override
	public boolean isSet(OOCSSElement ref, CSStyle style) {
		return ref.hasStyle(style);
	}
	
	@Override
	public int getLeft(OOCSSElement ref) {
		return ref.getLeft();
	}

	@Override
	public CSSUnit getLeftUnit(OOCSSElement ref) {
		return ref.getLeftUnit();
	}

	@Override
	public int getTop(OOCSSElement ref) {
		return ref.getTop();
	}

	@Override
	public CSSUnit getTopUnit(OOCSSElement ref) {
		return ref.getTopUnit();
	}

	@Override
	public int getWidth(OOCSSElement ref) {
		return ref.getWidth();
	}

	@Override
	public CSSUnit getWidthUnit(OOCSSElement ref) {
		return ref.getWidthUnit();
	}

	@Override
	public int getHeight(OOCSSElement ref) {
		return ref.getHeight();
	}

	@Override
	public CSSUnit getHeightUnit(OOCSSElement ref) {
		return ref.getHeightUnit();
	}
	
	private <PARAM> void set(OOWrapping ref, ICSSJustify<PARAM> setter, PARAM param) {
		
		setter.set(param,
				ref.getTop(), ref.getTopUnit(), ref.getTopType(),
				ref.getRight(), ref.getRightUnit(), ref.getRightType(),
				ref.getBottom(), ref.getBottomUnit(), ref.getBottomType(),
				ref.getLeft(), ref.getLeftUnit(), ref.getLeftType());
		
	}

	@Override
	public <PARAM> void getMargins(OOCSSElement ref, ICSSJustify<PARAM> setter, PARAM param) {
		set(ref.getMargins(), setter, param);
	}

	@Override
	public <PARAM> void getPadding(OOCSSElement ref, ICSSJustify<PARAM> setter, PARAM param) {
		set(ref.getPadding(), setter, param);
	}

	@Override
	public CSSDisplay getDisplay(OOCSSElement ref) {
		return ref.getDisplay();
	}

	@Override
	public CSSPosition getPosition(OOCSSElement ref) {
		return ref.getPosition();
	}

	@Override
	public CSSFloat getFloat(OOCSSElement ref) {
		return ref.getFloat();
	}

	@Override
	public CSSTextAlign getTextAlign(OOCSSElement ref) {
		return ref.getTextAlign();
	}

	@Override
	public CSSOverflow getOverflow(OOCSSElement ref) {
		return ref.getOverflow();
	}

	@Override
	public CSSTextDecoration getTextDecoration(OOCSSElement ref) {
		return ref.getTextDecoration();
	}

	@Override
	public int getColorR(OOCSSElement ref) {
		return ref.getColorR();
	}

	@Override
	public int getColorG(OOCSSElement ref) {
		return ref.getColorG();
	}

	@Override
	public int getColorB(OOCSSElement ref) {
		return ref.getColorB();
	}

	@Override
	public int getColorA(OOCSSElement ref) {
		return ref.getColorA();
	}
	
	@Override
	public CSSForeground getColorType(OOCSSElement ref) {
		return ref.getColorType();
	}

	@Override
	public int getBgColorR(OOCSSElement ref) {
		return ref.getBgColorR();
	}

	@Override
	public int getBgColorG(OOCSSElement ref) {
		return ref.getBgColorG();
	}

	@Override
	public int getBgColorB(OOCSSElement ref) {
		return ref.getBgColorB();
	}

	@Override
	public int getBgColorA(OOCSSElement ref) {
		return ref.getBgColorA();
	}

	@Override
	public CSSBackground getBgColorType(OOCSSElement ref) {
		return ref.getBgColorType();
	}

	@Override
	public int getMinWidth(OOCSSElement ref) {
		return ref.getMinWidth();
	}

	@Override
	public CSSUnit getMinWidthUnit(OOCSSElement ref) {
		return ref.getMinWidthUnit();
	}

	@Override
	public CSSMin getMinWidthType(OOCSSElement ref) {
		return ref.getMinWidthType();
	}

	@Override
	public int getMinHeight(OOCSSElement ref) {
		return ref.getMinHeight();
	}

	@Override
	public CSSUnit getMinHeightUnit(OOCSSElement ref) {
		return ref.getMinHeightUnit();
	}

	@Override
	public CSSMin getMinHeightType(OOCSSElement ref) {
		return ref.getMinHeightType();
	}

	@Override
	public int getMaxWidth(OOCSSElement ref) {
		return ref.getMaxWidth();
	}

	@Override
	public CSSUnit getMaxWidthUnit(OOCSSElement ref) {
		return ref.getMaxWidthUnit();
	}

	@Override
	public CSSMax getMaxWidthType(OOCSSElement ref) {
		return ref.getMaxWidthType();
	}

	@Override
	public int getMaxHeight(OOCSSElement ref) {
		return ref.getMaxHeight();
	}

	@Override
	public CSSUnit getMaxHeightUnit(OOCSSElement ref) {
		return ref.getMaxHeightUnit();
	}

	@Override
	public CSSMax getMaxHeightType(OOCSSElement ref) {
		return ref.getMaxHeightType();
	}

	/***************************************************** Parse listener *****************************************************/ 
	
	final OOCSSElement ref() {
		return curParseElement;
	}
	

	@Override
	public void onLeft(Void context, int left, CSSUnit unit) {
		ref().addLeft(left, unit);
	}

	@Override
	public void onTop(Void context, int top, CSSUnit unit) {
		ref().addTop(top, unit);
	}

	@Override
	public void onWidth(Void context, int width, CSSUnit unit) {
		ref().addWidth(width, unit);
	}

	@Override
	public void onHeight(Void context, int height, CSSUnit unit) {
		ref().addHeight(height, unit);
	}

	
	@Override
	public void onMargin(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		ref().setMargins(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onPadding(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		ref().setPadding(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}
	
	@Override
	public void onColor(Void context, int r, int g, int b, int a) {
		ref().setColorRGB(r, g, b, a);
	}

	@Override
	public void onColor(Void context, CSSColor color) {
		ref().setColorCSS(color);
	}
	
	@Override
	public void onColor(Void context, CSSForeground foreground) {
		ref().setColorType(foreground);
	}

	@Override
	public void onBackgroundColor(Void context, int r, int g, int b, int a) {
		ref().setBgColorRGB(r, g, b, a);
	}

	@Override
	public void onBackgroundColor(Void context, CSSColor color) {
		ref().setBgColorCSS(color);
	}

	@Override
	public void onBackgroundColor(Void context, CSSBackground background) {
		ref().setBgColorType(background);
	}

	@Override
	public void onTextAlign(Void context, CSSTextAlign textAlign) {
		ref().setTextAlign(textAlign);
		
	}
	@Override
	public void onDisplay(Void context, CSSDisplay display) {
		ref().setDisplay(display);
	}

	@Override
	public void onPosition(Void context, CSSPosition position) {
		ref().setPosition(position);
	}

	@Override
	public void onFloat(Void context, CSSFloat _float) {
		ref().setFloat(_float);
	}

	@Override
	public void onOverflow(Void context, CSSOverflow overflow) {
		ref().setOverflow(overflow);
	}

	@Override
	public void onTextDecoration(Void context, CSSTextDecoration textDecoration) {
		ref().setTextDecoration(textDecoration);
	}

	@Override
	public void onMaxWidth(Void context, int width, CSSUnit unit, CSSMax type) {
		ref().setMaxWidth(width, unit, type);
	}

	@Override
	public void onMaxHeight(Void context, int height, CSSUnit unit, CSSMax type) {
		ref().setMaxHeight(height, unit, type);
	}

	@Override
	public void onMinWidth(Void context, int width, CSSUnit unit, CSSMin type) {
		ref().setMinWidth(width, unit, type);
	}

	@Override
	public void onMinHeight(Void context, int height, CSSUnit unit, CSSMin type) {
		ref().setMinHeight(height, unit, type);
	}
	
	
	@Override
	public void onFontSize(Void context, int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum) {
		ref().setFontSize(fontSize, fontSizeUnit, fontSizeEnum);
	}
	
	@Override
	public void onFontWeight(Void context, int fontWeightNumber, CSSFontWeight fontWeightEnum) {
		ref().setFontWeight(fontWeightNumber, fontWeightEnum);
	}

	@Override
	public short getZIndex(OOCSSElement ref) {
		return ref.getZIndex();
	}

	@Override
	public String getFontFamily(OOCSSElement ref) {
		return ref.getFontFamily();
	}

	@Override
	public String getFontName(OOCSSElement ref) {
		return ref.getFontName();
	}

	@Override
	public int getFontSize(OOCSSElement ref) {
		return ref.getFontSize();
	}
	
	@Override
	public CSSUnit getFontSizeUnit(OOCSSElement ref) {
		return ref.getFontSizeUnit();
	}

	@Override
	public CSSFontSize getFontSizeEnum(OOCSSElement ref) {
		return ref.getFontSizeEnum();
	}

	@Override
	public int getFontWeightNumber(OOCSSElement ref) {
		return ref.getFontWeightNumber();
	}

	@Override
	public CSSFontWeight getFontWeightEnum(OOCSSElement ref) {
		return ref.getFontWeightEnum();
	}

	protected final OOCSSElement allocateCurParseElement() {
		this.curParseElement = new OOCSSElement();
		
		return this.curParseElement;
	}
}

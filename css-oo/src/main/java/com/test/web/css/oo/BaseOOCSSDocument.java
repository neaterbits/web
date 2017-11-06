package com.test.web.css.oo;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
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

	/***************************************************** Parse listener *****************************************************/ 
	
	private OOCSSElement ref() {
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
	public void onBackgroundColor(Void context, int r, int g, int b) {
		// TODO Auto-generated method stub
		
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
	
	protected final OOCSSElement allocateCurParseElement(String name) {
		this.curParseElement = new OOCSSElement(name);
		
		return this.curParseElement;
	}
}

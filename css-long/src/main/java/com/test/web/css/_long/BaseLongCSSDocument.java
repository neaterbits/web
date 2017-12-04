package com.test.web.css._long;

import com.test.web.buffers.LongBuffersIntegerIndex;
import com.test.web.buffers.StringStorageBuffer;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.css.CSSParserListener;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseLongCSSDocument 
		extends LongBuffersIntegerIndex
		implements CSSParserListener<LongTokenizer, Void>, ICSSDocumentStyles<Integer> {

	private StringStorageBuffer fontBuffer;

	private int curParseElement;

	final int ref() {
		return curParseElement;
	}
	
	/***************************************************** Access interface *****************************************************/ 
	
	@Override
	public boolean isSet(Integer ref, CSStyle style) {
		return LongCSS.hasStyle(style, buf(ref), offset(ref));
	}
	
	@Override
	public int getLeft(Integer ref) {
		return LongCSS.getLeft(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getLeftUnit(Integer ref) {
		return LongCSS.getLeftUnit(buf(ref), offset(ref));
	}

	@Override
	public int getTop(Integer ref) {
		return LongCSS.getTop(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getTopUnit(Integer ref) {
		return LongCSS.getTopUnit(buf(ref), offset(ref));
	}

	@Override
	public int getWidth(Integer ref) {
		return LongCSS.getWidth(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getWidthUnit(Integer ref) {
		return LongCSS.getWidthUnit(buf(ref), offset(ref));
	}

	@Override
	public int getHeight(Integer ref) {
		return LongCSS.getHeight(buf(ref), offset(ref));
	}

	@Override
	public CSSUnit getHeightUnit(Integer ref) {
		return LongCSS.getHeightUnit(buf(ref), offset(ref));
	}

	@Override
	public <PARAM> void getMargins(Integer ref, ICSSJustify<PARAM> setter, PARAM param) {
		LongCSS.getMargin(buf(ref), offset(ref), setter, param);
	}

	@Override
	public <PARAM> void getPadding(Integer ref, ICSSJustify<PARAM> setter, PARAM param) {
		LongCSS.getPadding(buf(ref), offset(ref), setter, param);
	}

	@Override
	public CSSDisplay getDisplay(Integer ref) {
		return LongCSS.getDisplay(buf(ref), offset(ref));
	}

	@Override
	public CSSPosition getPosition(Integer ref) {
		return LongCSS.getPosition(buf(ref), offset(ref));
	}

	@Override
	public CSSFloat getFloat(Integer ref) {
		return LongCSS.getFloat(buf(ref), offset(ref));
	}

	@Override
	public CSSTextAlign getTextAlign(Integer ref) {
		return LongCSS.getTextAlign(buf(ref), offset(ref));
	}

	@Override
	public CSSOverflow getOverflow(Integer ref) {
		return LongCSS.getOverflow(buf(ref), offset(ref));
	}

	/***************************************************** Parse listener *****************************************************/ 
	

	@Override
	public void onLeft(Void context, int left, CSSUnit unit) {
		LongCSS.addLeft(buf(curParseElement), offset(curParseElement), left, unit);
	}

	@Override
	public void onTop(Void context, int top, CSSUnit unit) {
		LongCSS.addTop(buf(curParseElement), offset(curParseElement), top, unit);
	}

	@Override
	public void onWidth(Void context, int width, CSSUnit unit) {
		LongCSS.addWidth(buf(curParseElement), offset(curParseElement), width, unit);
	}

	@Override
	public void onHeight(Void context, int height, CSSUnit unit) {
		LongCSS.addHeight(buf(curParseElement), offset(curParseElement), height, unit);
	}

	@Override
	public void onMargin(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		LongCSS.setMargin(buf(curParseElement), offset(curParseElement), top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onPadding(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		LongCSS.setPadding(buf(curParseElement), offset(curParseElement), top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onBackgroundColor(Void context, int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextAlign(Void context, CSSTextAlign textAlign) {
		LongCSS.setTextAlign(buf(curParseElement), offset(curParseElement), textAlign);
		
	}
	@Override
	public void onDisplay(Void context, CSSDisplay display) {
		LongCSS.setDisplay(buf(curParseElement), offset(curParseElement), display);
	}

	@Override
	public void onPosition(Void context, CSSPosition position) {
		LongCSS.setPosition(buf(curParseElement), offset(curParseElement), position);
	}

	@Override
	public void onFloat(Void context, CSSFloat _float) {
		LongCSS.setFloat(buf(curParseElement), offset(curParseElement), _float);
	}

	@Override
	public void onOverflow(Void context, CSSOverflow overflow) {
		LongCSS.setOverflow(buf(curParseElement), offset(curParseElement), overflow);
	}
	
	@Override
	public void onMaxWidth(Void context, int width, CSSUnit unit, CSSMax type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMaxHeight(Void context, int height, CSSUnit unit, CSSMax type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinWidth(Void context, int width, CSSUnit unit, CSSMin type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinHeight(Void context, int height, CSSUnit unit, CSSMin type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getZIndex(Integer ref) {
		return LongCSS.getZIndex(buf(ref), offset(ref));
	}

	@Override
	public String getFontFamily(Integer ref) {
		final int fontFamily = LongCSS.getFontFamily(buf(ref), offset(ref));

		return fontBuffer.getString(fontFamily);
	}

	@Override
	public String getFontName(Integer ref) {
		final int fontName = LongCSS.getFontName(buf(ref), offset(ref));

		return fontBuffer.getString(fontName);
	}

	@Override
	public int getFontSize(Integer ref) {
		return LongCSS.getFontSize(buf(ref), offset(ref));
	}
	
	@Override
	public int getMinWidth(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getMinWidthUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSMin getMinWidthType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMinHeight(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getMinHeightUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSMin getMinHeightType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxWidth(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getMaxWidthUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSMax getMaxWidthType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxHeight(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getMaxHeightUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSMax getMaxHeightType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	protected final int allocateCurParseElement() {
		this.curParseElement = allocate(LongCSS.CSS_ENTITY_COMPACT, "style element");
		
		return this.curParseElement;
	}
}

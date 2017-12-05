package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;

public final class OOCSSElement {
	private long styles;
	
	private int left;
	private CSSUnit leftUnit;
	
	private int top;
	private CSSUnit topUnit;
	
	private int width;
	private CSSUnit widthUnit;
	
	private int height;
	private CSSUnit heightUnit;
	
	private final OOWrapping margins;
	private final OOWrapping padding;
	
	private CSSDisplay display;
	private CSSPosition position;
	private CSSFloat _float;
	private CSSTextAlign textAlign;
	private CSSOverflow overflow;
	
	private short zIndex;
	
	private String fontFamily;
	private String fontName;
	private int fontSize;
	private CSSUnit fontSizeUnit;
	private CSSFontSize fontSizeEnum;

	private int minWidth;
	private CSSUnit minWidthUnit;
	private CSSMin minWidthType;
	
	private int minHeight;
	private CSSUnit minHeightUnit;
	private CSSMin minHeightType;
	
	private int maxWidth;
	private CSSUnit maxWidthUnit;
	private CSSMax maxWidthType;
	
	private int maxHeight;
	private CSSUnit maxHeightUnit;
	private CSSMax maxHeightType;

	static {
		if (CSStyle.values().length > 64) {
			throw new IllegalStateException("CSStyle.values().length > 64");
		}
	}

	public OOCSSElement() {
		this.margins = new OOWrapping();
		this.padding = new OOWrapping();
	}
	
	boolean hasStyle(CSStyle style) {
		return (styles & (1L << style.ordinal())) != 0L;
	}
	
	private void set(CSStyle style) {
		styles |= 1L << style.ordinal();
	}

	int getLeft() {
		return left;
	}

	private void setLeft(int left) {
		this.left = left;
	}

	CSSUnit getLeftUnit() {
		return leftUnit;
	}

	private void setLeftUnit(CSSUnit leftUnit) {
		if (leftUnit == null) {
			throw new IllegalArgumentException("leftUnit == null");
		}
		
		this.leftUnit = leftUnit;
	}

	void addLeft(int left, CSSUnit leftUnit) {
		setLeft(left);
		setLeftUnit(leftUnit);

		set(CSStyle.LEFT);
	}
	
	int getTop() {
		return top;
	}

	private void setTop(int top) {
		this.top = top;
	}

	CSSUnit getTopUnit() {
		return topUnit;
	}

	private void setTopUnit(CSSUnit topUnit) {
		if (topUnit == null) {
			throw new IllegalArgumentException("topUnit == null");
		}
		
		this.topUnit = topUnit;
	}
	
	void addTop(int top, CSSUnit topUnit) {
		setTop(top);
		setTopUnit(topUnit);
		
		set(CSStyle.TOP);
	}

	int getWidth() {
		return width;
	}

	private void setWidth(int width) {
		this.width = width;
	}

	CSSUnit getWidthUnit() {
		return widthUnit;
	}

	private void setWidthUnit(CSSUnit widthUnit) {
		this.widthUnit = widthUnit;
	}

	void addWidth(int width, CSSUnit widthUnit) {
		setWidth(width);
		setWidthUnit(widthUnit);
		
		set(CSStyle.WIDTH);
	}
	
	int getHeight() {
		return height;
	}

	private void setHeight(int height) {
		this.height = height;
	}

	CSSUnit getHeightUnit() {
		return heightUnit;
	}

	private void setHeightUnit(CSSUnit heightUnit) {
		this.heightUnit = heightUnit;
	}
	
	void addHeight(int height, CSSUnit heightUnit) {
		setHeight(height);
		setHeightUnit(heightUnit);
		
		set(CSStyle.HEIGHT);
	}
	
	private boolean isSet(CSSUnit unit, CSSJustify type) {
		return type == CSSJustify.AUTO || (unit != null && type != null);
	}
	
	private void set(OOWrapping ref,
			int top, CSSUnit topUnit, CSSJustify topType, CSStyle topStyle,
			int right, CSSUnit rightUnit, CSSJustify rightType, CSStyle rightStyle,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType, CSStyle bottomStyle,
			int left, CSSUnit leftUnit, CSSJustify leftType, CSStyle leftStyle) {
		
		
		if (isSet(topUnit, topType)) {
			ref.setTop(top);
			ref.setTopUnit(topUnit);
			ref.setTopType(topType);

			set(topStyle);
		}
		

		if (isSet(rightUnit, rightType)) {
			ref.setRight(right);
			ref.setRightUnit(rightUnit);
			ref.setRightType(rightType);

			set(rightStyle);
		}

		
		if (isSet(bottomUnit, bottomType)) {
			ref.setBottom(bottom);
			ref.setBottomUnit(bottomUnit);
			ref.setBottomType(bottomType);

			set(bottomStyle);
		}
	
		if (isSet(leftUnit, leftType)) {
			ref.setLeft(left);
			ref.setLeftUnit(leftUnit);
			ref.setLeftType(leftType);

			set(leftStyle);
		}
	}

	
	void setMargins(
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		

		set(margins,
				top, topUnit, topType, CSStyle.MARGIN_TOP,
				right, rightUnit, rightType, CSStyle.MARGIN_RIGHT,
				bottom, bottomUnit, bottomType, CSStyle.MARGIN_BOTTOM,
				left, leftUnit, leftType, CSStyle.MARGIN_LEFT);

		// System.out.println("## set margin of: " + System.identityHashCode(this) + ": " + margins);
	}

	OOWrapping getMargins() {
		return margins;
	}

	void setPadding(
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		set(padding,
				top, topUnit, topType, CSStyle.PADDING_TOP,
				right, rightUnit, rightType, CSStyle.PADDING_RIGHT,
				bottom, bottomUnit, bottomType, CSStyle.PADDING_BOTTOM,
				left, leftUnit, leftType, CSStyle.PADDING_LEFT);
	}

	OOWrapping getPadding() {
		return padding;
	}

	CSSDisplay getDisplay() {
		return display;
	}

	void setDisplay(CSSDisplay display) {
		
		if (display == null) {
			throw new IllegalArgumentException("display == null");
		}
		
		this.display = display;
		
		set(CSStyle.DISPLAY);
	}

	CSSPosition getPosition() {
		return position;
	}

	void setPosition(CSSPosition position) {

		if (position == null) {
			throw new IllegalArgumentException("position == null");
		}
		
		this.position = position;
		
		set(CSStyle.POSITION);
	}

	CSSFloat getFloat() {
		return _float;
	}

	void setFloat(CSSFloat _float) {
		
		if (_float == null) {
			throw new IllegalArgumentException("_float == null");
		}
		
		this._float = _float;
		
		set(CSStyle.FLOAT);
	}

	CSSTextAlign getTextAlign() {
		return textAlign;
	}

	void setTextAlign(CSSTextAlign textAlign) {
		
		if (textAlign == null) {
			throw new IllegalArgumentException("textAlign == null");
		}
		
		this.textAlign = textAlign;
		
		set(CSStyle.TEXT_ALIGN);
	}

	CSSOverflow getOverflow() {
		return overflow;
	}

	void setOverflow(CSSOverflow overflow) {
		
		if (overflow == null) {
			throw new IllegalArgumentException("overflow == null");
		}
		
		this.overflow = overflow;
		
		set(CSStyle.OVERFLOW);
	}

	short getZIndex() {
		return zIndex;
	}

	void setZIndex(short zIndex) {
		this.zIndex = zIndex;
	}

	String getFontFamily() {
		return fontFamily;
	}

	void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	String getFontName() {
		return fontName;
	}

	void setFontName(String fontName) {
		this.fontName = fontName;
	}

	int getFontSize() {
		return fontSize;
	}
	
	CSSUnit getFontSizeUnit() {
		return fontSizeUnit;
	}

	CSSFontSize getFontSizeEnum() {
		return fontSizeEnum;
	}

	void setFontSize(int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum) {
		this.fontSize = fontSize;
		this.fontSizeUnit = fontSizeUnit;
		this.fontSizeEnum = fontSizeEnum;
	}

	int getMinWidth() {
		return minWidth;
	}

	CSSUnit getMinWidthUnit() {
		return minWidthUnit;
	}

	CSSMin getMinWidthType() {
		return minWidthType;
	}

	void setMinWidth(int minWidth, CSSUnit unit, CSSMin type) {
		this.minWidth = minWidth;
		this.minWidthUnit = unit;
		this.minWidthType = type;
	}

	int getMinHeight() {
		return minHeight;
	}

	CSSUnit getMinHeightUnit() {
		return minHeightUnit;
	}

	CSSMin getMinHeightType() {
		return minHeightType;
	}

	void setMinHeight(int minHeight, CSSUnit unit, CSSMin type) {
		this.minHeight = minHeight;
		this.minHeightUnit = unit;
		this.minHeightType = type;
	}

	int getMaxWidth() {
		return maxWidth;
	}

	CSSUnit getMaxWidthUnit() {
		return maxWidthUnit;
	}

	CSSMax getMaxWidthType() {
		return maxWidthType;
	}

	void setMaxWidth(int maxWidth, CSSUnit unit, CSSMax type) {
		this.maxWidth = maxWidth;
		this.maxWidthUnit = unit;
		this.maxWidthType = type;
	}

	int getMaxHeight() {
		return maxHeight;
	}

	CSSUnit getMaxHeightUnit() {
		return maxHeightUnit;
	}


	CSSMax getMaxHeightType() {
		return maxHeightType;
	}

	void setMaxHeight(int maxHeight, CSSUnit unit, CSSMax type) {
		this.maxHeight = maxHeight;
		this.maxHeightUnit = unit;
		this.maxHeightType = type;
	}
}


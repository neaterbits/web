package com.test.web.css.oo;

import java.util.Arrays;

import com.test.web.css.common.CSSStyleBuilder;
import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSClear;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFilter;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSPriority;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.types.ColorAlpha;
import com.test.web.types.ColorRGB;
import com.test.web.types.DecimalSize;
import com.test.web.types.IEnum;

public final class OOCSSStyles extends OOStylesText {

	private CSSValue [] values;
	private int numValues;
	
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
	private CSSClear clear;
	private CSSTextAlign textAlign;
	private CSSOverflow overflow;
	
	private CSSTextDecoration textDecoration;
	
	private int colorRGB; // bit-shifted, ColorRGB.NONE is not set
	private int colorAlpha; // decimal encoded, DecimalSize.NONE if not set
	private CSSColor colorCSS; // enumerated color
	private CSSForeground colorType;
	
	private OOBackroundLayer [] bgLayers;
	
	private int bgColorRGB; // bit-shifted, ColorRGB.NONE is not set
	private int bgColorAlpha; // decimal encoded, DecimalSize.NONE if not set
	private CSSColor bgColorCSS; // enumerated color
	private CSSBackgroundColor bgColorType;

	private short zIndex;
	
	private String fontFamily;
	private String fontName;
	private int fontSize;
	private CSSUnit fontSizeUnit;
	private CSSFontSize fontSizeEnum;
	
	private int fontWeightNumber;
	private CSSFontWeight fontWeightEnum;

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

	// filters
	private OOCSSFilter filter;
	
	public OOCSSStyles() {
		this.margins = new OOWrapping();
		this.padding = new OOWrapping();

		this.colorRGB = ColorRGB.NONE;
		this.colorAlpha = DecimalSize.NONE;

		this.bgColorRGB = ColorRGB.NONE;
		this.bgColorAlpha = DecimalSize.NONE;
	}
	
	void setPriority(int propertyIndex, CSSPriority priority) {
		values[propertyIndex].setPriority(priority);
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
		return type == CSSJustify.AUTO || type == CSSJustify.INITIAL || type == CSSJustify.INHERIT || (unit != null && type != null);
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
	
	CSSClear getClear() {
		return clear;
	}
	
	void setClear(CSSClear clear) {
		if (clear == null) {
			throw new IllegalArgumentException("clear == null");
		}
		
		this.clear = clear;
		
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

	CSSTextDecoration getTextDecoration() {
		return textDecoration;
	}

	void setTextDecoration(CSSTextDecoration textDecoration) {
		
		if (textDecoration == null) {
			throw new IllegalArgumentException("textDecoration == null");
		}
		
		this.textDecoration = textDecoration;

		set(CSStyle.TEXT_DECORATION);
	}

	void setColorRGB(int r, int g, int b, int a) {
		this.colorRGB = r << 16 | g << 8 | b;
		this.colorAlpha = a;
		this.colorCSS = null;
		this.colorType = null;
		
		set(CSStyle.COLOR);
	}

	CSSColor getColorCSS() {
		return colorCSS;
	}

	void setColorCSS(CSSColor cssColor) {
		this.colorCSS = cssColor;
		this.colorRGB = ColorRGB.NONE;
		this.colorAlpha = DecimalSize.NONE;
		this.colorType = null;
		
		set(CSStyle.COLOR);
	}
	
	void setColorType(CSSForeground colorType) {
		this.colorCSS = null;
		this.colorRGB = ColorRGB.NONE;
		this.colorAlpha = DecimalSize.NONE;
		this.colorType = colorType;

		set(CSStyle.COLOR);
	}
	
	int getColorR() {
		return getColorR(colorRGB, colorCSS);
	}
	
	int getColorG() {
		return getColorG(colorRGB, colorCSS);
	}
	
	int getColorB() {
		return getColorB(colorRGB, colorCSS);
	}

	int getColorA() {
		return getColorA(colorAlpha);
	}
	
	CSSForeground getColorType() {
		return colorType;
	}
	
	int getNumBgLayers() {
		return bgLayers == null ? 0 : bgLayers.length;
	}
	
	OOBackroundLayer getBgLayer(int idx) {
		if (bgLayers == null || idx >= bgLayers.length) {
			throw new IllegalArgumentException("no layer at idx " + idx);
		}
		
		return bgLayers[idx];
	}
	
	OOBackroundLayer getOrAddBgLayer(int idx) {
		if (bgLayers == null) {
			this.bgLayers = new OOBackroundLayer[idx + 1];

			addBgLayers(0, idx);
		}
		else if (idx >= bgLayers.length) {
			final int oldLen = bgLayers.length;
			
			this.bgLayers = Arrays.copyOf(this.bgLayers, idx + 1);
			
			addBgLayers(oldLen, idx);
		}

		return bgLayers[idx];
	}
	
	private void addBgLayers(int initialIdx, int maxIdx) {
		for (int i = initialIdx; i <= maxIdx; ++ i) {
			this.bgLayers[i] = new OOBackroundLayer();
		}
	}

	void setBgColorRGB(int r, int g, int b, int a) {
		this.bgColorRGB = r << 16 | g << 8 | b;
		this.bgColorAlpha = a;
		this.bgColorCSS = null;
		this.bgColorType = null;

		set(CSStyle.BACKGROUND_COLOR);
	}

	CSSColor getBgColorCSS() {
		return bgColorCSS;
	}

	void setBgColorCSS(CSSColor cssColor) {
		this.bgColorCSS = cssColor;
		this.bgColorRGB = ColorRGB.NONE;
		this.bgColorAlpha = DecimalSize.NONE;
		this.bgColorType = null;
		
		set(CSStyle.BACKGROUND_COLOR);
	}
	
	void setBgColorType(CSSBackgroundColor colorType) {
		this.bgColorCSS = null;
		this.bgColorRGB = ColorRGB.NONE;
		this.bgColorAlpha = DecimalSize.NONE;
		this.bgColorType = colorType;
		
		set(CSStyle.BACKGROUND_COLOR);
	}
	
	int getBgColorR() {
		return getColorR(bgColorRGB, bgColorCSS);
	}
	
	int getBgColorG() {
		return getColorG(bgColorRGB, bgColorCSS);
	}
	
	int getBgColorB() {
		return getColorB(bgColorRGB, bgColorCSS);
	}

	int getBgColorA() {
		return getColorA(bgColorAlpha);
	}
	
	CSSBackgroundColor getBgColorType() {
		return bgColorType;
	}

	short getZIndex() {
		return zIndex;
	}

	void setZIndex(short zIndex) {
		this.zIndex = zIndex;
		
		set(CSStyle.Z_INDEX);
	}

	String getFontFamily() {
		return fontFamily;
	}

	void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		
		set(CSStyle.FONT_FAMILY);
	}

	String getFontName() {
		return fontName;
	}

	void setFontName(String fontName) {
		this.fontName = fontName;
		
		set(CSStyle.FONT_NAME);
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
		
		set(CSStyle.FONT_SIZE);
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

	int getFontWeightNumber() {
		return fontWeightNumber;
	}

	CSSFontWeight getFontWeightEnum() {
		return fontWeightEnum;
	}

	void setFontWeight(int fontWeightNumber, CSSFontWeight fontWeightEnum) {
		this.fontWeightNumber = fontWeightNumber;
		this.fontWeightEnum = fontWeightEnum;
		
		set(CSStyle.FONT_WEIGHT);
	}

	void setMinWidth(int minWidth, CSSUnit unit, CSSMin type) {
		this.minWidth = minWidth;
		this.minWidthUnit = unit;
		this.minWidthType = type;

		set(CSStyle.MIN_WIDTH);
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
		
		set(CSStyle.MIN_HEIGHT);
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
		
		set(CSStyle.MAX_WIDTH);
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
		
		set(CSStyle.MAX_HEIGHT);
	}
	
	private OOCSSFilter assureFilter() {
		if (this.filter == null) {
			this.filter = new OOCSSFilter();
		}
		
		return filter;
	}
	
	CSSFilter getFilter() {
		return filter == null ? null : filter.getFilter();
	}
	
	void setFilter(CSSFilter filter) {
		assureFilter().setFilter(filter);
		
		set(CSStyle.FILTER);
	}
	
	int getBlur() {
		return filter == null ? -1 : filter.getBlur();
	}

	void setBlur(int blur) {
		assureFilter().setBlur(blur);
	}

	int getBrightness() {
		return filter == null ? DecimalSize.NONE : filter.getBrightness();
	}

	void setBrightness(int brightness) {
		assureFilter().setBrightness(brightness);
	}

	int getContrast() {
		return filter == null ? DecimalSize.NONE : filter.getContrast();
	}

	void setContrast(int contrast) {
		assureFilter().setContrast(contrast);
	}

	int getGrayscale() {
		return filter == null ? DecimalSize.NONE : filter.getGrayscale();
	}

	void setGrayscale(int grayscale) {
		assureFilter().setGrayscale(grayscale);
	}

	boolean hasDropShadow() {
		return filter == null ? false : filter.hasDropShadow();
	}
	
	int getDropShadowH() {
		return filter == null ? DecimalSize.NONE : filter.getDropShadowH();
	}

	CSSUnit getDropShadowHUnit() {
		return filter == null ? null : filter.getDropShadowHUnit();
	}

	int getDropShadowV() {
		return filter == null ? DecimalSize.NONE : filter.getDropShadowV();
	}

	CSSUnit getDropShadowVUnit() {
		return filter == null ? null : filter.getDropShadowVUnit();
	}

	int getDropShadowBlur() {
		return filter == null ? null : filter.getDropShadowBlur();
	}

	int getDropShadowSpread() {
		return filter == null ? null : filter.getDropShadowSpread();
	}

	int getDropShadowR() {
		return filter == null ? ColorRGB.NONE : filter.getDropShadowR();
	}

	int getDropShadowG() {
		return filter == null ? ColorRGB.NONE : filter.getDropShadowG();
	}

	int getDropShadowB() {
		return filter == null ? ColorRGB.NONE : filter.getDropShadowB();
	}

	int getDropShadowA() {
		return filter == null ? ColorAlpha.NONE : filter.getDropShadowA();
	}

	CSSColor getDropShadowColor() {
		return filter == null ? null : filter.getDropShadowColor();
	}

	void setDropShadow(
			int dropShadowH,
			CSSUnit dropShadowHUnit,
			int dropShadowV,
			CSSUnit dropShadowVUnit,
			int dropShadowBlur,
			int dropShadowSpread,

			int dropShadowR,
			int dropShadowG,
			int dropShadowB,
			int dropShadowA
			) {
		
		assureFilter().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowR, dropShadowG, dropShadowB, dropShadowA);
	}

	void setDropShadow(
			int dropShadowH,
			CSSUnit dropShadowHUnit,
			int dropShadowV,
			CSSUnit dropShadowVUnit,
			int dropShadowBlur,
			int dropShadowSpread,

			CSSColor dropShadowColor
			) {

		assureFilter().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowColor);
	}

	
	int getHueRotate() {
		return filter == null ? DecimalSize.NONE : filter.getHueRotate();
	}

	void setHueRotate(int hueRotate) {
		assureFilter().setHueRotate(hueRotate);
	}

	int getInvert() {
		return filter == null ? DecimalSize.NONE : filter.getInvert();
	}

	void setInvert(int invert) {
		assureFilter().setInvert(invert);
	}

	int getOpacity() {
		return filter == null ? DecimalSize.NONE : filter.getOpacity();
	}

	void setOpacity(int opacity) {
		assureFilter().setOpacity(opacity);
	}

	int getSaturate() {
		return filter == null ? DecimalSize.NONE : filter.getSaturate();
	}

	void setSaturate(int saturate) {
		assureFilter().setSaturate(saturate);
	}

	int getSepia() {
		return filter == null ? DecimalSize.NONE : filter.getSepia();
	}

	void setSepia(int sepia) {
		assureFilter().setSepia(sepia);
	}

	String getUrl() {
		return filter == null ? null : filter.getUrl();
	}

	void setUrl(String url) {
		assureFilter().setUrl(url);
	}
	
	private int getPropertyIdx(String propertyName) {
		int idx = -1;
		
		for (int i = 0; i < numValues; ++ i) {
			if (values[i].hasPropertyName(propertyName)) {
				idx = i;
				break;
			}
		}

		return idx;
	}

	int getLength() {
		return numValues;
	}
	
	CSStyle getPropertyStyleType(int idx) {
		return idx < numValues ? values[idx].getType() : null;
	}

	String getStyleItem(int idx) {
		
		return idx < numValues ? values[idx].getName() : null;
	}

	<RULE> String getPropertyValue(ICSSDocument<RULE> styles, RULE rule, int idx) {
		
		return idx < numValues ? values[idx].getValue(styles, rule) : null;
	}

	<RULE> String getPropertyValue(ICSSDocument<RULE> styles, RULE rule, String propertyName) {
		
		final int idx = getPropertyIdx(propertyName);
		
		return idx >= 0 ? values[idx].getValue(styles, rule) : null;
	}

	String getPropertyPriority(String propertyName) {
		
		final int idx = getPropertyIdx(propertyName);
		
		final String priority = idx >= 0 ? getPropertyPriority(idx) : null;

		return priority;
	}

	String getPropertyPriority(int idx) {
		
		final CSSPriority priority = idx >= 0 && idx < values.length ? values[idx].getPriority() : null;

		return priority != null ? priority.getName() : null;
	}

	<RULE> String removeProperty(ICSSDocument<RULE> styles, RULE rule, String propertyName) {
		final int idx = getPropertyIdx(propertyName);
		
		final String existing;
		
		if (idx >= 0) {
			existing = values[idx].getValue(styles, rule);
			
			if (idx == numValues - 1) {
				values[numValues - 1] = null;
			}
			else {
				System.arraycopy(values, idx + 1, values, idx, numValues - idx - 1);
			}
			
			-- numValues;
		}
		else {
			existing = null;
		}

		return existing;
	}

	void setProperty(String propertyName, String value, String priority) {

		final CSStyle type = CSStyle.fromPropertyName(propertyName);
		
		final CSSPriority priorityEnum = IEnum.asEnum(CSSPriority.class, priority, false);
		
		final CSSValue cssValue = type != null
				? new StandardCSSValue(type, priorityEnum)
				: new CustomCSSValue(propertyName, value, priorityEnum);

		setProperty(cssValue);
	}

	private void setProperty(CSSValue cssValue) {
		if (values == null) {
			this.values = new CSSValue[20];

			this.values[0] = cssValue;
			this.numValues = 1;
		}
		else {
			final int idx = getPropertyIdx(cssValue.getName());

			if (idx >= 0) {
				values[idx] = cssValue;
			}
			else {
				// Add
				if (numValues == values.length) {
					this.values = Arrays.copyOf(values, values.length * 2);
				}

				this.values[numValues ++] = cssValue;
			}
		}
	}
	
	

	@Override
	void set(CSStyle style) {
		super.set(style);

		setProperty(new StandardCSSValue(style, null));
	}

	private static abstract class CSSValue {
		private CSSPriority priority;

		abstract boolean hasPropertyName(String propertyName);
		
		abstract CSStyle getType();
		
		abstract String getName();
		
		abstract <RULE> String getValue(ICSSDocument<RULE> styles, RULE rule);
		
		CSSValue(CSSPriority priority) {
			this.priority = priority;
		}

		final CSSPriority getPriority() {
			return priority;
		}

		final void setPriority(CSSPriority priority) {
			this.priority = priority;
		}
	}
	
	private class StandardCSSValue extends CSSValue {
		private final CSStyle type;
		
		StandardCSSValue(CSStyle type, CSSPriority priority) {
			super(priority);
			
			if (type == null) {
				throw new IllegalArgumentException("type == null");
			}

			this.type = type;
		}

		@Override
		boolean hasPropertyName(String propertyName) {
			return type.getName().equalsIgnoreCase(propertyName);
		}

		@Override
		CSStyle getType() {
			return type;
		}

		@Override
		String getName() {
			return type.getName();
		}

		@Override
		<RULE> String getValue(ICSSDocument<RULE> styles, RULE rule) {
			return CSSStyleBuilder.getCSSPropertyValue(styles, rule, type);
		}
	}
	
	private static class CustomCSSValue extends CSSValue {
		private final String name;
		private final String value;
		
		CustomCSSValue(String name, String value, CSSPriority priority) {
			super(priority);
			
			if (name == null) {
				throw new IllegalArgumentException("name == null");
			}

			if (value == null) {
				throw new IllegalArgumentException("name == null");
			}

			this.name = name;
			this.value = value;
		}

		@Override
		boolean hasPropertyName(String propertyName) {
			return name.equalsIgnoreCase(propertyName);
		}

		@Override
		CSStyle getType() {
			return null;
		}

		@Override
		String getName() {
			return name;
		}

		@Override
		<RULE> String getValue(ICSSDocument<RULE> styles, RULE rule) {
			return value;
		}
	}
}

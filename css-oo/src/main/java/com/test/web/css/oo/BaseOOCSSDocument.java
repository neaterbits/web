package com.test.web.css.oo;

import com.test.web.css.common.CSSGradientColorStop;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSClear;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFilter;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSGradientDirectionType;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.css.common.enums.CSSPriority;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.parse.css.CSSParserListener;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseOOCSSDocument 
		implements CSSParserListener<Void>, ICSSDocumentStyles<OOCSSRule> {

	private OOCSSRule curParseElement;

	/***************************************************** Access interface *****************************************************/ 
	
	static OOCSSStyles styles(OOCSSRule rule) {
		return ((OOCSSBaseStylesRule)rule).getStyles();
	}
	
	@Override
	public final boolean isSet(OOCSSRule ref, CSStyle style) {
		return styles(ref).hasStyle(style);
	}
	
	@Override
	public final int getLeft(OOCSSRule ref) {
		return styles(ref).getLeft();
	}

	@Override
	public final CSSUnit getLeftUnit(OOCSSRule ref) {
		return styles(ref).getLeftUnit();
	}

	@Override
	public final int getTop(OOCSSRule ref) {
		return styles(ref).getTop();
	}

	@Override
	public final CSSUnit getTopUnit(OOCSSRule ref) {
		return styles(ref).getTopUnit();
	}

	@Override
	public final int getWidth(OOCSSRule ref) {
		return styles(ref).getWidth();
	}

	@Override
	public final CSSUnit getWidthUnit(OOCSSRule ref) {
		return styles(ref).getWidthUnit();
	}

	@Override
	public final int getHeight(OOCSSRule ref) {
		return styles(ref).getHeight();
	}

	@Override
	public final CSSUnit getHeightUnit(OOCSSRule ref) {
		return styles(ref).getHeightUnit();
	}
	
	private <PARAM> void set(OOWrapping ref, ICSSJustify<PARAM> setter, PARAM param) {
		
		setter.set(param,
				ref.getTop(), ref.getTopUnit(), ref.getTopType(),
				ref.getRight(), ref.getRightUnit(), ref.getRightType(),
				ref.getBottom(), ref.getBottomUnit(), ref.getBottomType(),
				ref.getLeft(), ref.getLeftUnit(), ref.getLeftType());
		
	}

	@Override
	public final <PARAM> void getMargins(OOCSSRule ref, ICSSJustify<PARAM> setter, PARAM param) {
		set(styles(ref).getMargins(), setter, param);
	}

	@Override
	public final <PARAM> void getPadding(OOCSSRule ref, ICSSJustify<PARAM> setter, PARAM param) {
		set(styles(ref).getPadding(), setter, param);
	}

	@Override
	public final CSSDisplay getDisplay(OOCSSRule ref) {
		return styles(ref).getDisplay();
	}

	@Override
	public final CSSPosition getPosition(OOCSSRule ref) {
		return styles(ref).getPosition();
	}

	@Override
	public final CSSFloat getFloat(OOCSSRule ref) {
		return styles(ref).getFloat();
	}

	@Override
	public final CSSClear getClear(OOCSSRule ref) {
		return styles(ref).getClear();
	}

	@Override
	public final CSSTextAlign getTextAlign(OOCSSRule ref) {
		return styles(ref).getTextAlign();
	}

	@Override
	public final CSSOverflow getOverflow(OOCSSRule ref) {
		return styles(ref).getOverflow();
	}

	@Override
	public final CSSTextDecoration getTextDecoration(OOCSSRule ref) {
		return styles(ref).getTextDecoration();
	}

	@Override
	public final int getColorR(OOCSSRule ref) {
		return styles(ref).getColorR();
	}

	@Override
	public final int getColorG(OOCSSRule ref) {
		return styles(ref).getColorG();
	}

	@Override
	public final int getColorB(OOCSSRule ref) {
		return styles(ref).getColorB();
	}

	@Override
	public final int getColorA(OOCSSRule ref) {
		return styles(ref).getColorA();
	}
	
	@Override
	public final CSSForeground getColorType(OOCSSRule ref) {
		return styles(ref).getColorType();
	}
	
	@Override
	public final int getNumBgLayers(OOCSSRule ref) {
		return styles(ref).getNumBgLayers();
	}
	
	@Override
	public final boolean isBgSet(OOCSSRule ref, int bgLayer, CSStyle style) {
		return styles(ref).getBgLayer(bgLayer).hasStyle(style);
	}

	@Override
	public final String getBgImageURL(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getImageUrl();
	}

	@Override
	public final CSSBackgroundImage getBgImage(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getImage();
	}

	@Override
	public final CSSGradientDirectionType getGradientDirectionType(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getGradientDirectionType();
	}

	@Override
	public final int getGradientAngle(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getGradientAngle();
	}

	@Override
	public final CSSPositionComponent getGradientPos1(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getGradientPos1();
	}

	@Override
	public final CSSPositionComponent getGradientPos2(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getGradientPos2();
	}

	@Override
	public final CSSGradientColorStop[] getGradientColorStops(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getGradientColorStops();
	}

	@Override
	public final int getBgPositionLeft(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getPositionLeft();
	}

	@Override
	public final CSSUnit getBgPositionLeftUnit(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getPositionLeftUnit();
	}

	@Override
	public final int getBgPositionTop(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getPositionTop();
	}

	@Override
	public final CSSUnit getBgPositionTopUnit(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getPositionTopUnit();
	}

	@Override
	public final CSSBackgroundPosition getBgPosition(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getPosition();
	}

	@Override
	public final int getBgWidth(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getSizeWidth();
	}

	@Override
	public final CSSUnit getBgWidthUnit(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getSizeWidthUnit();
	}

	@Override
	public final int getBgHeight(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getSizeHeight();
	}

	@Override
	public final CSSUnit getBgHeightUnit(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getSizeHeightUnit();
	}

	@Override
	public final CSSBackgroundSize getBgSize(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getSize();
	}

	@Override
	public final CSSBackgroundRepeat getBgRepeat(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getRepeat();
	}

	@Override
	public final CSSBackgroundAttachment getBgAttachment(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getAttachment();
	}

	@Override
	public final CSSBackgroundOrigin getBgOrigin(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getOrigin();
	}

	@Override
	public final CSSBackgroundOrigin getBgClip(OOCSSRule ref, int bgLayer) {
		return styles(ref).getBgLayer(bgLayer).getClip();
	}

	/*
	@Override
	public int getBgColorR(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getBgColorR();
	}

	@Override
	public int getBgColorG(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getBgColorG();
	}

	@Override
	public int getBgColorB(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getBgColorB();
	}

	@Override
	public int getBgColorA(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getBgColorA();
	}

	@Override
	public CSSBackgroundColor getBgColorType(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getBgColorType();
	}
	*/

	@Override
	public final int getBgColorR(OOCSSRule ref) {
		return styles(ref).getBgColorR();
	}

	@Override
	public final int getBgColorG(OOCSSRule ref) {
		return styles(ref).getBgColorG();
	}

	@Override
	public final int getBgColorB(OOCSSRule ref) {
		return styles(ref).getBgColorB();
	}

	@Override
	public final int getBgColorA(OOCSSRule ref) {
		return styles(ref).getBgColorA();
	}

	@Override
	public final CSSBackgroundColor getBgColorType(OOCSSRule ref) {
		return styles(ref).getBgColorType();
	}

	@Override
	public final int getMinWidth(OOCSSRule ref) {
		return styles(ref).getMinWidth();
	}

	@Override
	public final CSSUnit getMinWidthUnit(OOCSSRule ref) {
		return styles(ref).getMinWidthUnit();
	}

	@Override
	public final CSSMin getMinWidthType(OOCSSRule ref) {
		return styles(ref).getMinWidthType();
	}

	@Override
	public final int getMinHeight(OOCSSRule ref) {
		return styles(ref).getMinHeight();
	}

	@Override
	public final CSSUnit getMinHeightUnit(OOCSSRule ref) {
		return styles(ref).getMinHeightUnit();
	}

	@Override
	public final CSSMin getMinHeightType(OOCSSRule ref) {
		return styles(ref).getMinHeightType();
	}

	@Override
	public final int getMaxWidth(OOCSSRule ref) {
		return styles(ref).getMaxWidth();
	}

	@Override
	public final CSSUnit getMaxWidthUnit(OOCSSRule ref) {
		return styles(ref).getMaxWidthUnit();
	}

	@Override
	public final CSSMax getMaxWidthType(OOCSSRule ref) {
		return styles(ref).getMaxWidthType();
	}

	@Override
	public final int getMaxHeight(OOCSSRule ref) {
		return styles(ref).getMaxHeight();
	}

	@Override
	public final CSSUnit getMaxHeightUnit(OOCSSRule ref) {
		return styles(ref).getMaxHeightUnit();
	}

	@Override
	public final CSSMax getMaxHeightType(OOCSSRule ref) {
		return styles(ref).getMaxHeightType();
	}
	
	@Override
	public final CSSFilter getFilter(OOCSSRule ref) {
		return styles(ref).getFilter();
	}

	@Override
	public final int getBlur(OOCSSRule ref) {
		return styles(ref).getBlur();
	}

	@Override
	public final int getBrightness(OOCSSRule ref) {
		return styles(ref).getBrightness();
	}

	@Override
	public final int getContrast(OOCSSRule ref) {
		return styles(ref).getContrast();
	}

	@Override
	public final int getGrayscale(OOCSSRule ref) {
		return styles(ref).getGrayscale();
	}
	@Override
	public final boolean hasDropShadow(OOCSSRule ref) {
		return styles(ref).hasDropShadow();
	}

	@Override
	public final int getDropShadowH(OOCSSRule ref) {
		return styles(ref).getDropShadowH();
	}

	@Override
	public final CSSUnit getDropShadowHUnit(OOCSSRule ref) {
		return styles(ref).getDropShadowHUnit();
	}

	@Override
	public final int getDropShadowV(OOCSSRule ref) {
		return styles(ref).getDropShadowV();
	}

	@Override
	public final CSSUnit getDropShadowVUnit(OOCSSRule ref) {
		return styles(ref).getDropShadowVUnit();
	}

	@Override
	public final int getDropShadowBlur(OOCSSRule ref) {
		return styles(ref).getDropShadowBlur();
	}

	@Override
	public final int getDropShadowSpread(OOCSSRule ref) {
		return styles(ref).getDropShadowSpread();
	}

	@Override
	public final int getDropShadowR(OOCSSRule ref) {
		return styles(ref).getDropShadowR();
	}

	@Override
	public final int getDropShadowG(OOCSSRule ref) {
		return styles(ref).getDropShadowG();
	}

	@Override
	public final int getDropShadowB(OOCSSRule ref) {
		return styles(ref).getDropShadowB();
	}

	@Override
	public final int getDropShadowA(OOCSSRule ref) {
		return stylesRef().getDropShadowA();
	}

	@Override
	public final CSSColor getDropShadowColor(OOCSSRule ref) {
		return styles(ref).getDropShadowColor();
	}

	@Override
	public final int getHueRotate(OOCSSRule ref) {
		return styles(ref).getHueRotate();
	}

	@Override
	public final int getInvert(OOCSSRule ref) {
		return styles(ref).getInvert();
	}

	@Override
	public final int getOpacity(OOCSSRule ref) {
		return styles(ref).getOpacity();
	}

	@Override
	public final int getSaturate(OOCSSRule ref) {
		return styles(ref).getSaturate();
	}

	@Override
	public final int getSepia(OOCSSRule ref) {
		return styles(ref).getSepia();
	}

	@Override
	public final String getFilterURL(OOCSSRule ref) {
		return styles(ref).getUrl();
	}

	/***************************************************** Parse listener *****************************************************/ 
	
	final OOCSSStyles stylesRef() {
		return ((OOCSSBaseStylesRule)curParseElement).getStyles();
	}
	
	final OOCSSRule rule() {
		return curParseElement;
	}

	@Override
	public int onStylePropertyStart(CSStyle property) {
		// return next index so that sets priority on tight instance
		return stylesRef().getLength();
	}

	@Override
	public void onStylePriority(int propertyIndex, CSSPriority priority) {
		stylesRef().setPriority(propertyIndex, priority);
	}


	@Override
	public void onLeft(Void context, int left, CSSUnit unit) {
		stylesRef().addLeft(left, unit);
	}

	@Override
	public void onTop(Void context, int top, CSSUnit unit) {
		stylesRef().addTop(top, unit);
	}

	@Override
	public void onWidth(Void context, int width, CSSUnit unit) {
		stylesRef().addWidth(width, unit);
	}

	@Override
	public void onHeight(Void context, int height, CSSUnit unit) {
		stylesRef().addHeight(height, unit);
	}

	
	@Override
	public void onMargin(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		stylesRef().setMargins(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onPadding(Void context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		stylesRef().setPadding(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}
	
	@Override
	public void onColor(Void context, int r, int g, int b, int a) {
		stylesRef().setColorRGB(r, g, b, a);
	}

	@Override
	public void onColor(Void context, CSSColor color) {
		stylesRef().setColorCSS(color);
	}
	
	@Override
	public void onColor(Void context, CSSForeground foreground) {
		stylesRef().setColorType(foreground);
	}
	
	@Override
	public void onBgImageURL(Void context, int bgLayer, String url) {
		stylesRef().getOrAddBgLayer(bgLayer).setImageURL(url);
	}

	@Override
	public void onBgImage(Void context, int bgLayer, CSSBackgroundImage image) {
		stylesRef().getOrAddBgLayer(bgLayer).setImage(image);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, int angle, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(angle, colorStops);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(pos1, pos2, colorStops);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(colorStops);
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, int left, CSSUnit leftUnit, int top, CSSUnit topUnit) {
		stylesRef().getOrAddBgLayer(bgLayer).setPosition(left, leftUnit, top, topUnit);
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, CSSBackgroundPosition position) {
		stylesRef().getOrAddBgLayer(bgLayer).setPosition(position);
	}

	@Override
	public void onBgSize(Void context, int bgLayer, int width, CSSUnit widthUnit, int height, CSSUnit heightUnit) {
		stylesRef().getOrAddBgLayer(bgLayer).setSize(width, widthUnit, height, heightUnit);
	}

	@Override
	public void onBgSize(Void context, int bgLayer, CSSBackgroundSize size) {
		stylesRef().getOrAddBgLayer(bgLayer).setSize(size);
	}

	@Override
	public void onBgRepeat(Void context, int bgLayer, CSSBackgroundRepeat repeat) {
		stylesRef().getOrAddBgLayer(bgLayer).setRepeat(repeat);
	}

	@Override
	public void onBgAttachment(Void context, int bgLayer, CSSBackgroundAttachment attachment) {
		stylesRef().getOrAddBgLayer(bgLayer).setAttachment(attachment);
	}

	@Override
	public void onBgOrigin(Void context, int bgLayer, CSSBackgroundOrigin origin) {
		stylesRef().getOrAddBgLayer(bgLayer).setOrigin(origin);
	}

	@Override
	public void onBgClip(Void context, int bgLayer, CSSBackgroundOrigin clip) {
		stylesRef().getOrAddBgLayer(bgLayer).setClip(clip);
	}

	@Override
	public void onBgColor(Void context, int r, int g, int b, int a) {
		stylesRef().setBgColorRGB(r, g, b, a);
	}

	@Override
	public void onBgColor(Void context, CSSColor color) {
		stylesRef().setBgColorCSS(color);
	}

	@Override
	public void onBgColor(Void context, CSSBackgroundColor background) {
		stylesRef().setBgColorType(background);
	}

	@Override
	public void onTextAlign(Void context, CSSTextAlign textAlign) {
		stylesRef().setTextAlign(textAlign);
		
	}
	@Override
	public void onDisplay(Void context, CSSDisplay display) {
		stylesRef().setDisplay(display);
	}

	@Override
	public void onPosition(Void context, CSSPosition position) {
		stylesRef().setPosition(position);
	}

	@Override
	public void onFloat(Void context, CSSFloat _float) {
		stylesRef().setFloat(_float);
	}

	@Override
	public void onClear(Void context, CSSClear clear) {
		stylesRef().setClear(clear);
	}

	@Override
	public void onOverflow(Void context, CSSOverflow overflow) {
		stylesRef().setOverflow(overflow);
	}

	@Override
	public void onTextDecoration(Void context, CSSTextDecoration textDecoration) {
		stylesRef().setTextDecoration(textDecoration);
	}

	@Override
	public void onMaxWidth(Void context, int width, CSSUnit unit, CSSMax type) {
		stylesRef().setMaxWidth(width, unit, type);
	}

	@Override
	public void onMaxHeight(Void context, int height, CSSUnit unit, CSSMax type) {
		stylesRef().setMaxHeight(height, unit, type);
	}

	@Override
	public void onMinWidth(Void context, int width, CSSUnit unit, CSSMin type) {
		stylesRef().setMinWidth(width, unit, type);
	}

	@Override
	public void onMinHeight(Void context, int height, CSSUnit unit, CSSMin type) {
		stylesRef().setMinHeight(height, unit, type);
	}
	
	@Override
	public void onFilter(Void context, CSSFilter filter) {
		stylesRef().setFilter(filter);
	}

	@Override
	public void onBlur(Void context, int blur) {
		stylesRef().setBlur(blur);
	}

	@Override
	public void onBrightness(Void context, int brightness) {
		stylesRef().setBrightness(brightness);
	}

	@Override
	public void onContrast(Void context, int contrast) {
		stylesRef().setContrast(contrast);
	}

	@Override
	public void onGrayscale(Void context, int grayscale) {
		stylesRef().setGrayscale(grayscale);
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, int dropShadowR, int dropShadowG,
			int dropShadowB, int dropShadowA) {

		stylesRef().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowR, dropShadowG, dropShadowB, dropShadowA);
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, CSSColor dropShadowColor) {

		stylesRef().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowColor);
	}

	@Override
	public void onHueRotate(Void context, int hueRotate) {
		stylesRef().setHueRotate(hueRotate);
	}

	@Override
	public void onInvert(Void context, int invert) {
		stylesRef().setInvert(invert);
	}

	@Override
	public void onOpacity(Void context, int opacity) {
		stylesRef().setOpacity(opacity);
	}

	@Override
	public void onSaturate(Void context, int saturate) {
		stylesRef().setSaturate(saturate);
	}

	@Override
	public void onSepia(Void context, int sepia) {
		stylesRef().setSepia(sepia);
	}

	@Override
	public void onUrl(Void context, String url) {
		stylesRef().setUrl(url);
	}

	@Override
	public void onFontSize(Void context, int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum) {
		stylesRef().setFontSize(fontSize, fontSizeUnit, fontSizeEnum);
	}
	
	@Override
	public void onFontWeight(Void context, int fontWeightNumber, CSSFontWeight fontWeightEnum) {
		stylesRef().setFontWeight(fontWeightNumber, fontWeightEnum);
	}

	@Override
	public short getZIndex(OOCSSRule ref) {
		return styles(ref).getZIndex();
	}

	@Override
	public String getFontFamily(OOCSSRule ref) {
		return styles(ref).getFontFamily();
	}

	@Override
	public String getFontName(OOCSSRule ref) {
		return styles(ref).getFontName();
	}

	@Override
	public int getFontSize(OOCSSRule ref) {
		return styles(ref).getFontSize();
	}
	
	@Override
	public CSSUnit getFontSizeUnit(OOCSSRule ref) {
		return styles(ref).getFontSizeUnit();
	}

	@Override
	public CSSFontSize getFontSizeEnum(OOCSSRule ref) {
		return styles(ref).getFontSizeEnum();
	}

	@Override
	public int getFontWeightNumber(OOCSSRule ref) {
		return styles(ref).getFontWeightNumber();
	}

	@Override
	public CSSFontWeight getFontWeightEnum(OOCSSRule ref) {
		return styles(ref).getFontWeightEnum();
	}

	public final OOCSSRule allocateCurParseElement(CSSRuleType ruleType) {
		
		final OOCSSRule rule;
		
		switch (ruleType) {
		case STYLE:
			rule = new OOCSSStylesRule();
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown rule type " + ruleType);
		}
		
		this.curParseElement = rule;
		
		return this.curParseElement;
	}

	public final void setCurParseElement(OOCSSRule element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}

		this.curParseElement = element;
	}
}

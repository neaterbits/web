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
import com.test.web.css.common.enums.CSSColorGamut;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSDisplayMode;
import com.test.web.css.common.enums.CSSFilter;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSGradientDirectionType;
import com.test.web.css.common.enums.CSSHover;
import com.test.web.css.common.enums.CSSInvertedColors;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSLightLevel;
import com.test.web.css.common.enums.CSSLogicalOperator;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMediaType;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOrientation;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSOverflowBlock;
import com.test.web.css.common.enums.CSSOverflowInline;
import com.test.web.css.common.enums.CSSPointer;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.css.common.enums.CSSPriority;
import com.test.web.css.common.enums.CSSRange;
import com.test.web.css.common.enums.CSSResolutionUnit;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSScan;
import com.test.web.css.common.enums.CSSScripting;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSSUpdate;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.css.CSSParserListener;
import com.test.web.types.Ratio;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseOOCSSDocument 
		implements CSSParserListener<OOCSSBase>, ICSSDocumentStyles<OOCSSRule> {

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
		// return next index so that sets priority on right instance
		return stylesRef().getLength();
	}

	@Override
	public void onStylePriority(int propertyIndex, CSSPriority priority) {
		stylesRef().setPriority(propertyIndex, priority);
	}


	@Override
	public void onLeft(OOCSSBase context, int left, CSSUnit unit) {
		stylesRef().addLeft(left, unit);
	}

	@Override
	public void onTop(OOCSSBase context, int top, CSSUnit unit) {
		stylesRef().addTop(top, unit);
	}

	@Override
	public void onWidth(OOCSSBase context, int width, CSSUnit unit) {
		stylesRef().addWidth(width, unit);
	}

	@Override
	public void onHeight(OOCSSBase context, int height, CSSUnit unit) {
		stylesRef().addHeight(height, unit);
	}

	
	@Override
	public void onMargin(OOCSSBase context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		stylesRef().setMargins(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}

	@Override
	public void onPadding(OOCSSBase context,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		stylesRef().setPadding(top, topUnit, topType, right, rightUnit, rightType, bottom, bottomUnit, bottomType, left, leftUnit, leftType);
	}
	
	@Override
	public void onColor(OOCSSBase context, int r, int g, int b, int a) {
		stylesRef().setColorRGB(r, g, b, a);
	}

	@Override
	public void onColor(OOCSSBase context, CSSColor color) {
		stylesRef().setColorCSS(color);
	}
	
	@Override
	public void onColor(OOCSSBase context, CSSForeground foreground) {
		stylesRef().setColorType(foreground);
	}
	
	@Override
	public void onBgImageURL(OOCSSBase context, int bgLayer, String url) {
		stylesRef().getOrAddBgLayer(bgLayer).setImageURL(url);
	}

	@Override
	public void onBgImage(OOCSSBase context, int bgLayer, CSSBackgroundImage image) {
		stylesRef().getOrAddBgLayer(bgLayer).setImage(image);
	}

	@Override
	public void onBgGradient(OOCSSBase context, int bgLayer, int angle, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(angle, colorStops);
	}

	@Override
	public void onBgGradient(OOCSSBase context, int bgLayer, CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(pos1, pos2, colorStops);
	}

	@Override
	public void onBgGradient(OOCSSBase context, int bgLayer, CSSGradientColorStop[] colorStops) {
		stylesRef().getOrAddBgLayer(bgLayer).setGradient(colorStops);
	}

	@Override
	public void onBgPosition(OOCSSBase context, int bgLayer, int left, CSSUnit leftUnit, int top, CSSUnit topUnit) {
		stylesRef().getOrAddBgLayer(bgLayer).setPosition(left, leftUnit, top, topUnit);
	}

	@Override
	public void onBgPosition(OOCSSBase context, int bgLayer, CSSBackgroundPosition position) {
		stylesRef().getOrAddBgLayer(bgLayer).setPosition(position);
	}

	@Override
	public void onBgSize(OOCSSBase context, int bgLayer, int width, CSSUnit widthUnit, int height, CSSUnit heightUnit) {
		stylesRef().getOrAddBgLayer(bgLayer).setSize(width, widthUnit, height, heightUnit);
	}

	@Override
	public void onBgSize(OOCSSBase context, int bgLayer, CSSBackgroundSize size) {
		stylesRef().getOrAddBgLayer(bgLayer).setSize(size);
	}

	@Override
	public void onBgRepeat(OOCSSBase context, int bgLayer, CSSBackgroundRepeat repeat) {
		stylesRef().getOrAddBgLayer(bgLayer).setRepeat(repeat);
	}

	@Override
	public void onBgAttachment(OOCSSBase context, int bgLayer, CSSBackgroundAttachment attachment) {
		stylesRef().getOrAddBgLayer(bgLayer).setAttachment(attachment);
	}

	@Override
	public void onBgOrigin(OOCSSBase context, int bgLayer, CSSBackgroundOrigin origin) {
		stylesRef().getOrAddBgLayer(bgLayer).setOrigin(origin);
	}

	@Override
	public void onBgClip(OOCSSBase context, int bgLayer, CSSBackgroundOrigin clip) {
		stylesRef().getOrAddBgLayer(bgLayer).setClip(clip);
	}

	@Override
	public void onBgColor(OOCSSBase context, int r, int g, int b, int a) {
		stylesRef().setBgColorRGB(r, g, b, a);
	}

	@Override
	public void onBgColor(OOCSSBase context, CSSColor color) {
		stylesRef().setBgColorCSS(color);
	}

	@Override
	public void onBgColor(OOCSSBase context, CSSBackgroundColor background) {
		stylesRef().setBgColorType(background);
	}

	@Override
	public void onTextAlign(OOCSSBase context, CSSTextAlign textAlign) {
		stylesRef().setTextAlign(textAlign);
		
	}
	@Override
	public void onDisplay(OOCSSBase context, CSSDisplay display) {
		stylesRef().setDisplay(display);
	}

	@Override
	public void onPosition(OOCSSBase context, CSSPosition position) {
		stylesRef().setPosition(position);
	}

	@Override
	public void onFloat(OOCSSBase context, CSSFloat _float) {
		stylesRef().setFloat(_float);
	}

	@Override
	public void onClear(OOCSSBase context, CSSClear clear) {
		stylesRef().setClear(clear);
	}

	@Override
	public void onOverflow(OOCSSBase context, CSSOverflow overflow) {
		stylesRef().setOverflow(overflow);
	}

	@Override
	public void onTextDecoration(OOCSSBase context, CSSTextDecoration textDecoration) {
		stylesRef().setTextDecoration(textDecoration);
	}

	@Override
	public void onMaxWidth(OOCSSBase context, int width, CSSUnit unit, CSSMax type) {
		stylesRef().setMaxWidth(width, unit, type);
	}

	@Override
	public void onMaxHeight(OOCSSBase context, int height, CSSUnit unit, CSSMax type) {
		stylesRef().setMaxHeight(height, unit, type);
	}

	@Override
	public void onMinWidth(OOCSSBase context, int width, CSSUnit unit, CSSMin type) {
		stylesRef().setMinWidth(width, unit, type);
	}

	@Override
	public void onMinHeight(OOCSSBase context, int height, CSSUnit unit, CSSMin type) {
		stylesRef().setMinHeight(height, unit, type);
	}
	
	@Override
	public void onFilter(OOCSSBase context, CSSFilter filter) {
		stylesRef().setFilter(filter);
	}

	@Override
	public void onBlur(OOCSSBase context, int blur) {
		stylesRef().setBlur(blur);
	}

	@Override
	public void onBrightness(OOCSSBase context, int brightness) {
		stylesRef().setBrightness(brightness);
	}

	@Override
	public void onContrast(OOCSSBase context, int contrast) {
		stylesRef().setContrast(contrast);
	}

	@Override
	public void onGrayscale(OOCSSBase context, int grayscale) {
		stylesRef().setGrayscale(grayscale);
	}

	@Override
	public void onDropShadow(OOCSSBase context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, int dropShadowR, int dropShadowG,
			int dropShadowB, int dropShadowA) {

		stylesRef().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowR, dropShadowG, dropShadowB, dropShadowA);
	}

	@Override
	public void onDropShadow(OOCSSBase context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, CSSColor dropShadowColor) {

		stylesRef().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowColor);
	}

	@Override
	public void onHueRotate(OOCSSBase context, int hueRotate) {
		stylesRef().setHueRotate(hueRotate);
	}

	@Override
	public void onInvert(OOCSSBase context, int invert) {
		stylesRef().setInvert(invert);
	}

	@Override
	public void onOpacity(OOCSSBase context, int opacity) {
		stylesRef().setOpacity(opacity);
	}

	@Override
	public void onSaturate(OOCSSBase context, int saturate) {
		stylesRef().setSaturate(saturate);
	}

	@Override
	public void onSepia(OOCSSBase context, int sepia) {
		stylesRef().setSepia(sepia);
	}

	@Override
	public void onUrl(OOCSSBase context, String url) {
		stylesRef().setUrl(url);
	}

	@Override
	public void onFontSize(OOCSSBase context, int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum) {
		stylesRef().setFontSize(fontSize, fontSizeUnit, fontSizeEnum);
	}
	
	@Override
	public void onFontWeight(OOCSSBase context, int fontWeightNumber, CSSFontWeight fontWeightEnum) {
		stylesRef().setFontWeight(fontWeightNumber, fontWeightEnum);
	}

	@Override
	public void onStylePropertyText(OOCSSBase context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final OOCSSBase onImportRuleStart(String file, String url) {
		final OOCSSImportRule importRule = (OOCSSImportRule)allocateCurParseElement(CSSRuleType.IMPORT);
		
		importRule.setImportFile(file);
		importRule.setImportUrl(url);
		
		return importRule;
	}

	@Override
	public void onImportRuleEnd(OOCSSBase context) {
		
	}

	@Override
	public OOCSSBase onMediaQueryStart(OOCSSBase context, boolean negated, CSSMediaType mediaType) {
		// Add to calling rule
		final OOCSSMediaQueryList mediaQueryList = ((OOCSSMediaQueryRule)context).getMediaQueryList();

		return mediaQueryList.addMediaQuery(negated, mediaType);
	}
	
	@Override
	public void onMediaFeaturesLogicalOperator(OOCSSBase context, int level, CSSLogicalOperator logicalOperator) {
		((OOCSSMediaQueryOptions)context).setLogicalOperator(logicalOperator);
	}

	@Override
	public void onMediaQueryEnd(OOCSSBase context) {
		
	}

	@Override
	public OOCSSBase onMediaFeaturesStart(OOCSSBase context, int level, boolean negated) {
		
		final OOCSSBase resultContext;
		
		if (level == 0) {
			// Ignore since already contains list
			resultContext = context;
		}
		else {
			// sublevel, handle for individual features
			final OOCSSMediaConditions conditions = new OOCSSMediaConditions(negated);
			((OOCSSMediaQueryOptions)context).addOption(conditions);
			
			resultContext = conditions;
		}
		
		return resultContext;
	}

	@Override
	public void onMediaFeaturesEnd(OOCSSBase context, int level) {
		
	}
	
	// (a : b) and ((c : d) or (e : f)))
	private OOCSSMediaFeature addFeature(OOCSSBase context) {
		final OOCSSMediaFeature feature = new OOCSSMediaFeature();
		
		((OOCSSMediaQueryOptions)context).addOption(feature);
		
		return feature;
	}

	@Override
	public void onMediaFeatureWidth(OOCSSBase context, int level, int value, CSSUnit unit, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);
		
		mediaFeature.setWidth(value, unit, range);
	}

	@Override
	public void onMediaFeatureHeight(OOCSSBase context, int level, int value, CSSUnit unit, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setHeight(value, unit, range);
	}

	@Override
	public void onMediaFeatureAspectRatio(OOCSSBase context, int level, Ratio value, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setAspectRatio(value, range);
	}

	@Override
	public void onMediaFeatureOrientation(OOCSSBase context, int level, CSSOrientation orientation) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setOrientation(orientation);
	}

	@Override
	public void onMediaFeatureResolution(OOCSSBase context, int level, int value, CSSResolutionUnit unit, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setResolution(value, unit, range);
	}

	@Override
	public void onMediaFeatureScan(OOCSSBase context, int level, CSSScan value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setScan(value);
	}

	@Override
	public void onMediaFeatureGrid(OOCSSBase context, int level, boolean value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setGrid(value);
	}

	@Override
	public void onMediaFeatureUpdate(OOCSSBase context, int level, CSSUpdate value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setUpdate(value);
	}

	@Override
	public void onMediaFeatureOverflowBlock(OOCSSBase context, int level, CSSOverflowBlock value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setOverflowBlock(value);
	}

	@Override
	public void onMediaFeatureOverflowInline(OOCSSBase context, int level, CSSOverflowInline value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setOverflowInline(value);
	}

	@Override
	public void onMediaFeatureColor(OOCSSBase context, int level, int value, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setColor(value, range);
	}

	@Override
	public void onMediaFeatureColorGamut(OOCSSBase context, int level, CSSColorGamut value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setColorGamut(value);
	}

	@Override
	public void onMediaFeatureColorIndex(OOCSSBase context, int level, int value, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setColorIndex(value, range);
	}

	@Override
	public void onMediaFeatureDisplayMode(OOCSSBase context, int level, CSSDisplayMode value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setDisplayMode(value);
	}

	@Override
	public void onMediaFeatureMonochrome(OOCSSBase context, int level, int value, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setMonochrome(value, range);
	}

	@Override
	public void onMediaFeatureInvertedColors(OOCSSBase context, int level, CSSInvertedColors value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setInvertedColors(value);
	}

	@Override
	public void onMediaFeaturePointer(OOCSSBase context, int level, CSSPointer value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setPointer(value);
	}

	@Override
	public void onMediaFeatureHover(OOCSSBase context, int level, CSSHover value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setHover(value);
	}

	@Override
	public void onMediaFeatureAnyPointer(OOCSSBase context, int level, CSSPointer value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setAnyPointer(value);
	}

	@Override
	public void onMediaFeatureAnyHover(OOCSSBase context, int level, CSSHover value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setAnyHover(value);
	}

	@Override
	public void onMediaFeatureLightLevel(OOCSSBase context, int level, CSSLightLevel value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setLightLevel(value);
	}

	@Override
	public void onMediaFeatureScripting(OOCSSBase context, int level, CSSScripting value) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setScripting(value);
	}

	@Override
	public void onMediaFeatureDeviceWidth(OOCSSBase context, int level, int value, CSSUnit unit, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setDeviceWidth(value, unit, range);
	}

	@Override
	public void onMediaFeatureDeviceHeight(OOCSSBase context, int level, int value, CSSUnit unit, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setDeviceHeight(value, unit, range);
	}

	@Override
	public void onMediaFeatureDeviceAspectRatio(OOCSSBase context, int level, Ratio value, CSSRange range) {
		final OOCSSMediaFeature mediaFeature = addFeature(context);

		mediaFeature.setDeviceAspectRatio(value, range);
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
		
		case IMPORT:
			rule = new OOCSSImportRule();
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

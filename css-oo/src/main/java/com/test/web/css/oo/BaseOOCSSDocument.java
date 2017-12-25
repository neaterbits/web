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
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.parse.css.CSSParserListener;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseOOCSSDocument 
		implements CSSParserListener<Void>, ICSSDocumentStyles<OOCSSElement> {

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
	public CSSClear getClear(OOCSSElement ref) {
		return ref.getClear();
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
	public int getNumBgLayers(OOCSSElement ref) {
		return ref.getNumBgLayers();
	}
	
	@Override
	public boolean isBgSet(OOCSSElement ref, int bgLayer, CSStyle style) {
		return ref.getBgLayer(bgLayer).hasStyle(style);
	}

	@Override
	public String getBgImageURL(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getImageUrl();
	}

	@Override
	public CSSBackgroundImage getBgImage(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getImage();
	}

	@Override
	public CSSGradientDirectionType getGradientDirectionType(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getGradientDirectionType();
	}

	@Override
	public int getGradientAngle(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getGradientAngle();
	}

	@Override
	public CSSPositionComponent getGradientPos1(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getGradientPos1();
	}

	@Override
	public CSSPositionComponent getGradientPos2(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getGradientPos2();
	}

	@Override
	public CSSGradientColorStop[] getGradientColorStops(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getGradientColorStops();
	}

	@Override
	public int getBgPositionLeft(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getPositionLeft();
	}

	@Override
	public CSSUnit getBgPositionLeftUnit(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getPositionLeftUnit();
	}

	@Override
	public int getBgPositionTop(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getPositionTop();
	}

	@Override
	public CSSUnit getBgPositionTopUnit(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getPositionTopUnit();
	}

	@Override
	public CSSBackgroundPosition getBgPosition(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getPosition();
	}

	@Override
	public int getBgWidth(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getSizeWidth();
	}

	@Override
	public CSSUnit getBgWidthUnit(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getSizeWidthUnit();
	}

	@Override
	public int getBgHeight(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getSizeHeight();
	}

	@Override
	public CSSUnit getBgHeightUnit(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getSizeHeightUnit();
	}

	@Override
	public CSSBackgroundSize getBgSize(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getSize();
	}

	@Override
	public CSSBackgroundRepeat getBgRepeat(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getRepeat();
	}

	@Override
	public CSSBackgroundAttachment getBgAttachment(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getAttachment();
	}

	@Override
	public CSSBackgroundOrigin getBgOrigin(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getOrigin();
	}

	@Override
	public CSSBackgroundOrigin getBgClip(OOCSSElement ref, int bgLayer) {
		return ref.getBgLayer(bgLayer).getClip();
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
	public CSSBackgroundColor getBgColorType(OOCSSElement ref) {
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
	
	@Override
	public CSSFilter getFilter(OOCSSElement ref) {
		return ref.getFilter();
	}

	@Override
	public int getBlur(OOCSSElement ref) {
		return ref.getBlur();
	}

	@Override
	public int getBrightness(OOCSSElement ref) {
		return ref.getBrightness();
	}

	@Override
	public int getContrast(OOCSSElement ref) {
		return ref.getContrast();
	}

	@Override
	public int getGrayscale(OOCSSElement ref) {
		return ref.getGrayscale();
	}
	@Override
	public boolean hasDropShadow(OOCSSElement ref) {
		return ref.hasDropShadow();
	}

	@Override
	public int getDropShadowH(OOCSSElement ref) {
		return ref.getDropShadowH();
	}

	@Override
	public CSSUnit getDropShadowHUnit(OOCSSElement ref) {
		return ref.getDropShadowHUnit();
	}

	@Override
	public int getDropShadowV(OOCSSElement ref) {
		return ref.getDropShadowV();
	}

	@Override
	public CSSUnit getDropShadowVUnit(OOCSSElement ref) {
		return ref.getDropShadowVUnit();
	}

	@Override
	public int getDropShadowBlur(OOCSSElement ref) {
		return ref.getDropShadowBlur();
	}

	@Override
	public int getDropShadowSpread(OOCSSElement ref) {
		return ref.getDropShadowSpread();
	}

	@Override
	public int getDropShadowR(OOCSSElement ref) {
		return ref.getDropShadowR();
	}

	@Override
	public int getDropShadowG(OOCSSElement ref) {
		return ref.getDropShadowG();
	}

	@Override
	public int getDropShadowB(OOCSSElement ref) {
		return ref.getDropShadowB();
	}

	@Override
	public int getDropShadowA(OOCSSElement ref) {
		return ref().getDropShadowA();
	}

	@Override
	public CSSColor getDropShadowColor(OOCSSElement ref) {
		return ref.getDropShadowColor();
	}

	@Override
	public int getHueRotate(OOCSSElement ref) {
		return ref.getHueRotate();
	}

	@Override
	public int getInvert(OOCSSElement ref) {
		return ref.getInvert();
	}

	@Override
	public int getOpacity(OOCSSElement ref) {
		return ref.getOpacity();
	}

	@Override
	public int getSaturate(OOCSSElement ref) {
		return ref.getSaturate();
	}

	@Override
	public int getSepia(OOCSSElement ref) {
		return ref.getSepia();
	}

	@Override
	public String getFilterURL(OOCSSElement ref) {
		return ref().getUrl();
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
	public void onBgImageURL(Void context, int bgLayer, String url) {
		ref().getOrAddBgLayer(bgLayer).setImageURL(url);
	}

	@Override
	public void onBgImage(Void context, int bgLayer, CSSBackgroundImage image) {
		ref().getOrAddBgLayer(bgLayer).setImage(image);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, int angle, CSSGradientColorStop[] colorStops) {
		ref().getOrAddBgLayer(bgLayer).setGradient(angle, colorStops);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop[] colorStops) {
		ref().getOrAddBgLayer(bgLayer).setGradient(pos1, pos2, colorStops);
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSGradientColorStop[] colorStops) {
		ref().getOrAddBgLayer(bgLayer).setGradient(colorStops);
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, int left, CSSUnit leftUnit, int top, CSSUnit topUnit) {
		ref().getOrAddBgLayer(bgLayer).setPosition(left, leftUnit, top, topUnit);
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, CSSBackgroundPosition position) {
		ref().getOrAddBgLayer(bgLayer).setPosition(position);
	}

	@Override
	public void onBgSize(Void context, int bgLayer, int width, CSSUnit widthUnit, int height, CSSUnit heightUnit) {
		ref().getOrAddBgLayer(bgLayer).setSize(width, widthUnit, height, heightUnit);
	}

	@Override
	public void onBgSize(Void context, int bgLayer, CSSBackgroundSize size) {
		ref().getOrAddBgLayer(bgLayer).setSize(size);
	}

	@Override
	public void onBgRepeat(Void context, int bgLayer, CSSBackgroundRepeat repeat) {
		ref().getOrAddBgLayer(bgLayer).setRepeat(repeat);
	}

	@Override
	public void onBgAttachment(Void context, int bgLayer, CSSBackgroundAttachment attachment) {
		ref().getOrAddBgLayer(bgLayer).setAttachment(attachment);
	}

	@Override
	public void onBgOrigin(Void context, int bgLayer, CSSBackgroundOrigin origin) {
		ref().getOrAddBgLayer(bgLayer).setOrigin(origin);
	}

	@Override
	public void onBgClip(Void context, int bgLayer, CSSBackgroundOrigin clip) {
		ref().getOrAddBgLayer(bgLayer).setClip(clip);
	}

	@Override
	public void onBgColor(Void context, int r, int g, int b, int a) {
		ref().setBgColorRGB(r, g, b, a);
	}

	@Override
	public void onBgColor(Void context, CSSColor color) {
		ref().setBgColorCSS(color);
	}

	@Override
	public void onBgColor(Void context, CSSBackgroundColor background) {
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
	public void onClear(Void context, CSSClear clear) {
		ref().setClear(clear);
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
	public void onFilter(Void context, CSSFilter filter) {
		ref().setFilter(filter);
	}

	@Override
	public void onBlur(Void context, int blur) {
		ref().setBlur(blur);
	}

	@Override
	public void onBrightness(Void context, int brightness) {
		ref().setBrightness(brightness);
	}

	@Override
	public void onContrast(Void context, int contrast) {
		ref().setContrast(contrast);
	}

	@Override
	public void onGrayscale(Void context, int grayscale) {
		ref().setGrayscale(grayscale);
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, int dropShadowR, int dropShadowG,
			int dropShadowB, int dropShadowA) {

		ref().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowR, dropShadowG, dropShadowB, dropShadowA);
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, CSSColor dropShadowColor) {

		ref().setDropShadow(dropShadowH, dropShadowHUnit, dropShadowV, dropShadowVUnit, dropShadowBlur, dropShadowSpread, dropShadowColor);
	}

	@Override
	public void onHueRotate(Void context, int hueRotate) {
		ref().setHueRotate(hueRotate);
	}

	@Override
	public void onInvert(Void context, int invert) {
		ref().setInvert(invert);
	}

	@Override
	public void onOpacity(Void context, int opacity) {
		ref().setOpacity(opacity);
	}

	@Override
	public void onSaturate(Void context, int saturate) {
		ref().setSaturate(saturate);
	}

	@Override
	public void onSepia(Void context, int sepia) {
		ref().setSepia(sepia);
	}

	@Override
	public void onUrl(Void context, String url) {
		ref().setUrl(url);
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

	public final OOCSSElement allocateCurParseElement() {
		this.curParseElement = new OOCSSElement();
		
		return this.curParseElement;
	}

	public final void setCurParseElement(OOCSSElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}

		this.curParseElement = element;
	}
}

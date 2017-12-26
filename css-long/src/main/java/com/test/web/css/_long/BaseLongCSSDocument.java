package com.test.web.css._long;

import com.test.web.buffers.LongBuffersIntegerIndex;
import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
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
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.parse.css.CSSParserListener;

// Base class for long-encoded CSS documents
// This class is inherited from for both CSS documents and for CSS inline styles

public abstract class BaseLongCSSDocument 
		extends LongBuffersIntegerIndex
		implements CSSParserListener<Void>, ICSSDocumentStyles<Integer> {

	private DuplicateDetectingStringStorageBuffer fontBuffer;

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
	public CSSClear getClear(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSTextAlign getTextAlign(Integer ref) {
		return LongCSS.getTextAlign(buf(ref), offset(ref));
	}

	@Override
	public CSSOverflow getOverflow(Integer ref) {
		return LongCSS.getOverflow(buf(ref), offset(ref));
	}
	
	@Override
	public CSSTextDecoration getTextDecoration(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColorR(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColorG(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColorB(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColorA(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public CSSForeground getColorType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumBgLayers(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isBgSet(Integer ref, int bgLayer, CSStyle style) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBgImageURL(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundImage getBgImage(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSGradientDirectionType getGradientDirectionType(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGradientAngle(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSPositionComponent getGradientPos1(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSPositionComponent getGradientPos2(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSGradientColorStop[] getGradientColorStops(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBgPositionLeft(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getBgPositionLeftUnit(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBgPositionTop(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getBgPositionTopUnit(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundPosition getBgPosition(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBgWidth(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getBgWidthUnit(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBgHeight(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getBgHeightUnit(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundSize getBgSize(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundRepeat getBgRepeat(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundAttachment getBgAttachment(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundOrigin getBgOrigin(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSBackgroundOrigin getBgClip(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public int getBgColorR(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorG(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorB(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorA(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSBackgroundColor getBgColorType(Integer ref, int bgLayer) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	@Override
	public int getBgColorR(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorG(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorB(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBgColorA(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSBackgroundColor getBgColorType(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	/***************************************************** Parse listener *****************************************************/ 
	

	@Override
	public void onLeft(Void context, int left, CSSUnit unit, CSSPriority priority) {
		LongCSS.addLeft(buf(curParseElement), offset(curParseElement), left, unit);
	}

	@Override
	public void onTop(Void context, int top, CSSUnit unit, CSSPriority priority) {
		LongCSS.addTop(buf(curParseElement), offset(curParseElement), top, unit);
	}

	@Override
	public void onWidth(Void context, int width, CSSUnit unit, CSSPriority priority) {
		LongCSS.addWidth(buf(curParseElement), offset(curParseElement), width, unit);
	}

	@Override
	public void onHeight(Void context, int height, CSSUnit unit, CSSPriority priority) {
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
	public void onColor(Void context, int r, int g, int b, int a, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onColor(Void context, CSSColor colo, CSSPriority priorityr) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onColor(Void context, CSSForeground foreground, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBgImageURL(Void context, int bgLayer, String url) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBgImage(Void context, int bgLayer, CSSBackgroundImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, int angle, CSSGradientColorStop[] colorStops) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSPositionComponent pos1, CSSPositionComponent pos2,
			CSSGradientColorStop[] colorStops) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgGradient(Void context, int bgLayer, CSSGradientColorStop[] colorStops) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, int left, CSSUnit leftUnit, int top, CSSUnit topUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgPosition(Void context, int bgLayer, CSSBackgroundPosition position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgSize(Void context, int bgLayer, int width, CSSUnit widthUnit, int height, CSSUnit heightUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgSize(Void context, int bgLayer, CSSBackgroundSize size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgRepeat(Void context, int bgLayer, CSSBackgroundRepeat repeat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgAttachment(Void context, int bgLayer, CSSBackgroundAttachment attachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgOrigin(Void context, int bgLayer, CSSBackgroundOrigin origin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgClip(Void context, int bgLayer, CSSBackgroundOrigin clip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgColor(Void context, int r, int g, int b, int a, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBgColor(Void context, CSSColor color, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBgColor(Void context, CSSBackgroundColor background, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextAlign(Void context, CSSTextAlign textAlign, CSSPriority priority) {
		LongCSS.setTextAlign(buf(curParseElement), offset(curParseElement), textAlign);
		
	}
	@Override
	public void onDisplay(Void context, CSSDisplay display, CSSPriority priority) {
		LongCSS.setDisplay(buf(curParseElement), offset(curParseElement), display);
	}

	@Override
	public void onPosition(Void context, CSSPosition position, CSSPriority priority) {
		LongCSS.setPosition(buf(curParseElement), offset(curParseElement), position);
	}

	@Override
	public void onFloat(Void context, CSSFloat _float, CSSPriority priority) {
		LongCSS.setFloat(buf(curParseElement), offset(curParseElement), _float);
	}

	@Override
	public void onClear(Void context, CSSClear clear, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOverflow(Void context, CSSOverflow overflow, CSSPriority priority) {
		LongCSS.setOverflow(buf(curParseElement), offset(curParseElement), overflow);
	}
	
	@Override
	public void onTextDecoration(Void context, CSSTextDecoration textDecoration, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMaxWidth(Void context, int width, CSSUnit unit, CSSMax type, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMaxHeight(Void context, int height, CSSUnit unit, CSSMax type, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinWidth(Void context, int width, CSSUnit unit, CSSMin type, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinHeight(Void context, int height, CSSUnit unit, CSSMin type, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFilter(Void context, CSSFilter filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlur(Void context, int blur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBrightness(Void context, int brightness) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContrast(Void context, int contrast) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGrayscale(Void context, int grayscale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, int dropShadowR, int dropShadowG,
			int dropShadowB, int dropShadowA) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDropShadow(Void context, int dropShadowH, CSSUnit dropShadowHUnit, int dropShadowV,
			CSSUnit dropShadowVUnit, int dropShadowBlur, int dropShadowSpread, CSSColor dropShadowColor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHueRotate(Void context, int hueRotate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvert(Void context, int invert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpacity(Void context, int opacity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaturate(Void context, int saturate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSepia(Void context, int sepia) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUrl(Void context, String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFontSize(Void context, int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum, CSSPriority priority) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onFontWeight(Void context, int fontWeightNumber, CSSFontWeight fontWeightEnum, CSSPriority priority) {
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
	public CSSUnit getFontSizeUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public int getFontWeightNumber(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSFontWeight getFontWeightEnum(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSFontSize getFontSizeEnum(Integer ref) {
		// TODO Auto-generated method stub
		return null;
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
	
	@Override
	public CSSFilter getFilter(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBlur(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBrightness(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getContrast(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGrayscale(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasDropShadow(Integer ref) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDropShadowH(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getDropShadowHUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDropShadowV(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSUnit getDropShadowVUnit(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDropShadowBlur(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDropShadowSpread(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDropShadowR(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDropShadowG(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDropShadowB(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDropShadowA(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSColor getDropShadowColor(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHueRotate(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInvert(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOpacity(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSaturate(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSepia(Integer ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilterURL(Integer ref) {
		// TODO Auto-generated method stub
		return null;
	}

	protected final int allocateCurParseElement() {
		this.curParseElement = allocate(LongCSS.CSS_ENTITY_COMPACT, "style element");
		
		return this.curParseElement;
	}
}

package com.test.web.document.oo;

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
import com.test.web.css.oo.OOCSSElement;
import com.test.web.types.ColorRGB;
import com.test.web.types.DecimalSize;

final class OOHTMLStyleDocumentWrapper implements ICSSDocumentStyles<OOTagElement>{

	private final OOStyleDocument delegate = new OOStyleDocument();

	public boolean isSet(OOTagElement ref, CSStyle style) {

		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? false
				: delegate.isSet(styleElement, style);
	}

	public int getLeft(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? 0
				: delegate.getLeft(styleElement);
	}

	public CSSUnit getLeftUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getLeftUnit(styleElement);
	}

	public int getTop(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? 0
				: delegate.getTop(styleElement);
	}

	public CSSUnit getTopUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getTopUnit(styleElement);
	}

	public int getWidth(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? 0
				: delegate.getWidth(styleElement);
	}

	public CSSUnit getWidthUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getWidthUnit(styleElement);
	}

	public int getHeight(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? 0
				: delegate.getHeight(styleElement);
	}

	public CSSUnit getHeightUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getHeightUnit(styleElement);
	}

	public <PARAM> void getMargins(OOTagElement ref, ICSSJustify<PARAM> setter, PARAM param) {
		final OOCSSElement styleElement = ref.getStyleElement();

		if (styleElement != null) {
			delegate.getMargins(styleElement, setter, param);
		}
	}

	public <PARAM> void getPadding(OOTagElement ref, ICSSJustify<PARAM> setter, PARAM param) {
		final OOCSSElement styleElement = ref.getStyleElement();

		if (styleElement != null) {
			delegate.getPadding(styleElement, setter, param);
		}
	}

	public CSSDisplay getDisplay(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getDisplay(ref.getStyleElement());
	}

	public CSSPosition getPosition(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getPosition(ref.getStyleElement());
	}

	public CSSFloat getFloat(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getFloat(ref.getStyleElement());
	}
	
	@Override
	public CSSClear getClear(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getClear(ref.getStyleElement());
	}

	public CSSTextAlign getTextAlign(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getTextAlign(ref.getStyleElement());
	}

	public CSSOverflow getOverflow(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getOverflow(ref.getStyleElement());
	}
	
	@Override
	public CSSTextDecoration getTextDecoration(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getTextDecoration(ref.getStyleElement());
	}

	@Override
	public int getColorR(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getColorR(ref.getStyleElement());
	}

	@Override
	public int getColorG(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getColorG(ref.getStyleElement());
	}

	@Override
	public int getColorB(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getColorB(ref.getStyleElement());
	}

	@Override
	public int getColorA(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getColorA(ref.getStyleElement());
	}
	
	@Override
	public CSSForeground getColorType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getColorType(ref.getStyleElement());
	}

	@Override
	public int getNumBgLayers(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? 0
				: delegate.getNumBgLayers(ref.getStyleElement());
	}

	@Override
	public boolean isBgSet(OOTagElement ref, int bgLayer, CSStyle style) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? false
				: delegate.isBgSet(ref.getStyleElement(), bgLayer, style);
	}

	@Override
	public String getBgImageURL(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgImageURL(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundImage getBgImage(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgImage(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgPositionLeft(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgPositionLeft(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSUnit getBgPositionLeftUnit(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgPositionLeftUnit(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgPositionTop(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgPositionTop(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSUnit getBgPositionTopUnit(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgPositionTopUnit(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundPosition getBgPosition(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgPosition(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgWidth(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgWidth(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSUnit getBgWidthUnit(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgWidthUnit(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgHeight(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgHeight(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSUnit getBgHeightUnit(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgHeightUnit(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundSize getBgSize(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgSize(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundRepeat getBgRepeat(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgRepeat(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundAttachment getBgAttachment(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgAttachment(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundOrigin getBgOrigin(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgOrigin(ref.getStyleElement(), bgLayer);
	}

	@Override
	public CSSBackgroundOrigin getBgClip(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgClip(ref.getStyleElement(), bgLayer);
	}

	/*
	@Override
	public int getBgColorR(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorR(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgColorG(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorG(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgColorB(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorB(ref.getStyleElement(), bgLayer);
	}

	@Override
	public int getBgColorA(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgColorA(ref.getStyleElement(), bgLayer);
	}
	
	@Override
	public CSSBackgroundColor getBgColorType(OOTagElement ref, int bgLayer) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgColorType(ref.getStyleElement(), bgLayer);
	}
	*/

	@Override
	public int getBgColorR(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorR(ref.getStyleElement());
	}

	@Override
	public int getBgColorG(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorG(ref.getStyleElement());
	}

	@Override
	public int getBgColorB(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? ColorRGB.NONE
				: delegate.getBgColorB(ref.getStyleElement());
	}

	@Override
	public int getBgColorA(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? DecimalSize.NONE
				: delegate.getBgColorA(ref.getStyleElement());
	}

	@Override
	public CSSBackgroundColor getBgColorType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getBgColorType(ref.getStyleElement());
	}

	@Override
	public short getZIndex(OOTagElement ref) {
		return delegate.getZIndex(ref.getStyleElement());
	}

	@Override
	public String getFontFamily(OOTagElement ref) {
		return delegate.getFontFamily(ref.getStyleElement());
	}

	@Override
	public String getFontName(OOTagElement ref) {
		return delegate.getFontName(ref.getStyleElement());
	}

	@Override
	public int getFontSize(OOTagElement ref) {
		return delegate.getFontSize(ref.getStyleElement());
	}
	
	@Override
	public CSSUnit getFontSizeUnit(OOTagElement ref) {
		return delegate.getFontSizeUnit(ref.getStyleElement());
	}

	@Override
	public CSSFontSize getFontSizeEnum(OOTagElement ref) {
		return delegate.getFontSizeEnum(ref.getStyleElement());
	}
	
	@Override
	public int getFontWeightNumber(OOTagElement ref) {
		return delegate.getFontWeightNumber(ref.getStyleElement());
	}

	@Override
	public CSSFontWeight getFontWeightEnum(OOTagElement ref) {
		return delegate.getFontWeightEnum(ref.getStyleElement());
	}

	@Override
	public int getMinWidth(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinWidth(ref.getStyleElement());
	}

	@Override
	public CSSUnit getMinWidthUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinWidthUnit(ref.getStyleElement());
	}

	@Override
	public CSSMin getMinWidthType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinWidthType(ref.getStyleElement());
	}

	@Override
	public int getMinHeight(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinHeight(ref.getStyleElement());
	}

	@Override
	public CSSUnit getMinHeightUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinHeightUnit(ref.getStyleElement());
	}

	@Override
	public CSSMin getMinHeightType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMinHeightType(ref.getStyleElement());
	}

	@Override
	public int getMaxWidth(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxWidth(ref.getStyleElement());
	}

	@Override
	public CSSUnit getMaxWidthUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxWidthUnit(ref.getStyleElement());
	}

	@Override
	public CSSMax getMaxWidthType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxWidthType(ref.getStyleElement());
	}

	@Override
	public int getMaxHeight(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxHeight(ref.getStyleElement());
	}

	@Override
	public CSSUnit getMaxHeightUnit(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxHeightUnit(ref.getStyleElement());
	}

	@Override
	public CSSMax getMaxHeightType(OOTagElement ref) {
		final OOCSSElement styleElement = ref.getStyleElement();
		
		return styleElement == null
				? null
				: delegate.getMaxHeightType(ref.getStyleElement());
	}

	public String toString() {
		return delegate.toString();
	}
}

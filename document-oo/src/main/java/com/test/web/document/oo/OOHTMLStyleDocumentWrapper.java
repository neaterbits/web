package com.test.web.document.oo;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.css.oo.OOCSSElement;

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

	public String toString() {
		return delegate.toString();
	}
}

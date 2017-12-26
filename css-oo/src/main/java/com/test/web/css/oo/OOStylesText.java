package com.test.web.css.oo;

abstract class OOStylesText extends OOStylesBase {

	private String originalCSSText;

	final String getOriginalCSSText() {
		return originalCSSText;
	}

	final void setOriginalCSSText(String originalCSSText) {
		// TODO parse
		this.originalCSSText = originalCSSText;
	}
}

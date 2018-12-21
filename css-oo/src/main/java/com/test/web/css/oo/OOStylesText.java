package com.test.web.css.oo;

abstract class OOStylesText extends OOStylesBase {

    OOStylesText() {

    }
    
    OOStylesText(OOStylesText toCopy) {
        super(toCopy);

        this.originalCSSText = toCopy.originalCSSText;
    }
    
	private String originalCSSText;

	final String getOriginalCSSText() {
		return originalCSSText;
	}

	final void setOriginalCSSText(String originalCSSText) {
		// TODO parse
		this.originalCSSText = originalCSSText;
	}
}

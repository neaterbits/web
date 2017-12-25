package com.test.web.css.oo;

abstract class OOStylesText extends OOStylesBase {

	private String cssText;

	final String getCSSText() {
		return cssText;
	}

	final void setCSSText(String cssText) {
		// TODO parse
		this.cssText = cssText;
	}
}

package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;

public final class CSSCharsetRule<RULE> extends CSSRule<RULE> {

	public CSSCharsetRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}

	public String getEncoding() {
		return getAccessStyleSheet().getCharsetEncoding(getRule());
	}

	public void setEncoding(String encoding) {
		getAccessStyleSheet().setCharsetEncoding(getRule(), encoding);
	}
}


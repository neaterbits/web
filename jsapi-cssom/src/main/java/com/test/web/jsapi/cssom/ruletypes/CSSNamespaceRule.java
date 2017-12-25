package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;

public final class CSSNamespaceRule<RULE> extends CSSRule<RULE> {

	public CSSNamespaceRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}

	public String getNamespaceURI() {
		return getAccessStyleSheet().getNamespaceURI(getRule());
	}
	
	public String getNamespacePrefix() {
		return getAccessStyleSheet().getNamespacePrefix(getRule());
	}
}

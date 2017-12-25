package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.CSSStyleDeclaration;

public final class CSSKeyframeRule<RULE> extends CSSRule<RULE> {

	public CSSKeyframeRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}

	public String getKeyText() {
		return getAccessStyleSheet().getKeyframeKeyText(getRule());
	}

	public void setKeyText(String keyText) {
		getAccessStyleSheet().setKeyframeKeyText(getRule(), keyText);
	}

	public CSSStyleDeclaration<RULE> getStyle() {
		return new CSSStyleDeclaration<>(getAccessStyleSheet(), getRule(), this);
	}
}

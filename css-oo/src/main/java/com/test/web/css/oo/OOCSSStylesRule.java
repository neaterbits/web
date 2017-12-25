package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSRuleType;

public final class OOCSSStylesRule extends OOCSSBaseStylesRule {

	private String selectorText;

	@Override
	CSSRuleType getRuleType() {
		return CSSRuleType.STYLE;
	}

	final String getSelectorText() {
		return selectorText;
	}

	final void setSelectorText(String selectorText) {
		this.selectorText = selectorText;
	}
}

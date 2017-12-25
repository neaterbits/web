package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSRuleType;

public final class OOCSSCharsetRule extends OOCSSRule {

	private String encoding;

	@Override
	CSSRuleType getRuleType() {
		return CSSRuleType.CHARSET;
	}

	String getEncoding() {
		return encoding;
	}

	void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}

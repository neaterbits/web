package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSRuleType;

public final class OOCSSCharsetRule extends OOCSSRule {

	private String encoding;
	
	private OOCSSCharsetRule(OOCSSCharsetRule toCopy) {
	    this.encoding = toCopy.encoding;
    }

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

    @Override
    public OOCSSRule makeCopy() {
        return new OOCSSCharsetRule(this);
    }
}

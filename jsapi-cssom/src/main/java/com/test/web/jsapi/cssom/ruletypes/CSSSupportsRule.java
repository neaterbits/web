package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.ICSSConditionRule;
import com.test.web.jsapi.cssom.ICSSRuleList;

public final class CSSSupportsRule<RULE> extends CSSRule<RULE> implements ICSSConditionRule<RULE> {

	private final ICSSRuleList<RULE> cssRules;

	public CSSSupportsRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);

		this.cssRules = new ICSSRuleList<RULE>() {

			@Override
			public int getLength() {
				return getAccessStyleSheet().getSupportsCSSRulesLength(getRule());
			}

			@Override
			public CSSRule<RULE> item(int index) {
				
				final RULE mediaRule = getAccessStyleSheet().getSupportsCSSRulesItem(getRule(), index);
				
				return mediaRule != null ? createRule(getAccessStyleSheet(), rule) : null;
			}
		};
	}

	@Override
	public ICSSRuleList<RULE> getCssRules() {
		return cssRules;
	}

	@Override
	public long insertRule(String rule, long index) {
		return getAccessStyleSheet().insertSupportsCSSRule(getRule(), rule, index);
	}

	@Override
	public void deleteRule(long index) {
		getAccessStyleSheet().deleteSupportsCSSRule(getRule(), index);
	}

	@Override
	public String getConditionText() {
		return getAccessStyleSheet().getSupportsConditionText(getRule());
	}

	@Override
	public void setConditionText(String conditionText) {
		getAccessStyleSheet().setSupportsConditionText(getRule(), conditionText);
	}
}

package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.ICSSRuleList;

public final class CSSKeyframesRule<RULE> extends CSSRule<RULE> {

	private final ICSSRuleList<RULE> cssRules;
	
	public CSSKeyframesRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);

		this.cssRules = new ICSSRuleList<RULE>() {

			@Override
			public int getLength() {
				return getAccessStyleSheet().getKeyframesCSSRulesLength(getRule());
			}

			@Override
			public CSSRule<RULE> item(int index) {
				final RULE keyframeRule = getAccessStyleSheet().getKeyframesCSSRulesItem(getRule(), index);
				
				return new CSSKeyframeRule<>(getAccessStyleSheet(), keyframeRule);
			}
		};
	}

	public ICSSRuleList<RULE> getCssRules() {
		return cssRules;
	}
	
	public String getName() {
		return getAccessStyleSheet().getKeyframesName(getRule());
	}
	
	public void setName(String name) {
		getAccessStyleSheet().setKeyframesName(getRule(), getName());
	}

	public void appendRule(String rule) {
		getAccessStyleSheet().appendKeyframesRule(getRule(), rule);
	}
	
	public void deleteRule(String key) {
		getAccessStyleSheet().deleteKeyframesRule(getRule(), key);
	}
	
	public CSSKeyframeRule<RULE> findRule(String key) {
		final RULE keyframeRule = getAccessStyleSheet().findKeyframesRule(getRule(), key);
		
		return keyframeRule != null ? new CSSKeyframeRule<>(getAccessStyleSheet(), keyframeRule) : null;
	}
}

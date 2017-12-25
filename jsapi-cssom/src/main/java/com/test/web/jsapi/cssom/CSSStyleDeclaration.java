package com.test.web.jsapi.cssom;

import com.test.web.css.common.ICSSStyleSheet;

public final class CSSStyleDeclaration<RULE> extends CSSAccess<RULE> {

	private final RULE rule;
	private final CSSRule<RULE> cssRule;

	public CSSStyleDeclaration(ICSSStyleSheet<RULE> styleSheet, RULE rule, CSSRule<RULE> cssRule) {
		super(styleSheet);

		if (rule == null) {
			throw new IllegalArgumentException("rule == null");
		}
		
		if (cssRule == null) {
			throw new IllegalArgumentException("cssRule == null");
		}

		this.rule = rule;
		this.cssRule = cssRule;
	}

	public String getCssText() {
		return getAccessStyleSheet().getStyleCSSText(rule);
	}
	
	public void setCssText(String cssText) {
		getAccessStyleSheet().setStyleCSSText(rule, cssText);
	}
	
	public int getLength() {
		return getAccessStyleSheet().getStyleLength(rule);
	}
	
	public CSSRule<RULE> getParentRule() {
		return cssRule;
	}
	
	public String getPropertyPriority(String propertyName) {
		return getAccessStyleSheet().getStylePropertyPriority(rule, propertyName);
	}
	
	public String getPropetryValue(String propertyName) {
		return getAccessStyleSheet().getStylePropertyValue(rule, propertyName);
	}
	
	public String item(int index) {
		return getAccessStyleSheet().getStyleItem(rule, index);
	}

	public String removeProperty(String propertyName) {
		return getAccessStyleSheet().removeStyleProperty(rule, propertyName);
	}
	
	public void setProperty(String propertyName, String value, String priority) {
		getAccessStyleSheet().setStyleProperty(rule, propertyName, value, priority);
	}
}

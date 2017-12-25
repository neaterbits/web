package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;

public final class CSSCounterStyleRule<RULE> extends CSSRule<RULE> {

	public CSSCounterStyleRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}

	public String getName() {
		return getAccessStyleSheet().getCounterStyleName(getRule());
	}

	public void setName(String name) {
		getAccessStyleSheet().setCounterStyleName(getRule(), name);
	}

	public String getSystem() {
		return getAccessStyleSheet().getCounterStyleName(getRule());
	}

	public void setSystem(String system) {
		getAccessStyleSheet().setCounterStyleSystem(getRule(), system);
	}

	public String getSymbols() {
		return getAccessStyleSheet().getCounterStyleSymbols(getRule());
	}

	public void setSymbols(String symbols) {
		getAccessStyleSheet().setCounterStyleSymbols(getRule(), symbols);
	}

	public String getAdditiveSymbols() {
		return getAccessStyleSheet().getCounterStyleAdditiveSymbols(getRule());
	}

	public void setAdditiveSymbols(String additiveSymbols) {
		getAccessStyleSheet().setCounterStyleAdditiveSymbols(getRule(), additiveSymbols);
	}

	public String getNegative() {
		return getAccessStyleSheet().getCounterStyleNegative(getRule());
	}

	public void setNegative(String negative) {
		getAccessStyleSheet().setCounterStyleNegative(getRule(), negative);
	}

	public String getPrefix() {
		return getAccessStyleSheet().getCounterStylePrefix(getRule());
	}

	public void setPrefix(String prefix) {
		getAccessStyleSheet().setCounterStylePrefix(getRule(), prefix);
	}

	public String getSuffix() {
		return getAccessStyleSheet().getCounterStyleSuffix(getRule());
	}

	public void setSuffix(String suffix) {
		getAccessStyleSheet().setCounterStyleSuffix(getRule(), suffix);
	}

	public String getRange() {
		return getAccessStyleSheet().getCounterStyleRange(getRule());
	}

	public void setRange(String range) {
		getAccessStyleSheet().setCounterStyleRange(getRule(), range);
	}

	public String getPad() {
		return getAccessStyleSheet().getCounterStylePad(getRule());
	}

	public void setPad(String pad) {
		getAccessStyleSheet().setCounterStylePad(getRule(), pad);
	}

	public String getSpeakAs() {
		return getAccessStyleSheet().getCounterStyleSpeakAs(getRule());
	}

	public void setSpeakAs(String speakAs) {
		getAccessStyleSheet().setCounterStyleSpeakAs(getRule(), speakAs);
	}

	public String getFallback() {
		return getAccessStyleSheet().getCounterStyleFallback(getRule());
	}

	public void setFallback() {
		getAccessStyleSheet().setCounterStyleFallback(getRule());
	}
}

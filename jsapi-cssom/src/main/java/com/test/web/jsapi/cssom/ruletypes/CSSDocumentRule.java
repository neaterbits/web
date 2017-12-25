package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;

public final class CSSDocumentRule<RULE> extends CSSRule<RULE> {

	public CSSDocumentRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}

	public String getUrl() {
		return getAccessStyleSheet().getDocumentUrl(getRule());
	}
	
	public void setUrl(String url) {
		getAccessStyleSheet().setDocumentUrl(getRule(), url);
	}
	
	public String getUrlPrefix() {
		return getAccessStyleSheet().getDocumentUrlPrefix(getRule());
	}
	
	public void setUrlPrefix(String prefix) {
		getAccessStyleSheet().setDocumentUrlPrefix(getRule(), prefix);
	}
	
	public String getDomain() {
		return getAccessStyleSheet().getDocumentDomain(getRule());
	}
	
	public void setDomain(String domain) {
		getAccessStyleSheet().setDocumentDomain(getRule(), domain);
	}
	
	public String getRegexp() {
		return getAccessStyleSheet().getDocumentRegexp(getRule());
	}
	
	public void setRegexp(String regexp) {
		getAccessStyleSheet().setDocumentRegexp(getRule(), regexp);
	}
}

package com.test.web.jsapi.cssom;

public interface ICSSGroupingRule<RULE> {

	ICSSRuleList<RULE> getCssRules();
	
	long insertRule(String rule, long index);
	
	void deleteRule(long index);
}

package com.test.web.jsapi.cssom;

public interface ICSSConditionRule<RULE> extends ICSSGroupingRule<RULE> {

	String getConditionText();
	
	void setConditionText(String conditionText);
	
}

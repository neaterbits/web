package com.test.web.css.common;

import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSStyle;

// Generic interface to stylesheet
public interface ICSSStyleSheet<RULE> {


	CSSRuleType getRuleType(RULE rule);
	
	// Style rules
	
	String getStyleSelectorText(RULE rule);
	
	void setStyleSelectorText(RULE rule, String selectorText);
	
	int getStyleLength(RULE rule);

	String getStyleCustomPropertyName(RULE rule, int index);

	String getStyleCustomPropertyValue(RULE rule, int index);
	
	CSStyle getStyleType(RULE rule, int index);
	
	CSSValueType getStylePropertyCSSValueType(RULE rule, String propertyName);

	String getStyleCSSText(RULE rule);

	void setStyleCSSText(RULE rule, String cssText);
	
	String getStylePropertyPriority(RULE rule, String propertyName);

	String getStylePropertyPriority(RULE rule, int index);

	String getStylePropertyValue(RULE rule, String propertyName);
	
	String getStyleItem(RULE rule, int index);
	
	String removeStyleProperty(RULE rule, String propertyName);

	void setStyleProperty(RULE rule, String propertyName, String value, String priority);

	// Charset rules
	String getCharsetEncoding(RULE rule);
	
	void setCharsetEncoding(RULE rule, String encoding);
	
	// Counter-style
	String getCounterStyleName(RULE rule);

	void setCounterStyleName(RULE rule, String name);

	String getCounterStyleSystem(RULE rule);

	void setCounterStyleSystem(RULE rule, String system);

	String getCounterStyleSymbols(RULE rule);

	void setCounterStyleSymbols(RULE rule, String symbols);

	String getCounterStyleAdditiveSymbols(RULE rule);

	void setCounterStyleAdditiveSymbols(RULE rule, String additiveSymbols);

	String getCounterStyleNegative(RULE rule);

	void setCounterStyleNegative(RULE rule, String negative);

	String getCounterStylePrefix(RULE rule);

	void setCounterStylePrefix(RULE rule, String prefix);

	String getCounterStyleSuffix(RULE rule);

	void setCounterStyleSuffix(RULE rule, String suffix);

	String getCounterStyleRange(RULE rule);

	void setCounterStyleRange(RULE rule, String range);

	String getCounterStylePad(RULE rule);

	void setCounterStylePad(RULE rule, String pad);

	String getCounterStyleSpeakAs(RULE rule);

	void setCounterStyleSpeakAs(RULE rule, String speakAs);

	String getCounterStyleFallback(RULE rule);

	void setCounterStyleFallback(RULE rule);


	// Document rules
	String getDocumentUrl(RULE rule);
	
	void setDocumentUrl(RULE rule, String url);
	
	String getDocumentUrlPrefix(RULE rule);
	
	void setDocumentUrlPrefix(RULE rule, String prefix);
	
	String getDocumentDomain(RULE rule);
	
	void setDocumentDomain(RULE rule, String domain);
	
	String getDocumentRegexp(RULE rule);
	
	void setDocumentRegexp(RULE rule, String regexp);

	// Font face rules utilizes API for styles
	
	// Import rules
	String getImportHref(RULE rule);
	
	void appendImportMedium(RULE rule, String newMedium);
	
	void deleteImportMedium(RULE rule, String oldMedium);
	
	int getImportMediaLength(RULE rule);
	
	String getImportMediaText(RULE rule);
	
	String getImportMediaItem(RULE rule, int index);

	void setImportMediaText(RULE rule, String mediaText);

	ICSSStyleSheet<RULE> getImportStyleSheet(RULE rule);

	// Keyframes rules
	int getKeyframesCSSRulesLength(RULE rule);
	
	RULE getKeyframesCSSRulesItem(RULE rule, int index);

	String getKeyframesName(RULE rule);

	void setKeyframesName(RULE rule, String name);

	void appendKeyframesRule(RULE rule, String keyframeRule);

	void deleteKeyframesRule(RULE rule, String key);

	RULE findKeyframesRule(RULE rule, String key);

	void insertKeyframesRule(RULE rule, String keyframeRule);

	// Keyframe rules
	String getKeyframeKeyText(RULE rule);
	
	void setKeyframeKeyText(RULE rule, String keyText);
	
	// ... style rules retrieved through style interface
	
	// Media rules 
	int getMediaCSSRulesLength(RULE rule);
	
	RULE getMediaCSSRulesItem(RULE rule, int index);

	void appendMediaMedium(RULE rule, String newMedium);
	
	void deleteMediaMedium(RULE rule, String oldMedium);
	
	int getMediaLength(RULE rule);
	
	String getMediaText(RULE rule);
	
	String getMediaItem(RULE rule, int index);

	void setMediaText(RULE rule, String mediaText);
	
	void deleteMediaCSSRule(RULE rule, long index);

	long insertMediaCSSRule(RULE rule, String mediaRule, long index);
	
	String getMediaConditionText(RULE rule);
	
	void setMediaConditionText(RULE rule, String conditionText);
	
	// Namespace rules
	String getNamespaceURI(RULE rule);
	
	String getNamespacePrefix(RULE rule);

	// Page rules
	String getPageSelectorText(RULE rule);
	
	void setPageSelectorText(RULE rule);
	
	// Supports rules
	int getSupportsCSSRulesLength(RULE rule);
	
	RULE getSupportsCSSRulesItem(RULE rule, int index);

	void deleteSupportsCSSRule(RULE rule, long index);

	long insertSupportsCSSRule(RULE rule, String supportsRule, long index);
	
	String getSupportsConditionText(RULE rule);
	
	void setSupportsConditionText(RULE rule, String conditionText);
	
	
}

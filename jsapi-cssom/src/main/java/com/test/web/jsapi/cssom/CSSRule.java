package com.test.web.jsapi.cssom;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.jsapi.cssom.ruletypes.CSSCharsetRule;
import com.test.web.jsapi.cssom.ruletypes.CSSCounterStyleRule;
import com.test.web.jsapi.cssom.ruletypes.CSSDocumentRule;
import com.test.web.jsapi.cssom.ruletypes.CSSFontFaceRule;
import com.test.web.jsapi.cssom.ruletypes.CSSFontFeatureValuesRule;
import com.test.web.jsapi.cssom.ruletypes.CSSImportRule;
import com.test.web.jsapi.cssom.ruletypes.CSSKeyframeRule;
import com.test.web.jsapi.cssom.ruletypes.CSSKeyframesRule;
import com.test.web.jsapi.cssom.ruletypes.CSSMediaRule;
import com.test.web.jsapi.cssom.ruletypes.CSSNamespaceRule;
import com.test.web.jsapi.cssom.ruletypes.CSSPageRule;
import com.test.web.jsapi.cssom.ruletypes.CSSRegionStyleRule;
import com.test.web.jsapi.cssom.ruletypes.CSSStyleRule;
import com.test.web.jsapi.cssom.ruletypes.CSSSupportsRule;
import com.test.web.jsapi.cssom.ruletypes.CSSUnknownRule;
import com.test.web.jsapi.cssom.ruletypes.CSSViewportRule;

public abstract class CSSRule<RULE> extends CSSAccess<RULE> {

	private final RULE rule;

	public CSSRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet);

		if (rule == null) {
			throw new IllegalArgumentException("rule == null");
		}

		this.rule = rule;
	}
	
	protected final RULE getRule() {
		return rule;
	}

	protected static <R> CSSRule<R> createRule(ICSSStyleSheet<R> styleSheet, R rule) {
		final CSSRule<R> cssRule;
		
		final CSSRuleType ruleType = styleSheet.getRuleType(rule);
		
		switch (ruleType) {
		
		case CHARSET:
			cssRule = new CSSCharsetRule<R>(styleSheet, rule);
			break;
			
		case COUNTER_STYLE:
			cssRule = new CSSCounterStyleRule<R>(styleSheet, rule);
			break;
			
		case DOCUMENT:
			cssRule = new CSSDocumentRule<R>(styleSheet, rule);
			break;
			
		case FONT_FACE:
			cssRule = new CSSFontFaceRule<R>(styleSheet, rule);
			break;
			
		case FONT_FEATURE_VALUES:
			cssRule = new CSSFontFeatureValuesRule<R>(styleSheet, rule);
			break;
			
		case IMPORT:
			cssRule = new CSSImportRule<R>(styleSheet, rule);
			break;
			
		case KEYFRAME:
			cssRule = new CSSKeyframeRule<R>(styleSheet, rule);
			break;
			
		case KEYFRAMES:
			cssRule = new CSSKeyframesRule<R>(styleSheet, rule);
			break;
			
		case MEDIA:
			cssRule = new CSSMediaRule<R>(styleSheet, rule);
			break;
			
		case NAMESPACE:
			cssRule = new CSSNamespaceRule<R>(styleSheet, rule);
			break;
		
		case PAGE:
			cssRule = new CSSPageRule<R>(styleSheet, rule);
			break;
			
		case REGION_STYLE:
			cssRule = new CSSRegionStyleRule<R>(styleSheet, rule);
			break;
			
		case SUPPORTS:
			cssRule = new CSSStyleRule<R>(styleSheet, rule);
			break;
			
		case STYLE:
			cssRule = new CSSSupportsRule<R>(styleSheet, rule);
			break;
			
		case UNKNOWN:
			cssRule = new CSSUnknownRule<R>(styleSheet, rule);
			break;
			
		case VIEWPORT:
			cssRule = new CSSViewportRule<R>(styleSheet, rule);
			break;

		default:
			throw new UnsupportedOperationException("Unknown rule type " + ruleType);
		}
		
		return cssRule;
	}
}

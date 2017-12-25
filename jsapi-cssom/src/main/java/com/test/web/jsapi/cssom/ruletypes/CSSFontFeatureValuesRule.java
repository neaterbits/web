package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;

// TODO not properly documented, only in Gecko
public final class CSSFontFeatureValuesRule<RULE> extends CSSRule<RULE> {

	public CSSFontFeatureValuesRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
	}
}

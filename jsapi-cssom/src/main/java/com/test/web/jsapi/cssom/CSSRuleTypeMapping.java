package com.test.web.jsapi.cssom;

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

public class CSSRuleTypeMapping {

	private static final CSSRuleTypeMapping [] RULE_TYPE_MAPPINGS = new CSSRuleTypeMapping [] {
	
		new CSSRuleTypeMapping(CSSRuleType.STYLE,				1, CSSStyleRule.class),
		new CSSRuleTypeMapping(CSSRuleType.IMPORT,			3, CSSImportRule.class),
		new CSSRuleTypeMapping(CSSRuleType.MEDIA, 			4, CSSMediaRule.class),
		new CSSRuleTypeMapping(CSSRuleType.FONT_FACE, 	5, CSSFontFaceRule.class),
		new CSSRuleTypeMapping(CSSRuleType.PAGE	,			6, CSSPageRule.class),
		new CSSRuleTypeMapping(CSSRuleType.KEYFRAMES,		7, CSSKeyframesRule.class),
		new CSSRuleTypeMapping(CSSRuleType.KEYFRAME,		8, CSSKeyframeRule.class),
		
		new CSSRuleTypeMapping(CSSRuleType.NAMESPACE,	10, CSSNamespaceRule.class),
		new CSSRuleTypeMapping(CSSRuleType.COUNTER_STYLE, 11, CSSCounterStyleRule.class),
		new CSSRuleTypeMapping(CSSRuleType.SUPPORTS,		12, CSSSupportsRule.class),
		new CSSRuleTypeMapping(CSSRuleType.DOCUMENT,		13, CSSDocumentRule.class),
		new CSSRuleTypeMapping(CSSRuleType.FONT_FEATURE_VALUES, 14, CSSFontFeatureValuesRule.class),
		new CSSRuleTypeMapping(CSSRuleType.VIEWPORT, 15, CSSViewportRule.class),
		new CSSRuleTypeMapping(CSSRuleType.REGION_STYLE, 16, CSSRegionStyleRule.class),
		new CSSRuleTypeMapping(CSSRuleType.UNKNOWN, 	0, CSSUnknownRule.class),
		new CSSRuleTypeMapping(CSSRuleType.CHARSET,		2, CSSCharsetRule.class)
	};

	
	private final CSSRuleType type;
	private final int typeConstant;
	private final Class<?> typeClass;

	private CSSRuleTypeMapping(CSSRuleType type, int typeConstant,
			Class<?> typeClass) {
		this.type = type;
		this.typeConstant = typeConstant;
		this.typeClass = typeClass;
	}
}

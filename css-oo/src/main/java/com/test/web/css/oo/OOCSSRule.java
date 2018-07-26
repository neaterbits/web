package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSRuleType;

public abstract class OOCSSRule extends OOCSSBase {
	
    OOCSSRule() {
        
    }
    
    OOCSSRule(OOCSSRule toCopy) {
        super(toCopy);
    }
    
	abstract CSSRuleType getRuleType();
	
	public abstract OOCSSRule makeCopy();
}


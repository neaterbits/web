package com.test.web.css.oo;

// Base class for any rule that has styles block
public abstract class OOCSSBaseStylesRule extends OOCSSRule {

	private final OOCSSStyles styles;
	
	OOCSSBaseStylesRule(OOCSSBaseStylesRule toCopy) {
	    super(toCopy);
	    
	    this.styles = toCopy.styles.makeCopy();
	}
	
	protected OOCSSBaseStylesRule() {
		this.styles = new OOCSSStyles();
	}
	
	final OOCSSStyles getStyles() {
		return styles;
	}
}

package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSRuleType;

final class OOCSSImportRule extends OOCSSRule implements OOCSSMediaQueryRule {

	private String importUrl;
	private String importFile;
	
	public OOCSSImportRule() {
		this.importUrl = null;
		this.importFile = null;
	}
	
	public OOCSSImportRule(String importUrl, String importFile) {
		this.importUrl = importUrl;
		this.importFile = importFile;
	}
	
	OOCSSImportRule(OOCSSImportRule toCopy) {
	    this.importUrl = toCopy.importUrl;
	    this.importFile = toCopy.importFile;
	}

	@Override
    public OOCSSRule makeCopy() {
        return new OOCSSImportRule(this);
    }


    private OOCSSMediaQueryList mediaQueryList;

	@Override
	CSSRuleType getRuleType() {
		return CSSRuleType.IMPORT;
	}
	
	String getImportUrl() {
		return importUrl;
	}
	
	void setImportUrl(String importUrl) {
		this.importUrl = importUrl;
	}

	String getImportFile() {
		return importFile;
	}
	
	void setImportFile(String importFile) {
		this.importFile = importFile;
	}

	@Override
	public OOCSSMediaQueryList getMediaQueryList() {

		if (mediaQueryList == null) {
			mediaQueryList = new OOCSSMediaQueryList();
		}

		return mediaQueryList;
	}
}

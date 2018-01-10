package com.test.web.css.common;

// Access to import rules
public interface CSSDocumentImports {

	String getImportUrl(int ruleIdx);
	
	String getImportFile(int ruleIdx);
	
}

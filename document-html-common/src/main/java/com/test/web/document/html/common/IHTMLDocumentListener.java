package com.test.web.document.html.common;

import com.test.web.css.common.ICSSDocumentStyles;

// For updating UI based on changes applied from JS
public interface IHTMLDocumentListener<ELEMENT> {

    // Style-attribute requires extra care since we have to get complex before and after state from parsed information 
    void onStyleAttributeUpdated(ELEMENT element, ICSSDocumentStyles<ELEMENT> beforeStyles, ICSSDocumentStyles<ELEMENT> afterStyles);
    
	void onAttributeUpdated(ELEMENT element, HTMLAttribute attribute, String beforeValue, String afterValue);
	
	void onStyleAttributeRemoved(ELEMENT element, ICSSDocumentStyles<ELEMENT> beforeStyles);

	void onAttributeRemoved(ELEMENT element, HTMLAttribute attribute, String beforeValue);
}

package com.test.web.jsapi.dom;

import com.test.web.jsapi.common.dom.IDocumentContext;

/**
 * Helper class that creates new HTML element from document
 */

public class HTMLJSElementFactory {
    
    public static <ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT,ATTRIBUTE>>
        Element<ELEMENT, ATTRIBUTE, DOCUMENT> makeElement(ELEMENT element, DOCUMENT document) {

        // TODO switch on element type
        return new Element<>(document, element);
    }

}

package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

/**
 * Helper class that creates new HTML element from document
 */

public class HTMLJSElementFactory {
    
    public static <
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT,ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>
    
        Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> makeElement(ELEMENT element, DOCUMENT_CONTEXT document) {

        // TODO switch on element type
        return new Element<>(document, element);
    }

}

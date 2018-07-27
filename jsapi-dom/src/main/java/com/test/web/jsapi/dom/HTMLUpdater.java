package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

/**
 * Helper class for retrieving and updating innerHTML and outerHTML of elements
 * by using the regular document access interfaces.
 * 
 */

final class HTMLUpdater {

    static <
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

        String getInnerHTML(ELEMENT element, DOCUMENT_CONTEXT document) {

        throw new UnsupportedOperationException();
    
    }

    
    static <
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

        void setInnerHTML(ELEMENT element, DOCUMENT_CONTEXT document, String html) {
        
        // Create elements in the model
        throw new UnsupportedOperationException();
        
    }

    static <
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>
    
        String getOuterHTML(ELEMENT element, DOCUMENT_CONTEXT document) {

        throw new UnsupportedOperationException();

    }


    static <
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

        void setOuterHTML(ELEMENT element, DOCUMENT_CONTEXT document, String html) {
    
        // Create elements in the model
        throw new UnsupportedOperationException();
    
    }

}

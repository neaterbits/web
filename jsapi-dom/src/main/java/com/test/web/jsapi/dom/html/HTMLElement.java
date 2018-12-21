package com.test.web.jsapi.dom.html;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.dom.Element;

public abstract class HTMLElement<
                ELEMENT,
                ATTRIBUTE,
                DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
                DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>
        
        extends Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {


    protected HTMLElement(DOCUMENT_CONTEXT document, ELEMENT element) {
        super(document, element);
    }
}

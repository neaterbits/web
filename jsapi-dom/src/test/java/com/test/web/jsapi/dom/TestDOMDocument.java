package com.test.web.jsapi.dom;

import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;

public class TestDOMDocument extends DOMDocument<OOTagElement, OOAttribute, OOHTMLDocument, DocumentContext<OOTagElement, OOAttribute, OOHTMLDocument>> {

    public TestDOMDocument() {
    }

    public TestDOMDocument(TestDocumentContext document, OOTagElement element) {
        super(document, element);
    }

    public TestDOMDocument(TestDocumentContext document) {
        super(document);
    }
}

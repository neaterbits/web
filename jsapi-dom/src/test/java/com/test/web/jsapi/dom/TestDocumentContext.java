package com.test.web.jsapi.dom;

import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.jsengine.common.JSInvocation;
import com.test.web.page.common.PageAccess;

public class TestDocumentContext extends DocumentContext<OOTagElement, OOAttribute, OOHTMLDocument> {

    public TestDocumentContext(OOHTMLDocument delegate,
            BrowserDefaultEventHandling<OOTagElement, OOAttribute, OOHTMLDocument> defaultEventHandling,
            PageAccess<OOTagElement> pageAccess, JSInvocation jsInvocation) {
        super(delegate, defaultEventHandling, pageAccess, jsInvocation);
    }
}

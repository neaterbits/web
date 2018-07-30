package com.test.web.document.html.test;

import com.test.web.parse.html.IDocumentParserListener;

import junit.framework.TestCase;

public abstract class BaseHTMLDocumentTest<
        ELEMENT,
        ATTRIBUTE,
        CSS_LISTENER_CONTEXT,
        DOCUMENT extends IDocumentParserListener<ELEMENT, ATTRIBUTE, CSS_LISTENER_CONTEXT, DOCUMENT>> extends TestCase {
	
}

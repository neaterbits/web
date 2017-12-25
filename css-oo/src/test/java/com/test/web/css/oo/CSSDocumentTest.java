package com.test.web.css.oo;

import com.test.web.css.test.BaseCSSDocumentTest;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class CSSDocumentTest extends BaseCSSDocumentTest<OOCSSElement> {

	@Override
	protected ICSSDocumentParserListener<OOCSSElement, Void> createDocument() {
		return new OOCSSDocument();
	}
}

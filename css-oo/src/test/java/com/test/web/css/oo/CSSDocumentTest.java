package com.test.web.css.oo;

import com.test.web.css.test.BaseCSSDocumentTest;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class CSSDocumentTest extends BaseCSSDocumentTest<OOCSSElement, OOTokenizer> {

	@Override
	protected ICSSDocumentParserListener<OOCSSElement, OOTokenizer, Void> createDocument() {
		return new OOCSSDocument();
	}
}

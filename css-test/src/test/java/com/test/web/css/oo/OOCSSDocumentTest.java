package com.test.web.css.oo;

import com.test.web.css.test.BaseCSSDocumentTest;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class OOCSSDocumentTest extends BaseCSSDocumentTest<OOCSSRule, OOCSSBase> {

	@Override
	protected ICSSDocumentParserListener<OOCSSRule, OOCSSBase> createDocument() {
		return new OOCSSDocument();
	}
}

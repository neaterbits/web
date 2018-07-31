package com.test.web.css.test._long;

import com.test.web.css._long.LongCSSDocument;
import com.test.web.css.test.BaseCSSDocumentTest;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class CSSDocumentTest extends BaseCSSDocumentTest<Integer, Void> {

	@Override
	protected ICSSDocumentParserListener<Integer, Void> createDocument() {
		return new LongCSSDocument();
	}
}

package com.test.web.css._long;

import com.test.web.css.test.BaseCSSDocumentTest;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class CSSDocumentTest extends BaseCSSDocumentTest<Integer, LongTokenizer> {

	@Override
	protected ICSSDocumentParserListener<Integer, LongTokenizer, Void> createDocument() {
		return new LongCSSDocument();
	}
}

package com.test.web.layout;

import com.test.web.document.common.Document;
import com.test.web.document.oo.OOHTMLDocument;
import com.test.web.document.oo.OOTagElement;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.ParserException;

public class OOLayoutTest extends BaseLayoutTest<OOTagElement, OOTokenizer>{

	@Override
	protected Document<OOTagElement> parseDocument(String html) throws ParserException {
		return OOHTMLDocument.parseHTMLDocument(html, null);
	}
}


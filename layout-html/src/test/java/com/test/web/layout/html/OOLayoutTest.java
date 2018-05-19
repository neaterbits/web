package com.test.web.layout.html;

import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.parse.common.ParserException;

public class OOLayoutTest extends BaseLayoutTest<OOTagElement, OOAttribute>{

	@Override
	protected IDocument<OOTagElement, OOAttribute> parseDocument(String html) throws ParserException {
		return OOHTMLDocument.parseHTMLDocument(html, null);
	}
}


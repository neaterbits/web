package com.test.web.layout.html;

import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.util.ParseHTML;

public class OOLayoutTest extends BaseLayoutTest<OOTagElement, OOAttribute, OOHTMLDocument> {

	@Override
	protected OOHTMLDocument parseDocument(String html) throws ParserException {
		return ParseHTML.parseOOHTMLDocument(html, null);
	}
}


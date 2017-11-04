package com.test.web.document.oo;

import java.io.IOException;

import com.test.web.document.test.BaseHTMLDocumentTest;
import com.test.web.io.common.SimpleLoadStream;
import com.test.web.io.oo.OOStringBuffer;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;

public class OOHTMLDocumentTest extends BaseHTMLDocumentTest<OOTagElement, OOHTMLDocument>{

	@Override
	protected OOHTMLDocument parseHTMLDocument(String html) throws ParserException {

		final OOHTMLDocument document = new OOHTMLDocument();
		
		final OOStringBuffer input = new OOStringBuffer(new SimpleLoadStream(html));
		
		final HTMLParser<OOTagElement, OOTokenizer> parser = new HTMLParser<>(
				input,
				input,
				document,
				document.getStyleParserListener());
		
		try {
			parser.parseHTMLFile();
		}
		catch (IOException ex) {
			throw new IllegalStateException("IO eception while parsing", ex);
		}
		
		return document;
	}
}

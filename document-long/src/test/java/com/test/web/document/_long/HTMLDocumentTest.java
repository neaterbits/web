package com.test.web.document._long;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;

import junit.framework.TestCase;

public class HTMLDocumentTest extends TestCase {
	public void testParser() throws IOException, ParserException {
		
		final String html = 
				
"<html>\n" +
"<head>\n" +
"</head>\n" +
"</html>";
		
		final LongHTMLDocument doc = new LongHTMLDocument();
		
		
		final StringBuffers buffers = new StringBuffers(new ByteArrayInputStream(html.getBytes()));
		
		final HTMLParser<LongTokenizer> parser = new HTMLParser<>(buffers, null, doc);
		
		parser.parseHTMLFile();
		
	}
		

}

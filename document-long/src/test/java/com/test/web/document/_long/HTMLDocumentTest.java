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
"<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">" +				
"<html>\n" +
"<!-- a single line comment -->\n" +
"<head>\n" +
"  <title>Document Title</title>\n" +
"  <script type=\"text/javascript\">\n" +
"    function func() {\n" +
"    }\n" +
"  </script>\n" +
"</head>\n" +
"<!-- a \n" +
" multi line \n" +
" comment -->\n" +
"</html>";
		
		final LongHTMLDocument doc = new LongHTMLDocument();
		
		final StringBuffers buffers = new StringBuffers(new ByteArrayInputStream(html.getBytes()));
		
		final HTMLParser<LongTokenizer> parser = new HTMLParser<>(buffers, buffers, doc);
		
		parser.parseHTMLFile();
		
	}
		

}

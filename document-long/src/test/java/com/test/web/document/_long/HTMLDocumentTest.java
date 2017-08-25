package com.test.web.document._long;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class HTMLDocumentTest extends TestCase {
	public void testParser() throws IOException, ParserException {
		
		final String html = 
"<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">" +				
"<html>\n" +
"<!-- a single line comment -->\n" +
"<head>\n" +
"  <title id=\"title_id\" class=\"title_class\">Document Title</title>\n" +
"  <script id=\"script_id\" type=\"text/javascript\">\n" +
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

		final Integer scriptElement = doc.getElementById("script_id");
		
		assertThat(doc.getScriptType(scriptElement)).isEqualTo("text/javascript");
		
		final Integer titleElement = doc.getElementById("title_id");
		assertThat(doc.getClasses(titleElement)).isEqualTo(new String [] { "title_class" });

		assertThat(doc.getElementsWithClass("title_class")).isEqualTo(Arrays.asList(titleElement));
	}
		

}

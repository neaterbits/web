package com.test.web.document._long;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.test.web.document.common.HTMLElement;
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
"  <link rel=\"stylesheet\" type=\"text/css\" href=\"test_stylesheet.css\"/>\n" +
"  <script id=\"script_id\" type=\"text/javascript\">\n" +
"    function func() {\n" +
"    }\n" +
"  </script>\n" +
"</head>\n" +
"<!-- a \n" +
" multi line \n" +
" comment -->\n" +
" <body>\n" +
"   <div id=\"main_div\">\n" +
"     Some text before span\n" +
"     <span class=\"text_content\">This is a text</span>\n" +
"     Some text after span\n" +
"   </div>\n" +
" </body>\n" +
"</html>";
		
		
		final StringBuffers buffers = new StringBuffers(new ByteArrayInputStream(html.getBytes()));

		final LongHTMLDocument doc = new LongHTMLDocument(buffers);
		
		final HTMLParser<LongTokenizer> parser = new HTMLParser<>(buffers, buffers, doc);
		
		parser.parseHTMLFile();
		
		doc.dumpFlat(System.out);

		final Integer scriptElement = doc.getElementById("script_id");
		
		assertThat(doc.getScriptType(scriptElement)).isEqualTo("text/javascript");
		
		final Integer titleElement = doc.getElementById("title_id");
		assertThat(doc.getClasses(titleElement)).isEqualTo(new String [] { "title_class" });

		assertThat(doc.getElementsWithClass("title_class")).isEqualTo(Arrays.asList(titleElement));

		final Integer mainDiv =  doc.getElementById("main_div");
		
		assertThat(mainDiv).isNotNull();
		assertThat(doc.getId(mainDiv)).isEqualTo("main_div");
		
		assertThat(doc.getNumElements(mainDiv)).isEqualTo(3);
		
		final List<Integer> links = doc.getElementsWithType(HTMLElement.LINK);
		
		assertThat(links.size()).isEqualTo(1);
		final Integer link = links.get(0);
		
		assertThat(doc.getLinkRel(link)).isEqualTo("stylesheet");
		assertThat(doc.getLinkType(link)).isEqualTo("text/css");
		assertThat(doc.getLinkHRef(link)).isEqualTo("test_stylesheet.css");
	}
	
	public void testIsLFWhiteSpace() {
		assertThat(Character.isWhitespace('\n')).isEqualTo(true);
	}
}

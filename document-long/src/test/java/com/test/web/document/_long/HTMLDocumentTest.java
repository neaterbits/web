package com.test.web.document._long;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.test.web.document.common.HTMLElement;
import com.test.web.parse.common.ParserException;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;

public class HTMLDocumentTest extends TestCase {
	public void testParser() throws IOException, ParserException {
		
		final String html = TestData.HTML;
		
		final LongHTMLDocument doc = LongHTMLDocument.parseHTMLDocument(html);
		
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

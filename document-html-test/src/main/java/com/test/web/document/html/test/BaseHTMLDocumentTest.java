package com.test.web.document.html.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.IDocument;
import com.test.web.parse.common.ParserException;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;

public abstract class BaseHTMLDocumentTest<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>> extends TestCase {
	
	protected abstract DOCUMENT parseHTMLDocument(String html) throws ParserException;
	
	public void testParser() throws IOException, ParserException {
		
		final String html = TestData.HTML;
		
		final DOCUMENT doc = parseHTMLDocument(html);
		
		doc.dumpFlat(System.out);

		final ELEMENT scriptElement = doc.getElementById("script_id");
		
		assertThat(doc.getScriptType(scriptElement)).isEqualTo("text/javascript");
		
		final ELEMENT titleElement = doc.getElementById("title_id");
		assertThat(doc.getClasses(titleElement)).isEqualTo(new String [] { "title_class" });

		assertThat(doc.getElementsWithClass("title_class")).isEqualTo(Arrays.asList(titleElement));

		final ELEMENT mainDiv =  doc.getElementById("main_div");
		
		assertThat(mainDiv).isNotNull();
		assertThat(doc.getId(mainDiv)).isEqualTo("main_div");
		
		assertThat(doc.getNumElements(mainDiv)).isEqualTo(3);
		
		final List<ELEMENT> links = doc.getElementsWithType(HTMLElement.LINK);
		
		assertThat(links.size()).isEqualTo(1);
		final ELEMENT link = links.get(0);
		
		assertThat(doc.getLinkRel(link)).isEqualTo("stylesheet");
		assertThat(doc.getLinkType(link)).isEqualTo("text/css");
		assertThat(doc.getLinkHRef(link)).isEqualTo("test_stylesheet.css");
	}

	public void testFirstCharMissingFromTextIssue() throws IOException, ParserException {
		
		final String html = TestData.HTML_TEXT_ISSUE;
		
		System.out.print("Char at 168 is '" + html.charAt(168) + "'");
		
		parseHTMLDocument(html);
	}
	
	public void testIsLFWhiteSpace() {
		assertThat(Character.isWhitespace('\n')).isEqualTo(true);
	}
}

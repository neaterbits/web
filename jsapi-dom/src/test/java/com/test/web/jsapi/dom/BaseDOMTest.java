package com.test.web.jsapi.dom;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.oo.OOHTMLDocument;
import com.test.web.document.oo.OOTagElement;
import com.test.web.document.test.DocumentParser;
import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.parse.common.ParserException;

import junit.framework.TestCase;

public abstract class BaseDOMTest extends TestCase {

	abstract IJSEngine getJSEngine();

	public void testAttributeSetting() throws ParserException, JSCompileException, JSExecutionException {

		final IJSEngine jsEngine = getJSEngine();

		// Loop through all elements and their attributes
		for (HTMLElement element : HTMLElement.values()) {
			
			// Global attributes
			for (HTMLAttribute attribute : HTMLAttribute.getGlobalAttributes()) {
				checkAttribute(jsEngine, element, attribute);
			}
			
			// Element specific attributes
			for (HTMLAttribute attribute : element.getAttributes()) {
				checkAttribute(jsEngine, element, attribute);
			}
		}
	}

	private void checkAttribute(IJSEngine jsEngine, HTMLElement element, HTMLAttribute attribute) throws ParserException, JSCompileException, JSExecutionException {

		// Generate some simple HTML for a DOM
		
		final String elementId = "element_to_test";

		final String html = "";
		
		final CSSContext<OOCSSElement> cssContext = new CSSContext<>();
		
		final OOHTMLDocument document = OOHTMLDocument.parseHTMLDocument(
				html,
				charInput -> DocumentParser.parseCSS(charInput, cssContext, new OOCSSDocument()));

		// Now run some JS tests
		final JSVariableMap varMap = new JSVariableMap();
		
		final DocumentContext<OOTagElement> documentContext = new DocumentContext<>(document);

		varMap.addReflected("document", new DOMDocument<OOTagElement, DocumentContext<OOTagElement>>(documentContext, null));
		
		jsEngine.evalJS("document.getElementById()", varMap);
	}
}


package com.test.web.parse.html;

import com.test.web.parse.css.CSSParserListener;

public interface IHTMLStyleParserListener<HTML_ELEMENT, CSS_CONTEXT>
	extends CSSParserListener<CSS_CONTEXT> {
	
	// Starts parsing a single value/attribute pair of the style attribute
	void startParseStyleElement(HTML_ELEMENT htmlElement, String styleText);
}

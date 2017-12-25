package com.test.web.parse.html;

import com.test.web.parse.css.CSSParserListener;

public interface IHTMLStyleParserListener<HTML_ELEMENT>
	extends CSSParserListener<Void> {
	
	// Starts parsing a single value/attribute pair of the style attribute
	void startParseStyleElement(HTML_ELEMENT htmlElement, String styleText);
}

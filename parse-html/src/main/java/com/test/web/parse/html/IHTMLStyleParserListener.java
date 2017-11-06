package com.test.web.parse.html;

import com.test.web.io.common.Tokenizer;
import com.test.web.parse.css.CSSParserListener;

public interface IHTMLStyleParserListener<HTML_ELEMENT, TOKENIZER extends Tokenizer>
	extends CSSParserListener<TOKENIZER, Void> {
	
	// Starts parsing a single value/attribute pair of the style attribute
	void startParseStyleElement(HTML_ELEMENT htmlElement);
}

package com.test.web.parse.html;

import com.test.web.io.common.Tokenizer;
import com.test.web.parse.css.CSSParserListener;

public interface IHTMLStyleParserListener<ELEMENT, TOKENIZER extends Tokenizer>
	extends CSSParserListener<TOKENIZER, Void> {
	
	void startParseStyleElement(ELEMENT htmlElement);
}

package com.test.web.parse.html;

import com.test.web.io.common.Tokenizer;
import com.test.web.parse.html.enums.HTMLAttribute;
import com.test.web.parse.html.enums.HTMLElement;

/**
 * Parser listener that calls onto model as we are parsing along the input document 
 * @author nhl
 *
 * @param <INPUT>
 * @param <TOKENIZER>
 */

public interface HTMLParserListener<TOKENIZER extends Tokenizer> {

	void onElementStart(TOKENIZER tokenizer, HTMLElement element);

	void onElementEnd(TOKENIZER tokenizer, HTMLElement element);
	
	void onText(TOKENIZER tokentizer);
	
	void onAttribute(TOKENIZER tokenizer, HTMLAttribute attribute);

	void onAttributeValue(TOKENIZER tokenizer, HTMLAttribute attribute);

	void onStyleAttributeValue(TOKENIZER tokenizer, String key, String value);
}

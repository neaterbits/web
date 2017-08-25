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
	
	void onAttributeWithoutValue(TOKENIZER tokenizer, HTMLAttribute attribute);

	void onAttributeWithValue(TOKENIZER tokenizer, HTMLAttribute attribute, int startOffset, int endSkip);

	void onStyleAttributeValue(TOKENIZER tokenizer, String key, String value);
}

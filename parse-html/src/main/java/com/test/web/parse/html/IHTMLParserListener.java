package com.test.web.parse.html;

import java.io.IOException;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.io.common.Tokenizer;

/**
 * Parser listener that calls onto model as we are parsing along the input document 
 * @author nhl
 *
 * @param <INPUT>
 * @param <TOKENIZER>
 */

public interface IHTMLParserListener<ELEMENT, TOKENIZER extends Tokenizer> {

	ELEMENT onElementStart(TOKENIZER tokenizer, HTMLElement element) throws IOException;

	ELEMENT onElementEnd(TOKENIZER tokenizer, HTMLElement element) throws IOException;
	
	void onText(TOKENIZER tokenizer, int startOffset, int endSkip);
	
	void onAttributeWithoutValue(TOKENIZER tokenizer, HTMLAttribute attribute);

	void onAttributeWithValue(TOKENIZER tokenizer, HTMLAttribute attribute, int startOffset, int endSkip, HTMLElement element);

	void onClassAttributeValue(TOKENIZER tokenizer, int startOffset, int endSkip);

	void onStyleAttributeValue(TOKENIZER tokenizer, String key, String value);
}

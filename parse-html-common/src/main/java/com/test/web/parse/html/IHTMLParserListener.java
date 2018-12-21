package com.test.web.parse.html;

import java.io.IOException;

import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.io.common.Tokenizer;

/**
 * Parser listener that calls onto model as we are parsing along the input document 
 * @author nhl
 *
 * @param <INPUT>
 * @param <Tokenizer>
 */

public interface IHTMLParserListener<ELEMENT> {

	ELEMENT onElementStart(Tokenizer Tokenizer, HTMLElement element) throws IOException;

	ELEMENT onElementEnd(Tokenizer Tokenizer, HTMLElement element) throws IOException;
	
	void onText(Tokenizer Tokenizer, long stringRef);
	
	void onAttributeWithoutValue(Tokenizer Tokenizer, HTMLAttribute attribute);

	void onAttributeWithValue(Tokenizer Tokenizer, HTMLAttribute attribute, long stringRef, HTMLElement element);

	void onClassAttributeValue(Tokenizer Tokenizer, long stringRef);

	void onStyleAttributeValue(Tokenizer Tokenizer, String key, String value);
}

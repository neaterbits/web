package com.test.web.document._long;

import java.util.ArrayList;
import java.util.List;

import com.test.web.buffers.LongBuffersIntegerIndex;
import com.test.web.document.common.Document;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io.common.CharInput;
import com.test.web.parse.html.HTMLParserListener;
import com.test.web.parse.html.enums.HTMLAttribute;
import com.test.web.parse.html.enums.HTMLElement;

/**
 * Stores the read document as a series of compacted long-values.
 * This in order to perform as few allocations as possible, avoid memory use for object headers
 * and avoid GC churn.
 * 
 * All common attributes are implemented as bit-flags into buffer with a certain length
 * 
 * Downside is dynamic pages where there are a lot of updates via Javascript
 *  
 * @author nhl
 *
 */


public class LongHTMLDocument extends LongBuffersIntegerIndex

	implements Document, HTMLParserListener<LongTokenizer> {

	private static final int INITIAL_BUFFERS = 100;
	
	
	// Stack for position while parsing
	private final List<Integer> stack;
	
	
	
	public LongHTMLDocument() {
		this.stack = new ArrayList<>();
	}


	private void pushElement() {
		
	}


	@Override
	public void onElementStart(LongTokenizer tokenizer, HTMLElement element) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onElementEnd(LongTokenizer tokenizer, HTMLElement element) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAttribute(LongTokenizer tokenizer, HTMLAttribute attribute) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAttribute(LongTokenizer tokenizer, HTMLAttribute attribute, String value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStyleAttributeValue(LongTokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}
}

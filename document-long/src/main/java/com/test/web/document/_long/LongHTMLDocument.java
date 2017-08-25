package com.test.web.document._long;

import com.test.web.buffers.LongBuffersIntegerIndex;
import com.test.web.buffers.StringStorageBuffer;
import com.test.web.document.common.Document;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.html.HTMLParserListener;
import com.test.web.parse.html.enums.HTMLAttribute;
import com.test.web.parse.html.enums.HTMLDirection;
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
	private final int [] stack;
	private int depth; 
	
	private final StringStorageBuffer idBuffer;
	private final StringStorageBuffer classBuffer;
	private final StringStorageBuffer accessKeyBuffer;
	private final StringStorageBuffer contextMenuBuffer;
	private final StringStorageBuffer langBuffer;
	private final StringStorageBuffer titleBuffer;
	private final StringStorageBuffer typeBuffer;
	
	
	public LongHTMLDocument() {
		this.stack = new int[100];
		
		this.idBuffer = new StringStorageBuffer();
		this.classBuffer = new StringStorageBuffer();
		
		// Just reuse same buffer
		this.accessKeyBuffer = this.contextMenuBuffer = this.langBuffer = this.titleBuffer = this.typeBuffer = new StringStorageBuffer();
	}


	private void pushElement(int element) {
		if (depth == stack.length) {
			throw new IllegalStateException("Reached max stack size: " + stack.length);
		}
		
		stack[depth ++] = element;
	}
	
	private int getCurElement() {
		return stack[depth - 1];
	}
	
	private void popElement() {
		if (depth == 0) {
			throw new IllegalStateException("depth == 0");
		}
		
		-- depth;
	}


	@Override
	public void onElementStart(LongTokenizer tokenizer, HTMLElement element) {
		
		// Start of an HTML element, add to buffer and
		
		final int numLongs = element.isContainerElement()
				? LongHTML.SIZE_CONTAINER_ELEMENT
				: LongHTML.SIZE_LEAF_ELEMENT;
		
		final int elementRef = allocate(numLongs);

		if (depth != 0) {
			appendElement(getCurElement(), elementRef);
		}
		
		pushElement(elementRef);
	}
	
	private void appendElement(int cur, int elementRef) {
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		// Append to current
		final long [] curBuf = buf(cur);
		final int curOffset = offset(cur);
		
		// ref to tail element of list, we must update this one
		final int tail = LongHTML.getTail(curBuf, curOffset);
		
		if (tail == LongHTML.END_OF_LIST_MARKER) {
			// Empty list, sets head and tail to this
			LongHTML.setHead(curBuf, curOffset, elementRef);
			LongHTML.setTail(curBuf, curOffset, elementRef);

			LongHTML.setElementLast(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
		}
		else {
		
			final long [] tailBuf = buf(tail);

			LongHTML.setElementNext(tailBuf, offset(tail), elementRef);
			LongHTML.setElementLast(elementBuf, elementOffset, tail);
		}

		LongHTML.setElementNext(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
		LongHTML.setParent(elementBuf, elementOffset, cur);
	}

	@Override
	public void onElementEnd(LongTokenizer tokenizer, HTMLElement element) {
		popElement();
	}

	@Override
	public void onText(LongTokenizer tokentizer) {
		final int elementRef = allocate(LongHTML.SIZE_TEXT);
		
		appendElement(getCurElement(), elementRef);
	}


	@Override
	public void onAttributeWithoutValue(LongTokenizer tokenizer, HTMLAttribute attribute) {
		
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		switch (attribute) {
		
		case CONTENTEDITABLE:
			LongHTML.setContentEditable(elementBuf, elementOffset, true);
			break;
			
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}


	@Override
	public void onAttributeWithValue(LongTokenizer tokenizer, HTMLAttribute attribute) {
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		switch (attribute) {
		
		case ACCESSKEY:
			LongHTML.setAccessKey(elementBuf, elementOffset, tokenizer.addToBuffer(accessKeyBuffer));
			break;
			
		case CONTENTEDITABLE:
			LongHTML.setContentEditable(elementBuf, elementOffset, tokenizer.equalsIgnoreCase("true"));
			break;
			
		case CONTEXTMENU:
			LongHTML.setContextMenu(elementBuf, elementOffset, tokenizer.addToBuffer(contextMenuBuffer));
			break;
			
		case DIRECTION:
			LongHTML.setDirection(elementBuf, elementOffset, tokenizer.asEnum(HTMLDirection.class, false));
			break;
			
		// script type as string
		case TYPE:
			LongHTML.setScriptType(elementBuf, elementOffset, tokenizer.addToBuffer(typeBuffer));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}


	@Override
	public void onStyleAttributeValue(LongTokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}
}

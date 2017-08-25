package com.test.web.document._long;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	implements Document<Integer>, HTMLParserListener<LongTokenizer> {

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

	private final Map<String, Integer> elementById;
	private final Map<String, List<Integer>> elementsByClass;

	// Array of element classes that are appended to every time we read an element
	// TODO: perhaps use a linked-list? We can only add classes here since we only stor index and count for each element, class would seldom be updated dynamically though
	
	private int [] elementClasses;
	private int numElementClasses;
	
	public LongHTMLDocument() {
		this.stack = new int[100];
		
		this.elementById = new HashMap<>();
		this.elementsByClass = new HashMap<>();
		
		this.idBuffer = new StringStorageBuffer();
		this.classBuffer = new StringStorageBuffer();
		
		this.elementClasses = new int[100];
		this.numElementClasses = 1; // Skip initial so that default encoded class value (0) never points to an index 
		
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
		
		final int numLongs = LongHTML.elementSize(element);
		
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
	public void onAttributeWithValue(LongTokenizer tokenizer, HTMLAttribute attribute, int startOffset, int endSkip) {
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		switch (attribute) {
		
		case ID:
			final int idStringRef = tokenizer.addToBuffer(idBuffer, startOffset, endSkip);
			final String idString = idBuffer.getString(idStringRef);

			elementById.put(idString, getCurElement());
			
			LongHTML.setId(elementBuf, elementOffset, idStringRef);
			break;
		
		case ACCESSKEY:
			LongHTML.setAccessKey(elementBuf, elementOffset, tokenizer.addToBuffer(accessKeyBuffer, startOffset, endSkip));
			break;
			
		case CONTENTEDITABLE:
			LongHTML.setContentEditable(elementBuf, elementOffset, tokenizer.equalsIgnoreCase("true"));
			break;
			
		case CONTEXTMENU:
			LongHTML.setContextMenu(elementBuf, elementOffset, tokenizer.addToBuffer(contextMenuBuffer, startOffset, endSkip));
			break;
			
		case DIRECTION:
			LongHTML.setDirection(elementBuf, elementOffset, tokenizer.asEnum(HTMLDirection.class, false));
			break;
			
		// script type as string
		case TYPE:
			LongHTML.setScriptType(elementBuf, elementOffset, tokenizer.addToBuffer(typeBuffer, startOffset, endSkip));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}

	@Override
	public void onClassAttributeValue(LongTokenizer tokenizer, int startOffset, int endSkip) {
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		final int classStringRef = tokenizer.addToBuffer(classBuffer, startOffset, endSkip);
		
		final String classString = classBuffer.getString(classStringRef);

		List<Integer> elementsWithClass = elementsByClass.get(classString);
		
		final int element = getCurElement();
		
		if (elementsWithClass == null) {
			elementsWithClass = new ArrayList<>();
			elementsByClass.put(classString, elementsWithClass);

			elementsWithClass.add(element);
		}
		else {
			if (!elementsWithClass.contains(element)) {
				elementsWithClass.add(element);
			}
		}
		
		final int existingClassRef = LongHTML.getClass(elementBuf, elementOffset);

		if (numElementClasses == elementClasses.length) {
			this.elementClasses = Arrays.copyOf(elementClasses, elementClasses.length * 2);
		}
		
		final int updatedClassRef;
		

		if (existingClassRef == 0) {
			// Not set since starts counting from 0
			updatedClassRef = numElementClasses << 16 | 1; // length 1 
		}
		else {
			final int classTableIdx = existingClassRef >> 16;
			final int classTableLength = existingClassRef & 0x0000FFFF;

			if (classTableIdx + classTableLength != numElementClasses ) {
				throw new IllegalArgumentException("Not last element");
			}
			
			updatedClassRef = (classTableIdx << 16) | (classTableLength + 1);
		}
		
		elementClasses[numElementClasses ++] = classStringRef;
		

		LongHTML.setClass(elementBuf, elementOffset, updatedClassRef);
	}


	@Override
	public void onStyleAttributeValue(LongTokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getElementById(String id) {
		return elementById.get(id);
	}

	@Override
	public String [] getClasses(Integer element) {
		final int elementOffset = offset(element);
		final long [] elementBuf = buf(element);
		
		final int classRef = LongHTML.getClass(elementBuf, elementOffset);
		
		final int numClasses = classRef & 0x0000FFFF;
		
		final String [] ret = new String[numClasses];
		
		final int startIdx = classRef >> 16;
		
		for (int i = 0; i < numClasses; ++ i) {
			ret[i] = classBuffer.getString(elementClasses[startIdx + i]);
		}
		
		return ret;
	}


	@Override
	public List<Integer> getElementsWithClass(String _class) {
		final List<Integer> elements = elementsByClass.get(_class);
		
		return elements != null ? elements : Collections.emptyList();
	}


	@Override
	public String getScriptType(Integer element) {
		final int ref = LongHTML.getScriptType(buf(element), offset(element));
		
		return typeBuffer.getString(ref);
	}
}

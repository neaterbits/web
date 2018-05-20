package com.test.web.layout.blockinline;

import java.util.function.Supplier;

import com.test.web.layout.algorithm.ElementLayoutSettersGetters;
import com.test.web.render.common.IFont;

/**
 * For caching inline elements in a tree through StackElement until we have at least got to liewrap
 * and can look for elements for which we can compute complete dimensions.
 * 
 * InlineElement is either
 *  - an inline HTML element, this will have a StackElement associated with it
 *  - a text chunk, that is we split text into chunks as we wrap from one line to another so that each chunk has a rectangular
 *    bounding box. Eg. <span>some text that is longer than the bounding box width</span> that was wrapped after  "than" would then be two
 *    text chunks, each with their owen computed layout. This makes it fast to rerender text from layout if necessary since we do not
 *    have to get text extent again.
 *    
 *    Like for StackElement we do not do any subclassing but just put both types in the same class so that we might re-init objects on the stack
 *    or keep a free-list to minimize GC (allocating from a free-list of same class i very fast)
 */

public final class InlineElement {
	
	public enum Type {
		// An HTML element where we know width and height from eg image width/height (from HTML or from the downloaded image data)
		KNOWN_SIZE_HTML_ELEMENT, 

		// start of an element for which we do not know the size, eg. a <span> element with some styles applied
		WRAPPER_HTML_ELEMENT_START,
		WRAPPER_HTML_ELEMENT_END, // end of same element, so that we know when to update inline element layout

		// a rectangular text chunk, inside a wrapper HTML element
		TEXT_CHUNK
	}
	
	// State use for caching of allocated elements, allowing us to reuse objects
	private enum State {
		ALLOCATED, // Newly allocated
		IN_USE,       // in use within layout algorith processing
		CLEARED;    // No longer in use, and cleared such that can be reused
	}
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	private State state;
	private Type type;
	private int lineNo; // Line no from 0-n within block
	
	
	// -------------------------- HTMLelements  --------------------------
	private StackElement<?> stackElement; // Every HTML element has a corresponding StackElement with all CSS properties computed at that level

	// -------------------------- Text chunks --------------------------
	private String textChunk; // Cache text to render when reaching end of text line
	private ElementLayoutSettersGetters textResultingLayout; // The layout of this text chunk element, this is used as scratch area for computation

	public InlineElement() {
		setState(State.ALLOCATED);
	}
	
	private void checkNotInUse() {
		if (state != State.ALLOCATED && state != State.CLEARED) {
			throw new IllegalStateException("in use: " + state);
		}
	}
	
	private void setState(State state) {
		this.state = state;
	}
	
	final int getLineNo() {
		return lineNo;
	}

	public final Type getType() {
		return type;
	}
	
	final StackElement<?> getStackElement() {
		return stackElement;
	}
	
	public final ElementLayoutSettersGetters getResultingLayout() {
		
		final ElementLayoutSettersGetters result;

		switch (type) {
		case KNOWN_SIZE_HTML_ELEMENT:
			result = stackElement.resultingLayout;
			break;

		case TEXT_CHUNK:
			result = textResultingLayout;
			break;
			
		// can also set layout on <span> elements
		case WRAPPER_HTML_ELEMENT_START:
			result = stackElement.resultingLayout;
			break;

		default:
			throw new IllegalStateException("Can only set layout for known size html element or text chunk");
		}

		return result;
	}
	
	private void setInUse(Type type) {
		if (type == null) {
			throw new IllegalArgumentException("type == null");
		}

		checkNotInUse();

		setState(State.IN_USE);

		this.type = type;
	}

	private void initWithStackElement(int lineNo, StackElement<?> stackElement, Type type) {
		if (stackElement == null) {
			throw new IllegalArgumentException("stackElement == null");
		}

		setInUse(type);

		this.lineNo = lineNo;
		this.stackElement = stackElement;
	}

	void initKnownSizeHTMLElement(int lineNo, StackElement<?> stackElement) {
		initWithStackElement(lineNo, stackElement, Type.KNOWN_SIZE_HTML_ELEMENT);
	}
	
	void initWrapperHTMLElementStart(int lineNo, StackElement<?> stackElement) {
		initWithStackElement(lineNo, stackElement, Type.WRAPPER_HTML_ELEMENT_START);
	}

	void initWrapperHTMLElementEnd(int lineNo, StackElement<?> stackElement) {
		initWithStackElement(lineNo, stackElement, Type.WRAPPER_HTML_ELEMENT_END);
	}

	ElementLayoutSettersGetters initTextChunk(int lineNo, String text, IFont font, Supplier<ElementLayoutSettersGetters> create) {

		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}
		
		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}

		setInUse(Type.TEXT_CHUNK);
		this.lineNo = lineNo;

		if (this.textResultingLayout == null) {
			// Create a scratch area for computing layout for this chunk
			this.textResultingLayout = create.get();
		}
		else {
			// Reuse existing but clear first
			textResultingLayout.clear();
		}

		textResultingLayout.setFont(font);

		this.textChunk = text;

		return textResultingLayout;
	}
	
	void clear() {
		if (state == State.ALLOCATED) {
			throw new IllegalStateException("Clearing newly allocated element");
		}
		
		setState(State.CLEARED);
		this.type = null;
		
		if (stackElement != null) {
			this.stackElement = null;
		}
		
		if (textResultingLayout != null) {
			textResultingLayout.clear();
		}
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}

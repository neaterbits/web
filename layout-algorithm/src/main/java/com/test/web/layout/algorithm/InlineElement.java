package com.test.web.layout.algorithm;

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

final class InlineElement {
	enum State {
		ALLOCATED,
		HTML_ELEMENT,
		TEXT_CHUNK,
		CLEARED;
	}
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	private State state;

	// -------------------------- HTMLelements  --------------------------
	private StackElement stackElement; // Every HTML element has a corresponding StackElement with all CSS properties computed at that level

	// -------------------------- Text chunks --------------------------
	private String textChunk; // Cache text to render when reaching end of text line
	private ElementLayout resultingLayout; // The layout of this text chunk element, this is used as scratch area for computation

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
	
	void initHTMLElement(StackElement stackElement) {
		if (stackElement == null) {
			throw new IllegalArgumentException("stackElement == null");
		}
		
		checkNotInUse();
		
		setState(State.HTML_ELEMENT);
		
		this.stackElement = stackElement;
	}
	
	ElementLayout initTextChunk(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}
		
		checkNotInUse();
		
		setState(State.TEXT_CHUNK);
		
		if (this.resultingLayout == null) {
			// Create a scratch area for computing layout for this chunk
			this.resultingLayout = new ElementLayout();
		}
		else {
			// Reuse existing but clear first
			resultingLayout.clear();
		}
		
		this.textChunk = text;
		
		return resultingLayout;
	}
	
	void clear() {
		if (state == State.ALLOCATED) {
			throw new IllegalStateException("Clearing newly allocated element");
		}
		
		setState(State.CLEARED);
	}
}

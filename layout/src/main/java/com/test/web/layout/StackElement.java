package com.test.web.layout;

import java.util.Arrays;

import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.enums.CSSDisplay;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
final class StackElement {
	
	static final int UNKNOWN_WIDTH = 0x01;
	static final int UNKNOWN_HEIGHT = 0x02;
	
	// flags for layout dimensions that cannot be computed until we have computed dimensions of all inner-elements
	int delayedLayout;
	
	// available width and height at this level, ie. the max dimension that the element at this level can have
	// width is given by the viewport at the window level mainly for width but also for height if specified as 100%
	private int availableWidth;
	private int availableHeight;

	// remaining width and height when adding elements
	// this so that we can find the size of elements that do not have these specified
	private int remainingWidth;
	private int remainingHeight;
	
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	final CSSLayoutStyles layoutStyles;
	
	// The resulting layout after computation of width and height, this is what rendering sees
	final ElementLayout resultingLayout;

	private BaseLayoutCase layoutCase;
	
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	// -------------------------- Text specific --------------------------

	// -------------------------- For inline elements  --------------------------
	
	// -------------------------- For block behaving elements  --------------------------

	// Max height for inline elements on current inline textline
	// When a text line wraps, the baseline will be computed from this
	// Thus rendering of a line cannot happen until the whole line is processed and baseline is known
	private int maxTextLineElementHeight;

	
	// Current height of block element (if this is one). This is the sum of all textline heights.
	// Unrelated to height specified by CSS, that would have to be handled by overflow attribute.
	private int curBlockElementHeight;
	
	// track all inline elements on one text line until we have enough to render the line
	private TextLineElement [] elementsOnThisTextLine;
	private int numElementsOnThisTextLine;

	StackElement(int availableWidth, int availableHeight) {
		this();
		
		init(availableWidth, availableHeight);
	}
	
	StackElement() {
		this.layoutStyles = new CSSLayoutStyles();
		this.resultingLayout = new ElementLayout();
	}
	
	void clear() {
		layoutStyles.clear();
		
		// reset array counter but keep array though to save on memory allocations
		this.numElementsOnThisTextLine = 0;
	}

	void init(int availableWidth, int availableHeight) {
		
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}
		
		if (availableHeight == 0) {
			throw new IllegalArgumentException("availableHeight == 0");
		}
		
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;
		
		this.remainingWidth = availableWidth;
		this.remainingHeight = availableHeight;

		this.curBlockElementHeight = 0;
	}

	void addInlineText(String text) {
		// Inherits the layout of this element
		addTextLineElement().init(text);
	}

	// Add some nested inline element, like an image
	void addInlineElement(ElementLayout layout) {
		addTextLineElement().init(layout);
	}

	private TextLineElement addTextLineElement() {
		final TextLineElement ret;
		
		// could use ArrayList but this is much-accessed part so just use array directly
		// not that complicated anyway and isolated to this method
		if (elementsOnThisTextLine == null) {
			// Just allocate a good number, not much memory since is in a stack
			this.elementsOnThisTextLine = new TextLineElement[20];
			
			ret = elementsOnThisTextLine[0] = new TextLineElement();
			numElementsOnThisTextLine = 1;
		}
		else {
			if (numElementsOnThisTextLine == elementsOnThisTextLine.length) {
				// expand array
				this.elementsOnThisTextLine = Arrays.copyOf(elementsOnThisTextLine, elementsOnThisTextLine.length * 2);
			}
			
			TextLineElement existing = elementsOnThisTextLine[numElementsOnThisTextLine];
			
			if (existing == null) {
				ret = elementsOnThisTextLine[numElementsOnThisTextLine] = new TextLineElement();
			}
			else {
				ret = existing;
			}
		}
		
		return ret;
	}
	

	int getAvailableWidth() {
		return availableWidth;
	}

	void setAvailableWidth(int availableWidth) {
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		this.availableWidth = availableWidth;
	}

	int getAvailableHeight() {
		return availableHeight;
	}

	void setAvailableHeight(int availableHeight) {
		this.availableHeight = availableHeight;
	}
	
	int getRemainingWidth() {
		return remainingWidth;
	}

	void setRemainingWidth(int remainingWidth) {
		this.remainingWidth = remainingWidth;
	}

	int getRemainingHeight() {
		return remainingHeight;
	}

	void setRemainingHeight(int remainingHeight) {
		this.remainingHeight = remainingHeight;
	}

	CSSLayoutStyles getLayoutStyles() {
		return layoutStyles;
	}

	ElementLayout getResultingLayout() {
		return resultingLayout;
	}
	
	CSSDisplay getDisplay() {
		return layoutStyles.getDisplay();
	}

	BaseLayoutCase getLayoutCase() {
		return layoutCase;
	}

	void setLayoutCase(BaseLayoutCase layoutCase) {
		this.layoutCase = layoutCase;
	}
}


package com.test.web.layout.blockinline;

import java.util.Arrays;
import java.util.function.Supplier;

import com.test.web.layout.algorithm.ElementLayoutSettersGetters;
import com.test.web.layout.algorithm.LayoutStackElement;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

abstract class StackElementBase<ELEMENT> extends LayoutStackElement<ELEMENT> {

	private static final boolean CHECK_ADD = true;

	StackElementBase(int stackIdx) {
		super(stackIdx);
	}
	
	// -------------------------- For block or inline elements, ie for one inline. Also inline within inline  --------------------------
	
	// Keep track of all inline sub stack elements, this is since we cannot know the dimensioning of inline elements
	// at least until line wrap when we know the height of the tallest element.
	// At that point we can update layout of elements



	// Elements within an element, either a block or an inline element (eg a span within a div or nested spans)
	private InlineElement [] inlineElements;
	private int firstInlineElement; // while processing inline-elements, we will increase this to point to the element that is currently being processed
	private int numInlineElements; // number of inline elements in the array above, this mmight be the same as array.length in case we are reusing existing objects

	final int getFirstInlineElement() {
		return firstInlineElement;
	}
	
	final int getNumInlineElements() {
		return numInlineElements;
	}
	
	final InlineElement getInlineElementAt(int index) {
		return inlineElements[index];
	}
	
	final void updateFirstInlineElement(int idx) {
		
		if (idx >= numInlineElements) {
			throw new IllegalArgumentException("idx >= numInlineElements: " + idx + "/" + numInlineElements);
		}

		this.firstInlineElement = idx;
	}
	
	final String inlineElementsDebugString() {
		return Arrays.toString(inlineElements);
	}

	/**
	 * Add inline text chunk, eg. <span>This is a text</span>
	 * Note that for a long text that spans multiple lines, this will be called multiple times
	 * as the text is wrapped over multiple lines.
	 * 
	 * @param text the text to add for
	 * @param subLayout computed layout for text
	 */
	final IElementRenderLayout addInlineTextChunk(int inlineLineNoInBlock, String text, IFont font, int width, int height, int zIndex, IDelayedRenderer renderer, Supplier<ElementLayoutSettersGetters> create) {
		// Inherits the layout of this element
		final ElementLayoutSettersGetters textChunkLayout = addTextLineElement().initTextChunk(inlineLineNoInBlock, text, font, create);

		textChunkLayout.initInnerWidthHeight(width, height);
		textChunkLayout.initOuterWidthHeight(width, height);
		
		textChunkLayout.setRenderer(zIndex, renderer);

		return textChunkLayout;
	}

	/**
	 * Add inline element, eg. the image in <span>This is a text<img ...> and an image</span>
	 * 
	 * @param layout computed layout for text
	 * @param atStartOfLine true if first element or is first after line wrap
	 */

	final void addInlineElementStart(StackElement<ELEMENT> stackElement, int inlineLineNoInBlock) {

		stackElement.addedInSeparateTree();
	
		if (CHECK_ADD) {
			for (int i = firstInlineElement; i < numInlineElements; ++ i) {
				if (inlineElements[i].getStackElement() == stackElement) {
					throw new IllegalArgumentException("stack element already added at " + i + "/" + numInlineElements);
				}
			}
		}
		
		// Make sure we are adding an inline-element, otherwise should not call this method
		if (!stackElement.getDisplay().isInline()) {
			throw new IllegalArgumentException("Adding inline element layout that is not inline-display : "  + stackElement.getDisplay());
		}

		addTextLineElement().initWrapperHTMLElementStart(inlineLineNoInBlock, stackElement);
	}

	final void addInlineElementEnd(StackElement<ELEMENT> stackElement, int inlineLineNoInBlock) {
		
		// Make sure we are adding an inline-element, otherwise should not call this method
		if (!stackElement.getDisplay().isInline()) {
			throw new IllegalArgumentException("Adding inline element layout that is not inline-display : "  + stackElement.getDisplay());
		}

		addTextLineElement().initWrapperHTMLElementEnd(inlineLineNoInBlock, stackElement);
	}
	
	final boolean hasAnyInlineElementsAdded() {
		return numInlineElements > 0;
	}

	
	private InlineElement addTextLineElement() {
		final InlineElement ret;
		
		// could use ArrayList but this is much-accessed part so just use array directly
		// not that complicated anyway and isolated to this method
		if (inlineElements == null) {
			// Just allocate a good number, not much memory since is in a stack
			this.inlineElements = new InlineElement[20];
			
			ret = inlineElements[0] = new InlineElement();
			numInlineElements = 1;
		}
		else {
			if (numInlineElements == inlineElements.length) {
				// expand array
				this.inlineElements = Arrays.copyOf(inlineElements, inlineElements.length * 2);
			}
			
			InlineElement existing = inlineElements[numInlineElements];
			
			if (existing == null) {
				ret = inlineElements[numInlineElements] = new InlineElement();
			}
			else {
				existing.clear();
				ret = existing;
			}
			
			 ++ numInlineElements;
		}
		
		return ret;
	}
	
	final void initBase() {

	}
	
	final void clearBase() {

		// reset array counter but keep array though to save on memory allocations
		this.firstInlineElement = 0;
		this.numInlineElements = 0;
	}
}

package com.test.web.layout.blockinline;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.test.web.layout.algorithm.ElementLayoutSettersGetters;
import com.test.web.layout.algorithm.LayoutStackElement;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

abstract class StackElementBase extends LayoutStackElement {


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

	final void addInlineElementStart(StackElement stackElement, int inlineLineNoInBlock) {

		stackElement.addedInSeparateTree();
	
		// Make sure we are adding an inline-element, otherwise should not call this method
		if (!stackElement.getDisplay().isInline()) {
			throw new IllegalArgumentException("Adding inline element layout that is not inline-display : "  + stackElement.getDisplay());
		}

		addTextLineElement().initWrapperHTMLElementStart(inlineLineNoInBlock, stackElement);
	}

	final void addInlineElementEnd(StackElement stackElement, int inlineLineNoInBlock) {
		
		// TODO simplify memory reuse handling
		stackElement.removedFromSeparateTree();

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
		}
		
		return ret;
	}
	
	/**
	 * Process all inline text lines up to the specified line, removing 
	 * any <inlineElement></inlineElement> that ends at this layouted line. Eg. a <span> that spans multiple lines
	 * and then its content is terminated on lastLine, the <span> will be removed from inline-elements being cached here so that it is no longer processed.
	 * This also might allow the StackElement object to be cleared and reused.
	 * 
	 * @param lastLine the last line being processed
	 * @param consumer process inline elements appearing from the last line up to the current line
	 */
	final void recursivelyProcessInlineElementsUpTo(int lastLine, Consumer<InlineElement> consumer) {
		
		InlineElement lastStartElement = null;
		
		for (int i = firstInlineElement; i < numInlineElements; ++ i) {

			final InlineElement inlineElement = inlineElements[i];

			switch (inlineElement.getType()) {
			case WRAPPER_HTML_ELEMENT_START:
				if (lastStartElement != null) {
					throw new IllegalStateException("Wrapper HTML element already set");
				}

				// Remember current start-element
				lastStartElement = inlineElement;

				consumer.accept(inlineElement);

				// recurse into found element
				inlineElement. getStackElement().recursivelyProcessInlineElementsUpTo(lastLine, consumer);
				break;

			case WRAPPER_HTML_ELEMENT_END:
				consumer.accept(inlineElement);
				lastStartElement = null;
				final int lineNo = inlineElement.getLineNo() ;

				if (lineNo <= lastLine) {
					// This inline-element ended at or before the asked for last-line.
					// This means we can delete this from the current lines at this level
					this.firstInlineElement = i + 1;
				}
				break;

			case KNOWN_SIZE_HTML_ELEMENT:
				consumer.accept(inlineElement);
				break;

			case TEXT_CHUNK:
				consumer.accept(inlineElement);
				break;

			default:
				throw new UnsupportedOperationException("Unknown inline element type " + inlineElement.getType());
			}
		}
	}

	final void initBase() {

	}
	
	final void clearBase() {

		// reset array counter but keep array though to save on memory allocations
		this.numInlineElements = 0;
	}
}

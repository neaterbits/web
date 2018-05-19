package com.test.web.layout.algorithm;

import java.util.Arrays;
import java.util.function.Consumer;

import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.enums.Display;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

abstract class StackElementBase {

	// Since we can reuse stack elements on a stack, we keep track of allocation state.
	// We need to keep a nested tree of StackElement instances when processing inline elements since we cannot
	// compute layout until we have reached line wrap or start of a block element or end of current block element.
	// To avoid re-allocation of this and all subtypes (most of which will have all values re-initialiezed anyway), we re-use elements on the stack
	// but have to make sure we do not reuse if there are inline elements kept in inline-element tree
	// eg if we have <span>som text<span>sub span</span><span>another sub span</span></span>
	// the stack in LayoutState would try to reuse the StackElement for <span>sub span</span> unless we track that this
	// is referenced in the tree from the outer <span> StackElement. So for <span>another sub span</span>
	// one will have to allocate a new StackElement() (which is simpler and more efficient than implementing copy-on-write or similar)
	enum AllocationState {
		ALLOCATED,   						// Newly allocated with new operator
		CLEARED, 							// Reused, called by clear()
		IN_LAYOUT_STATE_STACK, // In layout state stack only
		IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE, // Still on layout stack but also in stack element tree of containing element (eg while processing <span>sub span</span> above)
		IN_STACK_ELEMENT_TREE, // Referenced from stack element tree so cannot be caleld clear() or init() on.
	}

	private final int stackIdx;

	private AllocationState allocationState;

	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	private final LayoutStyles layoutStyles;

	// -------------------------- For block or inline elements, ie for one inline. Also inline within inline  --------------------------
	
	// Keep track of all inline sub stack elements, this is since we cannot know the dimensioning of inline elements
	// at least until line wrap when we know the height of the tallest element.
	// At that point we can update layout of elements

	// Elements within an element, either a block or an inline element (eg a span within a div or nested spans)
	private InlineElement [] inlineElements;
	private int firstInlineElement; // while processing inline-elements, we will increase this to point to the element that is currently being processed
	private int numInlineElements; // number of inline elements in the array above, this mmight be the same as array.length in case we are reusing existing objects

	StackElementBase(int stackIdx) {
		this.stackIdx = stackIdx;
		this.layoutStyles = new LayoutStyles();

		setAllocationState(AllocationState.ALLOCATED);
	}

	private void setAllocationState(AllocationState state) {
		if (state == null) {
			throw new IllegalArgumentException("state == null");
		}
		
		if (this.allocationState == state) {
			throw new IllegalArgumentException("Setting to same state: " + state);
		}
		
		this.allocationState = state;
	}
	
	final boolean checkAndUpdateWhetherInStackElementTree() {
		final boolean mayReuse;
		
		switch (allocationState) {
		case IN_LAYOUT_STATE_STACK:
			mayReuse = true;
			break;
			
		case IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE:
			mayReuse = false;
			// From now on only in stack element tree since LayoutState will have to allocate a new StackElement instance
			setAllocationState(AllocationState.IN_STACK_ELEMENT_TREE);
			break;
			
		default:
			throw new IllegalStateException("Cannot try reuse stack elements in state " + allocationState);
		}
		
		return mayReuse;
	}
	
	// Call this to release from stack element tree, eg. after we have computed layout for the element and added it to page layer
	final void releaseFromStackElementTree() {
		switch (allocationState) {
		case IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE:
			// Still can be reused in layout state since it is still on layout state stack
			setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
			break;
			
		case IN_STACK_ELEMENT_TREE:
			// Was removed from stack so no use updating allocation state
			break;
			
		default:
			throw new IllegalStateException("Unexpected allocation state " + allocationState);
		}
	}

	/**
	 * Add inline text chunk, eg. <span>This is a text</span>
	 * Note that for a long text that spans multiple lines, this will be called multiple times
	 * as the text is wrapped over multiple lines.
	 * 
	 * @param text the text to add for
	 * @param subLayout computed layout for text
	 */
	final IElementRenderLayout addInlineTextChunk(int inlineLineNoInBlock, String text, IFont font, int width, int height, int zIndex, IDelayedRenderer renderer) {
		// Inherits the layout of this element
		final ElementLayout textChunkLayout = addTextLineElement().initTextChunk(inlineLineNoInBlock, text, font);

		textChunkLayout.getInner().initWidthHeight(width, height);
		textChunkLayout.getOuter().initWidthHeight(width, height);
		
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
	
		if (((StackElementBase)stackElement).allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Expected element to be on layout stack: " + ((StackElementBase)stackElement).allocationState);
		}

		// Now in both layout state and on stack element tree
		((StackElementBase)stackElement).setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE);
		
		// Make sure we are adding an inline-element, otherwise should not call this method
		if (!stackElement.getDisplay().isInline()) {
			throw new IllegalArgumentException("Adding inline element layout that is not inline-display : "  + stackElement.getDisplay());
		}

		addTextLineElement().initWrapperHTMLElementStart(inlineLineNoInBlock, stackElement);
	}

	final void addInlineElementEnd(StackElement stackElement, int inlineLineNoInBlock) {
		
		final AllocationState allocationState = ((StackElementBase)stackElement).allocationState;
		
		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE) {
			throw new IllegalStateException("Expected element to be on layout stack: " + allocationState);
		}

		((StackElementBase)stackElement).setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);

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
		if (allocationState != AllocationState.ALLOCATED && allocationState != AllocationState.CLEARED) {
			throw new IllegalStateException("Can only init elements that are either newly allocated or cleared");
		}

		// Now in use in layout stack
		setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
	}
	
	final void clearBase() {

		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Can only clear elements that are in use in layout stack: "  + allocationState);
		}
		
		setAllocationState(AllocationState.CLEARED);

		// reset array counter but keep array though to save on memory allocations
		this.numInlineElements = 0;

		layoutStyles.clear();
	}
	
	final boolean isViewPort() {
		return this.stackIdx == 0;
	}

	final ILayoutStylesGetters getLayoutStyles() {
		return layoutStyles;
	}

	final boolean hasUserSpecifiedWidth() {
		return layoutStyles.hasWidth();
	}

	final boolean hasUserSpecifiedHeight() {
		return layoutStyles.hasHeight();
	}

	final Display getDisplay() {
		return layoutStyles.getDisplay();
	}
	
	
	final void initCSSLayoutStyles(Consumer<LayoutStyles> computeStyles) {
		// Just calls method to compute onto layoutStyles object
		computeStyles.accept(layoutStyles);
	}
}

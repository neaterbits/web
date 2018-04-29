package com.test.web.layout.algorithm;

import java.util.Arrays;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.enums.Display;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
final class StackElement extends StackElementBaseBlock implements ContainerDimensions, SubDimensions  {
	
	// Since we can reuse stack elements on a stack, we keep track of allocation state.
	// We need to keep a nested tree of StackElement instances when processing inline elements since we cannot
	// compute layout until we have reached line wrap or start of a block element or end of current block element.
	// To avoid re-allocation of this and all subtypes (most of which will have all values re-initialiezed anyeay), we re-use elements on the stack
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
	
	static final int UNKNOWN_WIDTH = 0x01;
	static final int UNKNOWN_HEIGHT = 0x02;
	
	private AllocationState allocationState;

	private String debugName;
	
	// flags for layout dimensions that cannot be computed until we have computed dimensions of all inner-elements
	int delayedLayout;
	
	// The resulting layout after computation of width and height, this is what rendering sees
	final ElementLayout resultingLayout;

	private BaseLayoutCase layoutCase;
	
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	// -------------------------- Text specific --------------------------

	// -------------------------- For block or inline elements, ie for one inline. Also inline within inline  --------------------------

	// Max height of subelements on this line, so can sum up to block level.
	// Added to at end-tag of sub inline (or inline-block) elements
	// When a text line wraps, the baseline will be computed from this
	// Thus rendering of a line cannot happen until the whole line is processed and baseline is known

	
	// Keep track of all inline sub stack elements, this is since we cannot know the dimensioning of inline elements
	// at least until line wrap when we know the height of the tallest element.
	// At that point we can update layout of elements
	
	// elements may differ in height, and there may be other elements like images 
	// eg <span style='font-size: 12'>Some text with larger font</span>More text with default font<img ../>
	// Thus we only know the size of elements after having processed a whole line
	private InlineElement [] elementsOnThisTextLine;
	private int numElementsOnThisTextLine;


	// -------------------------- For inline elements  --------------------------
	
	// -------------------------- For block behaving elements  --------------------------
	

	StackElement(int stackIdx, int availableWidth, int availableHeight, String debugName) {
		this(stackIdx);
		
		init(availableWidth, availableHeight, debugName);
	}
	
	StackElement(int stackIdx) {
		super(stackIdx);

		this.resultingLayout = new ElementLayout();
		
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
	
	boolean checkAndUpdateWhetherInStackElementTree() {
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
	void releaseFromStackElementTree() {
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

	void clear() {
		
		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Can only clear elements that are in use in layout stack: "  + allocationState);
		}
		
		setAllocationState(AllocationState.CLEARED);
		
		layoutStyles.clear();
		resultingLayout.clear();

		// reset array counter but keep array though to save on memory allocations
		this.numElementsOnThisTextLine = 0;
	}

	void init(int availableWidth, int availableHeight, String debugName) {
		
		if (allocationState != AllocationState.ALLOCATED && allocationState != AllocationState.CLEARED) {
			throw new IllegalStateException("Can only init elements that are either newly allocated or cleared");
		}
		
		// Now in use in layout stack
		setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
		
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		if (availableHeight == 0) {
			throw new IllegalArgumentException("availableHeight == 0");
		}

		this.debugName = debugName;

		initInline();
		initBlock(availableWidth, availableHeight);
	}
	

	/**
	 * Add inline text chunk, eg. <span>This is a text</span>
	 * Note that for a long text that spans multiple lines, this will be called multiple times
	 * as the text is wrapped over multiple lines.
	 * 
	 * @param text the text to add for
	 * @param subLayout computed layout for text
	 */
	ElementLayout addInlineTextChunk(String text) {
		// Inherits the layout of this element
		final ElementLayout textChunkLayout = addTextLineElement().initTextChunk(text);

		return textChunkLayout;
	}

	/**
	 * Add inline element, eg. the image in <span>This is a text<img ...> and an image</span>
	 * 
	 * @param layout computed layout for text
	 * @param atStartOfLine true if first element or is first after line wrap
	 */

	// Add some nested inline element, like an image
	void addInlineElement(StackElement stackElement) {
	
		if (stackElement.allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Expected element to be on layout stack: " + stackElement.allocationState);
		}

		// Now in both layout state and on stack element tree
		stackElement.setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE);
		
		// Make sure we are adding an inline-element, otherwise should not call this method
		if (!stackElement.getDisplay().isInline()) {
			throw new IllegalArgumentException("Adding inline element layout that is not inline-display : "  + stackElement.getDisplay());
		}

		addTextLineElement().initHTMLElement(stackElement);
	}
	


	private InlineElement addTextLineElement() {
		final InlineElement ret;
		
		// could use ArrayList but this is much-accessed part so just use array directly
		// not that complicated anyway and isolated to this method
		if (elementsOnThisTextLine == null) {
			// Just allocate a good number, not much memory since is in a stack
			this.elementsOnThisTextLine = new InlineElement[20];
			
			ret = elementsOnThisTextLine[0] = new InlineElement();
			numElementsOnThisTextLine = 1;
		}
		else {
			if (numElementsOnThisTextLine == elementsOnThisTextLine.length) {
				// expand array
				this.elementsOnThisTextLine = Arrays.copyOf(elementsOnThisTextLine, elementsOnThisTextLine.length * 2);
			}
			
			InlineElement existing = elementsOnThisTextLine[numElementsOnThisTextLine];
			
			if (existing == null) {
				ret = elementsOnThisTextLine[numElementsOnThisTextLine] = new InlineElement();
			}
			else {
				existing.clear();
				ret = existing;
			}
		}
		
		return ret;
	}


	ILayoutStylesGetters getLayoutStyles() {
		return layoutStyles;
	}

	ElementLayout getResultingLayout() {
		return resultingLayout;
	}
	
	Display getDisplay() {
		return layoutStyles.getDisplay();
	}

	boolean hasUserSpecifiedWidth() {
		return layoutStyles.hasWidth();
	}

	boolean hasUserSpecifiedHeight() {
		return layoutStyles.hasHeight();
	}

	BaseLayoutCase getLayoutCase() {
		return layoutCase;
	}

	String getLayoutCaseName() {
		return layoutCase.getName();
	}

	void setLayoutCase(BaseLayoutCase layoutCase) {
		this.layoutCase = layoutCase;
	}
}


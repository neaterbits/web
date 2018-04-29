package com.test.web.layout.algorithm;

import java.util.Arrays;

import com.test.web.layout.common.IBounds;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.enums.Display;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
final class StackElement implements ContainerDimensions, SubDimensions  {
	
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
	
	private final int stackIdx;
	
	private AllocationState allocationState;

	private String debugName;
	
	// flags for layout dimensions that cannot be computed until we have computed dimensions of all inner-elements
	int delayedLayout;
	
	// available width and height at this level, ie. the max dimension that the element at this level can have
	// width is given by the viewport at the window level mainly for width but also for height if specified as 100%
	private int availableWidth;
	private int availableHeight;

	// remaining width and height when adding elements
	// this so that we can find the size of elements that do not have these specified
	private int remainingWidth; // Always known since we know the width of the viewport? What about overflow:scroll?
	private int remainingHeight;
	
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	final LayoutStyles layoutStyles;
	
	// The resulting layout after computation of width and height, this is what rendering sees
	final ElementLayout resultingLayout;

	private BaseLayoutCase layoutCase;
	
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	// -------------------------- Text specific --------------------------

	// -------------------------- For block or inline elements, ie for one inline. Also inline within inline  --------------------------

	private boolean inlineElementsAdded;
	
	// Sum of width of subelements on this line, so can sum up to block level.Useful when block width not set
	// Added to at end-tag of sub inline (or inline-block) elements
	private int curInlineWidth;
	
	// Max width for inline element, eg. if line did not wrap this will be length of text on first line, otherwise
	// will be length of longest line within wrapped text
	private int inlineMaxWidth;

	// Max height of subelements on this line, so can sum up to block level.
	// Added to at end-tag of sub inline (or inline-block) elements
	// When a text line wraps, the baseline will be computed from this
	// Thus rendering of a line cannot happen until the whole line is processed and baseline is known

	// So when at end tag of a inline element and we are certain of the height of that element, we will call on the container to see if > current max height.
	// Only done when sum element is an inline or inline-block element
	private int curInlineMaxHeight;
	
	// Height of this inline element, summarizing all text lines' max height
	private int inlineHeight;
	private boolean totalInlineHeightComputed; // For checking that we only compute total once for each element
	
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

	// Current height of block element (if this is one). This is the sum of all block elements within this block.
	// This is used in the case where height for blocks is not set in CSS, eg. for nested divs we just summarize
	// the height of all sub divs to find the height of container div.
	// Note as seen above, we do not do that for widths because that is only needed for inline elements within a block
	
	// Unrelated to height specified by CSS, that would have to be handled by overflow attribute.
	private int curBlockElementHeight;
	

	StackElement(int stackIdx, int availableWidth, int availableHeight, String debugName) {
		this(stackIdx);
		
		init(availableWidth, availableHeight, debugName);
	}
	
	StackElement(int stackIdx) {
		this.stackIdx = stackIdx;
		this.layoutStyles = new LayoutStyles();
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

	boolean isViewPort() {
		return this.stackIdx == 0;
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
		
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;
		
		this.debugName = debugName;

		this.remainingWidth = availableWidth;
		this.remainingHeight = availableHeight;

		this.inlineElementsAdded = false;
		this.curInlineWidth = 0;
		this.inlineMaxWidth = 0;
		this.curInlineMaxHeight = 0;
		this.inlineHeight = 0;
		this.totalInlineHeightComputed = false;
		this.inlineElementsAdded = false;
		this.curBlockElementHeight = 0;
	}
	
	boolean hasAnyInlineElementsAdded() {
		return inlineElementsAdded;
	}

	@Override
	public int getLineStartXPos() {
		// TODO take margin of current element into account
		final int leftMargin = 0;

		return leftMargin;
	}
	
	int getCurLineXPos() {
		// Current inline x pos, computed from cur inline width
		// Note that if this is the first textline, it is 0
		
		return getLineStartXPos() + (inlineElementsAdded ? this.curInlineWidth : 0);
	}

	int getCurLineYPos() {
		// Current inline y pos, computed from cur inline height
		
		// TODO margins etc
		
		return inlineHeight;
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
	

	// Computation of inline height, typically happens on reaching end tag
	int computeInlineHeightAndClearInlineData() {

		// Verify that not already computed, since we are adding last max-value to current
		if (totalInlineHeightComputed) {
			throw new IllegalStateException("Already computed inline height");
		}
		
		this.totalInlineHeightComputed = true;
		this.inlineElementsAdded = false;

		addToInlineHeight();
		
		return inlineHeight;
	}
	
	
	@Override
	public int getInlineContentMaxWidth() {
		checkIsInlineElement();

		return inlineMaxWidth;
	}

	@Override
	public int getInlineContentHeight() {
		checkIsInlineElement();

		if (!totalInlineHeightComputed) {
			throw new IllegalStateException("inline content height not computed");
		}
		
		return inlineHeight;
	}

	private void updateInlineInfo(ElementLayout subLayout, boolean atStartOfLine) {

		final IBounds bounds = subLayout.getOuterBounds();

		this.inlineElementsAdded = true;

		// Add to current inline-width
		if (atStartOfLine) {
			this.curInlineWidth = bounds.getWidth();
		}
		else {
			// Appended to end of line without line wrapping
			// Eg if nested span elements or an image within text
			this.curInlineWidth += bounds.getWidth();
			
			if (this.curInlineWidth > availableWidth) {
				throw new IllegalStateException();
			}
		}

		// Inline max width must be increased if this line was longer than existing
		if (curInlineWidth > inlineMaxWidth) {
			this.inlineMaxWidth = curInlineWidth;
		}

		// If at start of line, add max height for last line.
		if (atStartOfLine) {
			addToInlineHeight();
		}
		
		if (bounds.getHeight() > this.curInlineMaxHeight) {
			// Tallest element on this line, update max height
			this.curInlineMaxHeight = bounds.getHeight();
		}
	}
	
	private void addToInlineHeight() {
		// If this is first line, then curInlineMaxHeight ought to be 0
		// TODO must add line spacing, if so this might now work, could keep a counter of number of text lines
		
		this.inlineHeight += curInlineMaxHeight;
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

	private void checkIsInlineElement() {
		if (isViewPort() || !this.getLayoutStyles().getDisplay().isInline()) {
			throw new IllegalStateException("Current element is not an inline element");
		}
	}

	private void checkIsBlockElement() {
		if (!isViewPort() && !this.getLayoutStyles().getDisplay().isBlock()) {
			throw new IllegalStateException("Current element is not a block element");
		}
	}
	
	boolean updateBlockRemainingForNewInlineElement(int widthPx, int heightPx) {
		
		if (widthPx <= 0) {
			throw new IllegalArgumentException("widthPx <= 0");
		}

		if (heightPx <= 0) {
			throw new IllegalArgumentException("heightPx <= 0");
		}
		
		checkIsBlockElement();

		// Compute any horizontal margins
		final boolean lineWrapped = widthPx > remainingWidth;

		updateBlockInlineRemainingWidth(widthPx, lineWrapped);
		
		return lineWrapped;
	}
	
	void updateBlockInlineRemainingWidth(int widthPx, boolean applyLineWrap) {
		if (applyLineWrap) {
			// TODO overflow
			this.remainingWidth = getAvailableWidth();
		}
		else {
			this.remainingWidth -= widthPx;
		}
	}

	void addToBlockElementHeight(int height) {
		checkIsBlockElement();

		this.curBlockElementHeight += height;
	}
	
	@Override
	public int getCurBlockYPos() {
		// TODO margin

		checkIsBlockElement();
		
		return getCollectedBlockHeight();
	}

	@Override
	public int getCollectedBlockHeight() {
		checkIsBlockElement();

		return curBlockElementHeight;
	}
	
	@Override
	public int getAvailableWidth() {
		return availableWidth;
	}

	void setAvailableWidth(int availableWidth) {
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		this.availableWidth = availableWidth;
	}

	@Override
	public int getAvailableHeight() {
		return availableHeight;
	}

	void setAvailableHeight(int availableHeight) {
		this.availableHeight = availableHeight;
	}
	
	@Override
	public int getRemainingWidth() {
		return remainingWidth;
	}

	void setRemainingWidth(int remainingWidth) {
		this.remainingWidth = remainingWidth;
	}

	@Override
	public int getRemainingHeight() {
		return remainingHeight;
	}

	void setRemainingHeight(int remainingHeight) {
		this.remainingHeight = remainingHeight;
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


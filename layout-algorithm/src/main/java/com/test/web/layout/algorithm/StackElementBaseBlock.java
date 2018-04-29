package com.test.web.layout.algorithm;

// Contains all elements for block behaving elements
// Note that subclasses that for inline. we reuse
// StackElement class for all elements so that we can reuse objects and all sub-objects
abstract class StackElementBaseBlock extends StackElementBaseInline implements ContainerDimensions, SubDimensions {

	// Current height of block element (if this is one). This is the sum of all block elements within this block.
	// This is used in the case where height for blocks is not set in CSS, eg. for nested divs we just summarize
	// the height of all sub divs to find the height of container div.
	// Note as seen above, we do not do that for widths because that is only needed for inline elements within a block

	// Unrelated to height specified by CSS, that would have to be handled by overflow attribute.
	private int curBlockElementHeight;
	
	
	// available width and height at this level, ie. the max dimension that the element at this level can have
	// width is given by the viewport at the window level mainly for width but also for height if specified as 100%
	private int availableWidth;
	private int availableHeight;

	// remaining width and height when adding elements
	// this so that we can find the size of elements that do not have these specified
	private int remainingWidth; // Always known since we know the width of the viewport? What about overflow:scroll?
	private int remainingHeight;

	// Sum of width of subelements on this line, so can sum up to block level.Useful when block width not set
	// Added to at end-tag of sub inline (or inline-block) elements
	private int curInlineWidth;
	
	// Whether any inline elements have been added to this block element at all
	// Cached value, could figure by looking at element tree
	private boolean inlineElementsAdded;
	
	// Keeping track of total height of consecutive inline lines in block element (eg. lines of text)
	private boolean totalConsecutiveInlineHeightComputed; // For checking that we only compute total consecutive once for each element
	private int totalConsecutiveInlineHeight; // Height of all inline elements occuring in sequence within a block element until terminated by a block element or end tag for this block element
		
	// So when at end tag of a inline element and we are certain of the height of that element, we will call on the container to see if > current max height.
	// Only done when sum element is an inline or inline-block element
	private int curInlineMaxHeight;
	
	// number of elements on current line (regarded whether are nested)
	// TODO perhaps get from tree?
	private int numElementsOnLine;
	
	// current inline line no within block
	private int curLineNoInBlock;

	StackElementBaseBlock(int stackIdx) {
		super(stackIdx);
	}

	final void initBlock(int availableWidth, int availableHeight) {

		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;

		this.remainingWidth = availableWidth;
		this.remainingHeight = availableHeight;

		this.curBlockElementHeight = 0;

		this.curInlineWidth = 0;
		this.inlineElementsAdded = false;
		this.totalConsecutiveInlineHeightComputed = false;
		this.curInlineMaxHeight = 0;
		this.totalConsecutiveInlineHeight = 0;
		this.curLineNoInBlock = 0;
		this.numElementsOnLine = 0;
	}

	// Computation of inline height, typically happens on reaching end tag
	int computeInlineHeightAndClearInlineData() {

		// Verify that not already computed, since we are adding last max-value to current
		if (totalConsecutiveInlineHeightComputed) {
			throw new IllegalStateException("Already computed inline height");
		}
		
		this.totalConsecutiveInlineHeightComputed = true;
		this.inlineElementsAdded = false;

		addToCurLineMaxHeightToTotalConsecutiveInlineHeight();
		
		return totalConsecutiveInlineHeight;
	}
	
	
	private void addToCurLineMaxHeightToTotalConsecutiveInlineHeight() {
		// If this is first line, then curInlineMaxHeight ought to be 0
		// TODO must add line spacing, if so this might now work, could keep a counter of number of text lines
		
		this.totalConsecutiveInlineHeight += curInlineMaxHeight;
	}

	
	final boolean hasAnyInlineElementsAdded() {
		return inlineElementsAdded;
	}

	final void addToBlockElementHeight(int height) {
		checkIsBlockElement();

		this.curBlockElementHeight += height;
	}

	@Override
	public final int getInlineContentHeight() {

		if (!totalConsecutiveInlineHeightComputed) {
			throw new IllegalStateException("inline content height not computed");
		}
		
		return totalConsecutiveInlineHeight;
	}

	@Override
	public final int getCurBlockYPos() {
		// TODO margin

		checkIsBlockElement();
		
		return getCollectedBlockHeight();
	}

	@Override
	public final int getCollectedBlockHeight() {
		checkIsBlockElement();

		return curBlockElementHeight;
	}

	private void checkIsBlockElement() {
		if (!isViewPort() && !layoutStyles.getDisplay().isBlock()) {
			throw new IllegalStateException("Current element is not a block element");
		}
	}
	
	final boolean updateBlockRemainingForNewInlineElement(int widthPx, int heightPx) {
		
		if (widthPx <= 0) {
			throw new IllegalArgumentException("widthPx <= 0");
		}

		if (heightPx <= 0) {
			throw new IllegalArgumentException("heightPx <= 0");
		}
		
		checkIsBlockElement();

		// Compute any horizontal margins
		final boolean lineWrapped = widthPx > remainingWidth;

		
		if (lineWrapped) {
			// Must wrap line since no more space on this one
			applyLineBreak();
		}
		
		// then update
		updateCurrentLine(widthPx, heightPx);
		
		return lineWrapped;
	}

	final void updateBlockInlineRemainingWidthForTextElement(int widthPx, int heightPx, boolean applyLineWrap) {
		
		// Add to current widths
		updateCurrentLine(widthPx, heightPx);

		if (applyLineWrap) {
			// Add last line max-height to current
			applyLineBreak();
		}
	}
	
	private void applyLineBreak() {
		addToCurLineMaxHeightToTotalConsecutiveInlineHeight();
		this.numElementsOnLine = 0;
		++ curLineNoInBlock;
		this.curInlineWidth = 0;
		this.remainingWidth = getAvailableWidth();
	}
	
	private void updateCurrentLine(int widthPx, int heightPx) {
		this.curInlineWidth += widthPx;
		
		if (this.curInlineWidth > availableWidth) {
			throw new IllegalStateException();
		}
		this.remainingWidth -= widthPx;
		
		++ numElementsOnLine;
		
		if (heightPx > curInlineMaxHeight) {
			this.curInlineMaxHeight = heightPx;
		}
	}


	final int getCurInlineLineNoInBlock() {
		return curLineNoInBlock;
	}
	
	@Override
	public final int getLineStartXPos() {
		// TODO take margin of current element into account
		final int leftMargin = 0;

		return leftMargin;
	}
	
	final int getCurLineXPos() {
		// Current inline x pos, computed from cur inline width
		// Note that if this is the first textline, it is 0
		
		return getLineStartXPos() + (inlineElementsAdded ? this.curInlineWidth : 0);
	}

	final int getCurLineYPos() {
		// Current inline y pos, computed from cur inline height
		
		// TODO margins etc
		
		return totalConsecutiveInlineHeight;
	}
	
	@Override
	public final int getAvailableWidth() {
		return availableWidth;
	}

	final void setAvailableWidth(int availableWidth) {
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		this.availableWidth = availableWidth;
	}

	@Override
	public final int getAvailableHeight() {
		return availableHeight;
	}

	final void setAvailableHeight(int availableHeight) {
		this.availableHeight = availableHeight;
	}
	
	@Override
	public final int getRemainingWidth() {
		return remainingWidth;
	}

	final void setRemainingWidth(int remainingWidth) {
		this.remainingWidth = remainingWidth;
	}

	@Override
	public final int getRemainingHeight() {
		return remainingHeight;
	}

	final void setRemainingHeight(int remainingHeight) {
		this.remainingHeight = remainingHeight;
	}
}

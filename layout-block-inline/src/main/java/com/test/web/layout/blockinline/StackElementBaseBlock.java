package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.ElementLayoutSettersGetters;
import com.test.web.layout.algorithm.SubDimensions;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.render.common.IFont;

// Contains all elements for block behaving elements
// Note that subclasses that for inline. we reuse
// StackElement class for all elements so that we can reuse objects and all sub-objects
abstract class StackElementBaseBlock<ELEMENT> extends StackElementBaseInline<ELEMENT> implements ContainerDimensions, SubDimensions {

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
	
	// Keeping track of total height of consecutive inline lines in block element (eg. lines of text)
	private boolean totalConsecutiveInlineHeightComputed; // For checking that we only compute total consecutive once for each element
	private int totalConsecutiveInlineHeight; // Height of all inline elements occuring in sequence within a block element until terminated by a block element or end tag for this block element
		
	// Max height of subelements on this line, so can sum up to block level.
	// Added to at end-tag of sub inline (or inline-block) elements
	// When a text line wraps, the baseline will be computed from this
	// Thus rendering of a line cannot happen until the whole line is processed and baseline is known
	// So when at end tag of a inline element and we are certain of the height of that element, we will call on the container to see if > current max height.
	// Only done when sum element is an inline or inline-block element
	private int curInlineMaxHeight;
	
	// current inline line no within block
	private int curLineNoInBlock;
	
	// Temp object for value result
	private WrapperSums wrapperSums;

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
		this.totalConsecutiveInlineHeightComputed = false;
		this.curInlineMaxHeight = 0;
		this.totalConsecutiveInlineHeight = 0;
		this.curLineNoInBlock = 0;
	}

	// Computation of inline height, typically happens on reaching end tag
	int computeInlineHeightAndClearInlineData() {

		// Verify that not already computed, since we are adding last max-value to current
		if (totalConsecutiveInlineHeightComputed) {
			throw new IllegalStateException("Already computed inline height");
		}
		
		this.totalConsecutiveInlineHeightComputed = true;

		addToCurLineMaxHeightToTotalConsecutiveInlineHeight();
		
		return totalConsecutiveInlineHeight;
	}
	
	
	private void addToCurLineMaxHeightToTotalConsecutiveInlineHeight() {
		// If this is first line, then curInlineMaxHeight ought to be 0
		// TODO must add line spacing, if so this might now work, could keep a counter of number of text lines
		
		this.totalConsecutiveInlineHeight += curInlineMaxHeight;
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
		if (!isViewPort() && !getDisplay().isBlock()) {
			throw new IllegalStateException("Current element is not a block element");
		}
	}
	
	final <ELEMENT_TYPE> boolean updateBlockRemainingForNewInlineElement(int widthPx, int heightPx, int debugDepth, ILayoutDebugListener<ELEMENT_TYPE> debugListener, AddToPageLayer<ELEMENT> addToPageLayer) {
		
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
			applyLineBreak(debugDepth, debugListener, addToPageLayer);
		}
		
		// then update
		updateCurrentLine(widthPx, heightPx);
		
		return lineWrapped;
	}

	final <ELEMENT_TYPE> void updateBlockInlineRemainingWidthForTextElement(int widthPx, int heightPx, boolean applyLineWrap, int debugDepth, ILayoutDebugListener<ELEMENT_TYPE> layoutDebugListener, AddToPageLayer<ELEMENT> addToPageLayer) {
		
		checkIsBlockElement();
		
		// Add to current widths
		updateCurrentLine(widthPx, heightPx);

		if (applyLineWrap) {
			// Add last line max-height to current
			applyLineBreak(debugDepth, layoutDebugListener, addToPageLayer);
		}
	}
	
	
	// Call this when reaching end of current div-element so we can compute sizes for the last inline text line
	final <ELEMENT_TYPE> void applyLineBreakAtEndOfBlockElement(int debugDepth, ILayoutDebugListener<ELEMENT_TYPE> layoutDebugListener, AddToPageLayer<ELEMENT> addToPageLayer) {

		checkIsBlockElement();
		
		applyLineBreak(debugDepth, layoutDebugListener, addToPageLayer);
	}
	
	private <ELEMENT_TYPE> void applyLineBreak(int debugDepth, ILayoutDebugListener<ELEMENT_TYPE> layoutDebugListener, AddToPageLayer<ELEMENT> addToPageLayer) {
		
		checkIsBlockElement();
		
		layoutDebugListener.onApplyLineBreakStart(debugDepth, curLineNoInBlock);
		
		addToCurLineMaxHeightToTotalConsecutiveInlineHeight();
		
		this.curInlineWidth = 0;
		this.remainingWidth = getAvailableWidth();

		// Main calculation for inline elements happen at line-break, we recursively scan tree of inline elements
		// and compute elements

		// recursivelyCheckForAnyCompletedInlineElementAndComputeBounds(this, curLineNoInBlock, curInlineMaxHeight, debugDepth, layoutDebugListener);
		
		if (this.wrapperSums == null) {
			this.wrapperSums = new WrapperSums();
		}
		
		recursivelyProcessInlineElementsUpTo(this, curLineNoInBlock, curInlineMaxHeight, addToPageLayer, wrapperSums);

		this.curInlineMaxHeight = 0;
		++ curLineNoInBlock;

		layoutDebugListener.onApplyLineBreakEnd(debugDepth);
	}
	
	// For summarizing position and sizes of wrapper element and returning upwards in tree.
	// Note, this is passed in so to avoid allocating a return-object
	private static class WrapperSums {
		private int maxWidth; // max width for all lines this wrapper element spans across
		private int sumHeight; // sum height for all lines this wrapper element spans across
		private int leftmostX; // leftmost position of wrapper element. This is always at start of first non-wrapper element on a line if wraps
		private int topmostY;
		
		void init(int maxWidth, int sumHeight, int leftmostX, int topmostY) {
			this.maxWidth = maxWidth;
			this.sumHeight = sumHeight;
			this.leftmostX = leftmostX;
			this.topmostY = topmostY;
		}
	}

	// Recurses all inline-elements under this block-element that we have buffered during parsing, advancing until we have processed current line no.
	// For any wrapper element like <span></span> that ends on this line, we advance firstInlineElement index past it
	// so that we do not loop through that the next time we run this algorithm
	
	static <E> void recursivelyProcessInlineElementsUpTo(StackElementBase<E> cur, int lineToProcess, int lineMaxHeight, AddToPageLayer<E> addToPageLayer, WrapperSums wrapperSums) {

		// continue, take into account margin-left and margin-right of inline wrapper elements, these are added before or after line
		int widthForCurrentWrapperElement = -1;
		int heightForCurrentWrapperElement = -1;
		int leftmostXForCurrentWrapperElement = -1;
		int topmostYForCurrentWrapperElement = -1;

		// sum-width for cur line, for figuring width of current line
		int sumWidthForCurLine = 0;
		
		// Max height for text and non-wrapper elements on current line, this is reset whenever we move to a new line
		int maxHeightForCurLine = 0;
		
		
		int currentLineNo = -1;
		
		// max-width, returned in order to compute max-width of wrapper elements
		int maxWidth = 0;

		// sum-height, returned in order to compute height of wrapper elements
		int sumHeight = 0;
		
		int leftmostX = Integer.MAX_VALUE;
		int topmostY = -1;

		for (int i = cur.getFirstInlineElement(); i < cur.getNumInlineElements(); ++ i) {
			
			final InlineElement inlineElement = cur.getInlineElementAt(i);

			final int lineNo = inlineElement.getLineNo() ;

			if (lineNo > currentLineNo) {
				// Moved to a new line, add max height to sum
				sumHeight += maxHeightForCurLine;

				if (sumWidthForCurLine > maxWidth) {
					maxWidth = sumWidthForCurLine;
				}

				sumWidthForCurLine = 0;
				maxHeightForCurLine = 0;
				currentLineNo = lineNo;
			}
			
			// height of element if this is a non-wrapper element
			final int nonWrapperElementWidth;
			final int nonWrapperElementHeight;
			final int nonWrapperElementX;
			final int nonWrapperElementY;
			
			switch (inlineElement.getType()) {
			case WRAPPER_HTML_ELEMENT_START: {
				
				// recurse into found element, return width found for current line to process
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final StackElement<E> sub = (StackElement)inlineElement. getStackElement();
			
				recursivelyProcessInlineElementsUpTo(sub, lineToProcess, lineMaxHeight, addToPageLayer, wrapperSums);
				
				// Sum of width for lineToProcess. Cache value so that temp object for returning values can be reused
				widthForCurrentWrapperElement = wrapperSums.maxWidth;
				heightForCurrentWrapperElement  = wrapperSums.sumHeight;
				leftmostXForCurrentWrapperElement = wrapperSums.leftmostX;
				topmostYForCurrentWrapperElement = wrapperSums.topmostY;

				// if width found is more than current sum, then add to width for span element.
				// this is done so that if spans multiple lines, we will find the largest width within the span
				// so that we find bounding-box
				
				nonWrapperElementWidth = 0; // this is a wrapper element
				nonWrapperElementHeight = 0; // this is a wrapper element
				nonWrapperElementX = Integer.MAX_VALUE; // this is a wrapper element
				nonWrapperElementY = -1; // this is a wrapper element
				break;
			}

			case WRAPPER_HTML_ELEMENT_END:

				if (lineNo <= lineToProcess) {
					final ElementLayoutSettersGetters layout = inlineElement.getResultingLayout();

					// This inline-element ended at or before the asked for last-line.
					// This means we can delete this from the current lines at this level
					if (i < cur.getNumInlineElements() - 1) {
						cur.updateFirstInlineElement(i + 1);
					}

					// Set sum height for element that we cached from element-start
					layout.initOuterWidthHeight(widthForCurrentWrapperElement, heightForCurrentWrapperElement);
					layout.initInnerWidthHeight(widthForCurrentWrapperElement, heightForCurrentWrapperElement);
					
					layout.initOuterPosition(leftmostXForCurrentWrapperElement, topmostYForCurrentWrapperElement);
					layout.initInnerPosition(leftmostXForCurrentWrapperElement, topmostYForCurrentWrapperElement);

					@SuppressWarnings("unchecked")
					final E element = (E)inlineElement.getStackElement().getElement();

					layout.setBoundsComputed();
					addToPageLayer.add(element, layout);

					// release since no longer needed
					inlineElement.getStackElement().removedFromSeparateTree();
				}

				nonWrapperElementWidth = 0; // this is a wrapper element
				nonWrapperElementHeight = 0; // this is a wrapper element
				nonWrapperElementX = Integer.MAX_VALUE; // this is a wrapper element
				nonWrapperElementY = -1; // this is a wrapper element
				break;

			case KNOWN_SIZE_HTML_ELEMENT:
				// <img> or similar, we know width and height for this element so nothing in particular to be done
				final IElementLayout layout = inlineElement.getResultingLayout();

				nonWrapperElementWidth = layout.getOuterBounds().getWidth();
				nonWrapperElementHeight = layout.getOuterBounds().getHeight();
				nonWrapperElementX = layout.getOuterBounds().getLeft();
				nonWrapperElementY = layout.getOuterBounds().getTop();
				break;

			case TEXT_CHUNK:
				
				final ElementLayoutSettersGetters textChunkLayout = inlineElement.getResultingLayout();

				if (inlineElement.getLineNo() == lineToProcess) {
					// a text chunk, these are always rectangular in size.
					// here we have to compute the positioning of the chunk since size was computed earlier
					
					// TODO might not be correct since we do not know line height due to other inline elements on this line
					
					final int yPos = getTextYPosOnLine(textChunkLayout.getFont(), lineMaxHeight);
					
					
					textChunkLayout.initOuterPosition(textChunkLayout.getOuterBounds().getLeft(), yPos);
					textChunkLayout.initInnerPosition(textChunkLayout.getInnerBounds().getLeft(), yPos);
					
					textChunkLayout.setBoundsComputed();
				}

				else if (inlineElement.getLineNo() < lineToProcess && !textChunkLayout.areBoundsComputed()) {
					throw new IllegalStateException("Previous line not computed");
				}
				nonWrapperElementWidth = textChunkLayout.getOuterBounds().getWidth();
				nonWrapperElementHeight = textChunkLayout.getOuterBounds().getHeight();
				nonWrapperElementX = textChunkLayout.getOuterBounds().getLeft();
				nonWrapperElementY = textChunkLayout.getOuterBounds().getTop();
				break;

			default:
				throw new UnsupportedOperationException("Unknown inline element type " + inlineElement.getType());
			}

			if (nonWrapperElementHeight > maxHeightForCurLine) {
				maxHeightForCurLine = nonWrapperElementHeight;
			}

			sumWidthForCurLine += nonWrapperElementWidth;
			
			if (nonWrapperElementX < leftmostX) {
				leftmostX = nonWrapperElementX;
			}
			
			if (nonWrapperElementY >= 0 && topmostY < 0) {
				topmostY = nonWrapperElementY;
			}
		}

		// Any leftover in completion of current line
		if (sumWidthForCurLine > maxWidth) {
			maxWidth = sumWidthForCurLine;
		}
		sumHeight += maxHeightForCurLine;

		wrapperSums.init(maxWidth, sumHeight, leftmostX, topmostY);
	}

	private static int getTextYPosOnLine(IFont font, int lineMaxHeight) {
		// TODO align text to baseline
		return lineMaxHeight - font.getHeight();
	}

	private void updateCurrentLine(int widthPx, int heightPx) {
		this.curInlineWidth += widthPx;
		
		if (this.curInlineWidth > availableWidth) {
			throw new IllegalStateException();
		}

		this.remainingWidth -= widthPx;
		
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
		
		return getLineStartXPos() + (hasAnyInlineElementsAdded() ? this.curInlineWidth : 0);
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

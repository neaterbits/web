package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.ElementLayoutSettersGetters;
import com.test.web.layout.algorithm.SubDimensions;
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
		
		recursivelyProcessInlineElementsUpTo(this, curLineNoInBlock, curInlineMaxHeight, addToPageLayer);

		this.curInlineMaxHeight = 0;
		++ curLineNoInBlock;

		layoutDebugListener.onApplyLineBreakEnd(debugDepth);
	}

	// Recurses all inline-elements under this block-element that we have buffered during parsing, advancing until we have proessed current line no.
	// For any wrapper element like <span></span> that ends on this line, we advance firstInlineElement index past it
	// so that we do not loop through that the next time we run this algorithm
	
	static <E> long recursivelyProcessInlineElementsUpTo(StackElementBase<E> cur, int lineToProcess, int lineMaxHeight, AddToPageLayer<E> addToPageLayer) {

		int widthForLineToProcess = 0;
		int widthForCurrentWrapperElement = -1;
		int heightForCurrentWrapperElement = -1;

		InlineElement lastStartElement;
		
		// Keep track of width found at start-element, so we can update in end-element
		int widthForLineInStartElement = 0;
		
		// Max height for text and non-wrapper elements on current line, this is reset whenever we move to a new line
		int maxHeightForCurLine = 0;
		
		int currentLineNo = -1;
		
		// sum-height, returned in order to compute heigher of wrapper elements
		int sumHeight = 0;

		for (int i = cur.getFirstInlineElement(); i < cur.getNumInlineElements(); ++ i) {

			
			final InlineElement inlineElement = cur.getInlineElementAt(i);

			final int lineNo = inlineElement.getLineNo() ;

			if (lineNo > currentLineNo) {
				// Moved to a new line, add max height to sum
				sumHeight += maxHeightForCurLine;
				maxHeightForCurLine = 0;
				currentLineNo = lineNo;
			}
			
			// height of element if this is a non-wrapper element
			final int nonWrapperElementHeight;
			
			switch (inlineElement.getType()) {
			case WRAPPER_HTML_ELEMENT_START: {
				
				// recurse into found element, return width found for current line to process
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final StackElement<E> sub = (StackElement)inlineElement. getStackElement();
			
				final long widthAndHeight = recursivelyProcessInlineElementsUpTo(sub, lineToProcess, lineMaxHeight, addToPageLayer);
				
				// Sum of width for lineToProcess
				final int widthForLine = (int)((widthAndHeight >> 32) & 0xFFFFFFFFL);
				
				final int sumHeightForSub = (int)(widthAndHeight & 0xFFFFFFFFL);

				heightForCurrentWrapperElement = sumHeightForSub;

				final ElementLayoutSettersGetters layout = inlineElement.getResultingLayout();
				
				lastStartElement = inlineElement;
				widthForLineInStartElement = widthForLine; // Need this if this is last line, eg. </span> so that we may return width for this element on current line

				// if width found is more than current sum, then add to width for span element.
				// this is done so that if spans multiple lines, we will find the largest width within the span
				// so that we find bounding-box
				
				if (widthForLine > layout.getOuterBounds().getWidth()) {
					// TODO take margin and padding into account?
					layout.initOuterWidthHeight(widthForLine, layout.getOuterBounds().getHeight());
					layout.initInnerWidthHeight(widthForLine, layout.getInnerBounds().getHeight());
				}

				widthForLineToProcess = widthForLine;

				nonWrapperElementHeight = 0; // this is a wrapper element
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
					layout.initOuterWidthHeight(layout.getOuterBounds().getWidth(), heightForCurrentWrapperElement);
					layout.initInnerWidthHeight(layout.getInnerBounds().getWidth(), heightForCurrentWrapperElement);

					@SuppressWarnings("unchecked")
					final E element = (E)inlineElement.getStackElement().getElement();
					layout.setBoundsComputed();
					addToPageLayer.add(element, layout);
					
					// release since no longer needed
					inlineElement.getStackElement().removedFromSeparateTree();
				}
				
				if (lineNo == lineToProcess) {
					// return width for all element in this wrapper that appears on this line only
					widthForLineToProcess += widthForLineInStartElement;
				}
				nonWrapperElementHeight = 0; // this is a wrapper element
				break;

			case KNOWN_SIZE_HTML_ELEMENT:
				// <img> or similar, we know width and height for this element so nothing in particular to be done
				
				if (inlineElement.getLineNo() == lineToProcess) {
					widthForLineToProcess += inlineElement.getResultingLayout().getOuterBounds().getWidth();
				}

				nonWrapperElementHeight = inlineElement.getResultingLayout().getOuterBounds().getHeight();
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
					
					// Must add this to layers for this element

					widthForLineToProcess += textChunkLayout.getOuterBounds().getWidth();
				}

				else if (inlineElement.getLineNo() < lineToProcess && !textChunkLayout.areBoundsComputed()) {
					throw new IllegalStateException("Previous line not computed");
				}
				nonWrapperElementHeight = textChunkLayout.getOuterBounds().getHeight();
				break;

			default:
				throw new UnsupportedOperationException("Unknown inline element type " + inlineElement.getType());
			}

			if (nonWrapperElementHeight > maxHeightForCurLine) {
				maxHeightForCurLine = nonWrapperElementHeight;
			}
		}

		// Any leftover in completion of current line
		sumHeight += maxHeightForCurLine;

		// return in a long-var so that no need for allocation
		return ((long)widthForLineToProcess) << 32 | sumHeight;
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

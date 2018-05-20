package com.test.web.layout.blockinline;

import java.util.function.BiConsumer;

import com.test.web.layout.algorithm.BaseLayoutCase;
import com.test.web.layout.algorithm.ElementLayoutSetters;
import com.test.web.layout.algorithm.LayoutHelperUnits;
import com.test.web.layout.algorithm.TextUtil;
import com.test.web.layout.algorithm.TextUtil.NumberOfChars;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.render.common.IFont;

abstract class BaseBlockInlineLayoutCase<ELEMENT> extends BaseLayoutCase<ELEMENT, StackElement<ELEMENT>, BlockInlineLayoutUpdate<ELEMENT>> {

	@Override
	protected final void onText(StackElement<ELEMENT> container, String text, TextUtil textUtil,  BlockInlineLayoutUpdate<ELEMENT> state, GetRenderer getRenderer, BiConsumer<String, IElementRenderLayout> chunkListener) {
		computeAndAddInlineText_wrapAndRenderAsNecessary_textUtil(container, text, textUtil, state, getRenderer, chunkListener);
	}

    // Renders text in multiple text elements.
    // It has to be multiple since we can add more text on the last line of the wrapping text if there is room for it
    // eg. <span>This is some text that wraps</span><span>This is more text</span> could result in the latter span text result on the same line of the first span, wven if the first span wrapped.
    // We need to compute layout for each element, if the display view is resized, we will recompute new elements.
    // Thus the layouted text elements are not necessarily corresponding to the HTML text elements. 
    

    private void computeAndAddInlineText_wrapAndRenderAsNecessary_textUtil(StackElement<ELEMENT> cur, String text,
    				TextUtil textUtil, BlockInlineLayoutUpdate<ELEMENT> state, GetRenderer getRenderer, BiConsumer<String, IElementRenderLayout> chunkListener) {

		final IFont font = cur.resultingLayout.getFont();


		// Find start position of text within container element
		// Container element is either a block element or an inline element but either way we will set xPos to the current xPos withib that element.
		// This might be multiple lines down in the element and in the middle of a line, ie. if text is wrapping.

		// TODO must reflect any inline elements
		// Can only be at start of line if no elements have been added, we will detect wrapping further down
		boolean atStartOfLineInitial = !cur.hasAnyInlineElementsAdded();
		final int lineHeight = font.getHeight();

		textUtil.splitTextIntoLines(
				text,
				cur.getCurLineXPos(),  cur.getCurLineYPos(), // current x and y pos inline element
				cur.getLineStartXPos(), // x pos of position of at new line
				font,
				atStartOfLineInitial,
				() -> state.getInlineRemainingWidth(), // return remaining width of current element
				(lineText, numChars, x, y, lineWrapped, atStartOfLine) -> {
					// will update current remaining width
					processOneTextElement(lineText, cur, numChars, x, y, lineHeight, lineWrapped, atStartOfLineInitial, state, getRenderer, chunkListener);
					
					return lineHeight;
				});
    }
    
    private int processOneTextElement(
    		String lineText,
    		StackElement<ELEMENT> cur,
    		NumberOfChars numChars,
    		int xPos, int yPos, int lineHeight,
    		boolean lineWrapped,
    		boolean atStartOfLine,
    		BlockInlineLayoutUpdate<ELEMENT> state,
    		GetRenderer getRenderer,
    		BiConsumer<String, IElementRenderLayout> chunkListener) {

		final ElementLayoutSetters containerLayout = cur.resultingLayout;
		final int zIndex = cur.getLayoutStyles().getZIndex();

		final IElementRenderLayout textChunkLayout = state.addTextChunk(cur, lineText, numChars.getWidth(), lineHeight, zIndex, getRenderer.getRenderer(zIndex), lineWrapped);

		chunkListener.accept(lineText, textChunkLayout);
		
    	return lineHeight;
    }

	final void initAvailableAndRemainingWidthFromCSS(IElementLayout containerLayout, StackElement<ELEMENT> sub) {
		final int width  = LayoutHelperUnits.computeWidthPx(
				sub.getLayoutStyles().getWidth(),
				sub.getLayoutStyles().getWidthUnit(),
				containerLayout);

		initAvailableAndRemainingFromCSSWidth(sub, width);
	}

	final void initAvailableAndRemainingFromCSSWidth(StackElement<ELEMENT> sub, int width) {
		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
	}

	final void initAvailableAndRemainingWidthFromContainerRemaining(StackElement<ELEMENT> container, StackElement<ELEMENT> cur) {
		cur.setAvailableWidth(cur.getRemainingWidth());
		cur.setRemainingWidth(cur.getRemainingWidth());
	}

	final void initAvailableAndRemainingHeightFromCSS(IElementLayout containerLayout, StackElement<ELEMENT> sub) {
		final int height  = LayoutHelperUnits.computeHeightPx(
				sub.getLayoutStyles().getHeight(),
				sub.getLayoutStyles().getHeightUnit(),
				containerLayout);

		initAvailableAndRemainingFromCSSHeight(sub, height);
	}

	final void initAvailableAndRemainingFromCSSHeight(StackElement<ELEMENT> sub, int height) {
		sub.setAvailableHeight(height);
		sub.setRemainingHeight(height);
	}

	final void initAvailableAndRemainingHeightFromContainerRemaining(StackElement<ELEMENT> container, StackElement<ELEMENT> cur) {
		cur.setAvailableHeight(cur.getRemainingHeight());
		cur.setRemainingHeight(cur.getRemainingHeight());
	}
}

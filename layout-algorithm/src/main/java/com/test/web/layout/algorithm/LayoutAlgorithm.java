package com.test.web.layout.algorithm;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.algorithm.TextUtil.NumberOfChars;
import com.test.web.layout.common.FontStyle;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ViewPort;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;
import com.test.web.types.Pixels;


/*
/*
 * Main layout algorithm for document, implemented as a listener on start and end element events,
 * computes width for CSS speciefied elements on start element and tries to find dimensions
 * of other elements when processing end element, by adding up sizes.
 * 
 */

public class LayoutAlgorithm<
		ELEMENT,
		ELEMENT_TYPE,
		DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>>

	implements IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT>> {

	private final ITextExtent textExtent;
	
	// For creating renderers, rendering occurs in the same pass (but renderer implenentation might just queue operations for later)
	private final IDelayedRendererFactory rendererFactory;
	private final ILayoutDebugListener<ELEMENT_TYPE> debugListener;
	
	private final IFontSettings<ELEMENT_TYPE> fontSettings;
	
	private final TextUtil textUtil;
	
	public LayoutAlgorithm(
			ITextExtent textExtent,
			IDelayedRendererFactory rendererFactory,
			IFontSettings<ELEMENT_TYPE> fontSettings,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {

		this.textExtent = textExtent;
		this.textUtil = new TextUtil(textExtent);
		this.fontSettings = fontSettings;
		this.rendererFactory = rendererFactory;
		this.debugListener = debugListener;
	}

	public void layout(
			DOCUMENT document,
			ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener) {
		
		final LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state = new LayoutState<>(textExtent, viewPort, layoutContext, pageLayout, listener);
		
		document.iterate(this, state);
	}
	
	private int getDebugDepth(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		// state depth includes outer viewport so has to subtract one
		return state.getDepth() - 1;
	}

    @Override
	public void onElementStart(DOCUMENT document, ELEMENT element, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
    	
    	final ELEMENT_TYPE elementType = document.getType(element);

    	if (!document.isLayoutElement(elementType)) {
    		return;
    	}	
    	
    	if (debugListener != null) {
    		debugListener.onElementStart(
    				getDebugDepth(state),
    				elementType,
    				document.getId(element),
    				document.getTag(element),
    				document.getClasses(element));
    	}

    	// get layout information for the container of the element we're getting a callback on
    	final StackElement container = state.getCur();

    	// Push new sub-element onto stack with remaining width and height from current element
    	final StackElement sub = state.push(container.getRemainingWidth(), container.getRemainingHeight());
    	
    	// Compute all style information from defaults, css files, in-document style text and style attributes.
    	// Store the result in sub
    	computeStyles(state, document, element, elementType, sub);
    
    	final BaseLayoutCase layoutCase = LayoutCases.determineLayoutCase(container, sub.layoutStyles, elementType);

    	sub.setLayoutCase(layoutCase);
    	
    	layoutCase.onElementStart(container, element, sub, state);

		// Set resulting font of element, this is common for all display styles and is known at this point in time
    	setResultingFont(state, sub);

		// Got layout, set renderer from appropriate layer so that rendering can find it, rendering may happen already during this pass
    	setResultingRenderer(state, sub);

		// listener, eg renderer
		if (state.getListener() != null) {
			state.getListener().onElementStart(document, element, sub.resultingLayout);
		}
		
		if (debugListener!= null && sub.resultingLayout.areBoundsComputed()) {
			debugListener.onResultingLayoutAtStartTag(getDebugDepth(state), sub.resultingLayout, layoutCase.getClass().getSimpleName());
		}
	}
    
    private void setResultingFont(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement sub) {
		final FontSpec spec = sub.layoutStyles.getFont();
		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		sub.resultingLayout.setFont(font);
    }
    
    private void setResultingRenderer(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement sub) {
		final short zIndex = sub.layoutStyles.getZIndex();
		
		setResultingRenderer(state, sub, zIndex);
    }
    
    private void setResultingRenderer(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement sub, int zIndex) {
		final PageLayer<ELEMENT>layer = state.addOrGetLayer(zIndex, rendererFactory);
		sub.resultingLayout.setRenderer(zIndex, layer.getRenderer());
    }
  
    @Override
	public void onElementEnd(DOCUMENT document, ELEMENT element, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
	
    	final ELEMENT_TYPE elementType = document.getType(element);
    	
    	if (!document.isLayoutElement(elementType)) {
    		return;
    	}
    	
    	// End of element where wer're at
		final StackElement sub = state.getCur();
	
		final boolean boundsAlreadyComputed = sub.resultingLayout.areBoundsComputed();
			    
		state.pop();
    	
    	if (debugListener != null) {
    		debugListener.onElementEnd(getDebugDepth(state) , elementType);
    	}

		final StackElement container = state.getCur();

		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		sub.getLayoutCase().onElementEnd(container, element, sub, state);

		// Got layout, add to layer
		final short zIndex = sub.layoutStyles.getZIndex();
		
		final PageLayer<ELEMENT>layer = state.addOrGetLayer(zIndex, rendererFactory);

		// make copy since resulting layout is reused
		// TODO long-buffer version
		layer.add(element, sub.resultingLayout.makeCopy());

		if (state.getListener() != null) {
			state.getListener().onElementEnd(document, element, sub.resultingLayout);
		}
		
		// log layout computation if was done in onElementEnd()
		if (debugListener != null && ! boundsAlreadyComputed && sub.resultingLayout.areBoundsComputed()) {
			debugListener.onResultingLayoutAtEndTag(getDebugDepth(state), sub.resultingLayout, sub.getLayoutCase().getClass().getSimpleName());
		}
	}


    @Override
	public void onText(DOCUMENT document, ELEMENT element, String text, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

    	final ELEMENT_TYPE elementType = document.getType(element);

    	if (!document.isLayoutElement(elementType)) {
    		return;
    	}

		final StackElement cur = state.getCur();
		
		// just add to current text line as many characters as there are room for, or all the text if needed
		computeAndAddInlineText_wrapAndRenderAsNecessary(document, element, cur, text, state);
		
		
		// onTextComputeAndRender(document, element, text, cur, state);
	}
    
    private void computeAndAddInlineText_wrapAndRenderAsNecessary(DOCUMENT document, ELEMENT element, StackElement cur, String text, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		final IFont font = cur.resultingLayout.getFont();

		String remainingText = text;
		
		// Must push a separate layout for text for passing text bounds
		final StackElement textElem = state.push(cur.getRemainingWidth(), cur.getRemainingHeight());
		
		setResultingRenderer(state, textElem, cur.layoutStyles.getZIndex());

		// TODO must reflect any inline elements
		int xPos = cur.getCollectedBlockWidth();
		int yPos = cur.getCollectedBlockHeight();
		
		while ( ! remainingText.isEmpty() ) {
		
	    	// find number of chars width regards to this line
			final NumberOfChars numChars = textUtil.findNumberOfChars(remainingText, cur.getRemainingWidth(), font);
	
			final boolean lineWrapped;
			final String lineText;
			
			final int numCharsOnLine = numChars.getNumberOfChars();
			
			if (numCharsOnLine < text.length()) {
				
				// Not enough room for all of text, which means that line wraps.
				// figure max height, baseline and render line
				lineWrapped = true;
				lineText = remainingText.substring(0, numCharsOnLine);

				remainingText = remainingText.substring(numCharsOnLine);
			}
			else {
				// space for all characters
				lineWrapped = false;
				lineText = remainingText;
				
				remainingText = ""; // to exit loop
			}
			

			cur.addInlineText(lineText);
			
			final int lineHeight = font.getHeight();
			
			final ElementLayout containerLayout = cur.resultingLayout;
			
			// Compute bounds of text
			textElem.resultingLayout.getOuter().init(xPos, yPos, numChars.getWidth(), lineHeight);
			textElem.resultingLayout.getInner().init(xPos, yPos, numChars.getWidth(), lineHeight);
			
			textElem.resultingLayout.getAbsolute().init(
					containerLayout.getAbsolute().getLeft() + xPos,
					containerLayout.getAbsolute().getTop() + yPos,
					numChars.getWidth(),
					lineHeight);

			textElem.resultingLayout.setBoundsComputed();
			
			// render each item of text in a separate callback
			if (state.getListener() != null) {
				state.getListener().onText(document, element, lineText, textElem.resultingLayout);
			}

			xPos = 0;
			yPos += lineHeight;
		}
		
		state.pop();
    }
 
    private void renderCurrentTextLine() {
    	throw new UnsupportedOperationException("TODO");
    }
    
    @Deprecated // does not take varying inline element height into account
    private void onTextComputeAndRender(DOCUMENT document, ELEMENT element, String text, StackElement cur, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		final IFont font = cur.resultingLayout.getFont();
		
		final int width = textUtil.getTextLengthOrAvailableWidth(text, cur.getAvailableWidth(), font);
		
		int height;
		
		if (cur.getAvailableWidth() != Pixels.NONE) {
			// We have to compute number of lines for this text
			// TODO: floats
	
			height = textUtil.computeTextLinesHeight(text, cur, font);
		}
		else {
			// height is size of text line
			height = textUtil.getTextLineHeight(cur, font);
		}

		/*
		if (height > cur.getMaxBlockElementHeight()) {
			cur.setMaxBlockElementHeight(height);
		}
		*/
		
		if (state.getListener() != null) {
			
			// verify that we have set a renderer for this element before calling render listener
			if (cur.resultingLayout.getRenderer() == null) {
				throw new IllegalStateException("No renderer set");
			}
			
			state.getListener().onText(document, element, text, cur.getResultingLayout());
		}
    }
    
	
	private int setBlockBehavingElementWidthIfPresentInCSS(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement container, StackElement sub) {
		
		int cssWidthPx;

		if (sub.layoutStyles.hasWidth()) {
			// has width, compute and update
			cssWidthPx = LayoutHelperUnits.computeWidthPx(sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), container.getRemainingWidth());

	    	if (cssWidthPx <= 0) {
				throw new IllegalStateException("Computed width 0 from "  + sub.layoutStyles.getWidth() + " of unit " + sub.layoutStyles.getWidthUnit());
			}
	    	
			// Got width from CSS above
			sub.resultingLayout.setHasCSSWidth(true);
			
			// remaining - width may be negative below, eg if there is overflow
			final int remaining = Math.max(0, container.getRemainingWidth() - cssWidthPx);
			
			container.setRemainingWidth(remaining);

			sub.setAvailableWidth(cssWidthPx);
			sub.setRemainingWidth(cssWidthPx);

			if (debugListener != null) {
	    		debugListener.onComputedWidth(getDebugDepth(state), container.getAvailableWidth(), sub.getAvailableWidth(), cssWidthPx, sub.layoutStyles.hasWidth());
	    	}
		}
		else {
			cssWidthPx = Pixels.NONE;
		}

		return cssWidthPx;
 	}

	@Deprecated
	private int setBlockBehavingElementHeightIfPresentInCSS(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement container, StackElement sub) {
		// Cache values since may be updated further down
		int heightPx = Pixels.NONE;
    	final int cssHeighPxt;
		
		if (sub.layoutStyles.hasHeight()) {
			// has width, compute and update
			cssHeighPxt = LayoutHelperUnits.computeHeightPx(sub.layoutStyles.getHeight(), sub.layoutStyles.getHeightUnit(), container.getAvailableHeight());

	    	// height is Pixels.NONE if cur.getAvailableHeight() == Pixels.NONE (scrolled webage with no specified height)
	    	if (cssHeighPxt != Pixels.NONE) {
	    		sub.resultingLayout.setHasCSSHeight(true);
	    	}
		}
		else {
			cssHeighPxt = Pixels.NONE;
		}
		
    	if (debugListener != null) {
    		debugListener.onComputedHeight(getDebugDepth(state), container.getAvailableHeight(), sub.getAvailableHeight(), cssHeighPxt, sub.layoutStyles.hasHeight());
    	}
		
		if (cssHeighPxt == Pixels.NONE) {
			// No CSS height, height is computed from what is available in container, or from size of element, knowing width
			
			if (container.getRemainingHeight() > 0) {
				// Set to rest of available height
				heightPx = container.getRemainingHeight();

				// Not set remaining height to 0 here, that only happens when we switch to new block
			}
			else if (container.getRemainingHeight() == 0) {
				heightPx = 0;
			}
			else {
				// We must compute element height, but element is nested so we do not know yet, we must figure out
				// after having recursed. TODO this has impact on how render background
				sub.delayedLayout |= StackElement.UNKNOWN_HEIGHT;
			}
		}

		
		// height might be Pixels.NONE
		sub.resultingLayout.getOuter().setHeight(heightPx);
		sub.setAvailableHeight(heightPx);
		
		return heightPx;
	}

	@Deprecated
	private boolean tryComputeLayoutOfBlockBehavingElement(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement cur, StackElement sub) {
		// Adjust sub available width/height if is set

		final int width = setBlockBehavingElementWidthIfPresentInCSS(state, cur, sub);
		final int height = setBlockBehavingElementHeightIfPresentInCSS(state, cur, sub);
		
		final boolean layoutComputed;
		
		if (width != Pixels.NONE && height != Pixels.NONE) {
			// Compute inner-dimensions
			LayoutHelperWrappingBounds.computeDimensionsFromOuter(
					sub.layoutStyles.getDisplay(),
					cur.getRemainingWidth(),  width,  sub.layoutStyles.hasWidth(),
					cur.getRemainingHeight(), height, sub.layoutStyles.hasHeight(),
					sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);
	
			if (debugListener != null) {
				debugListener.onResultingLayoutAtStartTag(getDebugDepth(state), sub.resultingLayout, sub.getLayoutCase().getClass().getSimpleName());
			}
			
			layoutComputed = true;
		}
		else {
			layoutComputed = false;
		}

		return layoutComputed;
	}
	
	private void computeStyles(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, DOCUMENT document, ELEMENT element, ELEMENT_TYPE elementType, StackElement sub) {
		state.getLayoutContext().computeLayoutStyles(document, element, fontSettings, sub.layoutStyles, getDebugDepth(state), debugListener);
	}
}

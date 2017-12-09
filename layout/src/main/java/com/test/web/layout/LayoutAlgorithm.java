package com.test.web.layout;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.Tokenizer;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderer;
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

public class LayoutAlgorithm<ELEMENT, TOKENIZER extends Tokenizer>
	implements HTMLElementListener<ELEMENT, LayoutState<ELEMENT>> {

	private final ITextExtent textExtent;
	
	// For creating renderers, rendering occurs in the same pass (but renderer implenentation might just queue operations for later)
	private final IBufferRenderFactory renderFactory;
	private final ILayoutDebugListener debugListener;
	
	private final FontSettings fontSettings;
	
	private final TextUtil textUtil;
	
	public LayoutAlgorithm(
			ITextExtent textExtent,
			IBufferRenderFactory renderFactory,
			FontSettings fontSettings,
			ILayoutDebugListener debugListener) {
		this.textExtent = textExtent;
		this.textUtil = new TextUtil(textExtent);
		this.fontSettings = fontSettings;
		this.renderFactory = renderFactory;
		this.debugListener = debugListener;
	}

	public PageLayout<ELEMENT> layout(Document<ELEMENT> document, ViewPort viewPort, CSSContext<ELEMENT> cssContext, HTMLElementListener<ELEMENT, IElementRenderLayout> listener, IRenderer displayRenderer) {
		
		final LayoutState<ELEMENT> state = new LayoutState<>(textExtent, viewPort, displayRenderer, cssContext, listener);
		
		document.iterate(this, state);
		
		return state.getPageLayout();
	}
	
	private int getDebugDepth(LayoutState<ELEMENT> state) {
		// state depth includes outer viewport so has to subtract one
		return state.getDepth() - 1;
	}

    @Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {
    	
    	final HTMLElement elementType = document.getType(element);

    	if (!elementType.isLayoutElement()) {
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
    
    	final BaseLayoutCase layoutCase = LayoutCases.determineLayoutCase(container, sub.layoutStyles);

    	sub.setLayoutCase(layoutCase);
    	
    	layoutCase.onElementStart(container, sub);

		// Set resulting font of element, this is common for all display styles and is known at this point in time
    	setResultingFont(state, sub);

		// Got layout, set renderer from appropriate layer so that rendering can find it, rendering may happen already during this pass
    	setResultingRenderer(state, sub);

		// listener, eg renderer
		if (state.getListener() != null) {
			state.getListener().onElementStart(document, element, null);
		}
	}
    
    private void setResultingFont(LayoutState<ELEMENT> state, StackElement sub) {
		final FontSpec spec = sub.layoutStyles.getFont();
		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		sub.resultingLayout.setFont(font);
    }
    
    private void setResultingRenderer(LayoutState<ELEMENT> state, StackElement sub) {
		final short zIndex = sub.layoutStyles.getZIndex();
		final PageLayer<ELEMENT>layer = state.addOrGetLayer(zIndex, renderFactory);
		sub.resultingLayout.setRenderer(layer.getRenderer());
    }
  
    @Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {
	
    	final HTMLElement elementType = document.getType(element);
    	
    	if (!elementType.isLayoutElement()) {
    		return;
    	}
    	
    	// End of element where wer're at
		final StackElement sub = state.getCur();
		
		state.pop();
    	
    	if (debugListener != null) {
    		debugListener.onElementEnd(getDebugDepth(state) , elementType);
    	}

		final StackElement container = state.getCur();

		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		sub.getLayoutCase().onElementEnd(container, sub);

		// Got layout, add to layer
		final short zIndex = sub.layoutStyles.getZIndex();
		
		final PageLayer<ELEMENT>layer = state.addOrGetLayer(zIndex, renderFactory);

		// make copy since resulting layout is reused
		// TODO long-buffer version
		layer.add(element, sub.resultingLayout.makeCopy());

		if (state.getListener() != null) {
			state.getListener().onElementEnd(document, element, sub.resultingLayout);
		}
	}


    @Override
	public void onText(Document<ELEMENT> document, ELEMENT element, String text, LayoutState<ELEMENT> state) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

    	final HTMLElement elementType = document.getType(element);

    	if (!elementType.isLayoutElement()) {
    		return;
    	}

		final StackElement cur = state.getCur();
		
		// just add to current text line as many characters as there are room for, or all the text if needed
		computeAndAddInlineText_WrapAndRenderAsNecessary(cur, text);
		
		
		// onTextComputeAndRender(document, element, text, cur, state);
	}
    
    private void computeAndAddInlineText_WrapAndRenderAsNecessary(StackElement cur, String text) {
		final IFont font = cur.resultingLayout.getFont();

		String remainingText = text;

		while (!remainingText.isEmpty()) {
		
	    	// find number of chars width regards to this line
			final int numCharsOnLine = textUtil.findNumberOfChars(remainingText, cur.getRemainingWidth(), font);
	
			final boolean lineWrapped;
			final String lineText;
			
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
			}

			cur.addInlineText(lineText);
 
			if (lineWrapped) {
				renderCurrentTextLine();
			}
		}
    }
 
    private void renderCurrentTextLine() {
    	throw new UnsupportedOperationException("TODO");
    }
    
    @Deprecated // does not take varying inline element height into account
    private void onTextComputeAndRender(Document<ELEMENT> document, ELEMENT element, String text, StackElement cur, LayoutState<ELEMENT> state) {
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
    
	
	private int setBlockBehavingElementWidthIfPresentInCSS(LayoutState<ELEMENT> state, StackElement container, StackElement sub) {
		
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

	private int setBlockBehavingElementHeightIfPresentInCSS(LayoutState<ELEMENT> state, StackElement container, StackElement sub) {
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

	private boolean tryComputeLayoutOfBlockBehavingElement(LayoutState<ELEMENT> state, StackElement cur, StackElement sub) {
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
				debugListener.onResultingLayout(getDebugDepth(state), sub.resultingLayout);
			}
			
			layoutComputed = true;
		}
		else {
			layoutComputed = false;
		}

		return layoutComputed;
	}
	
	private void computeStyles(LayoutState<ELEMENT> state, Document<ELEMENT> document, ELEMENT element, HTMLElement elementType, StackElement sub) {

		final FontSpec defaultFont = fontSettings.getFontForElement(elementType);
    	
    	if (defaultFont == null) {
    		throw new IllegalStateException("No default font for element " + elementType);
    	}

    	// Collect all layout styles from CSS
    	state.getCSSContext().getCSSLayoutStyles(
    			elementType.getDefaultDisplay(),
    			defaultFont,
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				sub.layoutStyles);
    	
    	if (debugListener != null) {
    		debugListener.onElementCSS(getDebugDepth(state), sub.layoutStyles);
    	}

    	// Also apply style attribute if defined
		final ICSSDocumentStyles<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			// Get CSS document from style-tag of element
			state.getCSSContext().applyLayoutStyles(styleAttribute, element, sub.layoutStyles);

	    	if (debugListener != null) {
	    		debugListener.onElementStyleAttribute(getDebugDepth(state), sub.layoutStyles);
	    	}
		}
	
	}

}

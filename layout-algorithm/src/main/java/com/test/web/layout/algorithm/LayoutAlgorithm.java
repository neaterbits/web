package com.test.web.layout.algorithm;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.algorithm.TextUtil.NumberOfChars;
import com.test.web.layout.common.FontStyle;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.enums.Display;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;


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
		
		final LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state = new LayoutState<>(textExtent, viewPort, layoutContext, pageLayout, listener, debugListener);
		
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
    	// Calls back to compute all style information from defaults, css files, in-document style text and style attributes.
    	// This must be done from .push() since .push() must know CSS display type
    	final StackElement sub = state.push(
    			container.getRemainingWidth(), container.getRemainingHeight(),
    			elementType.toString(),
    			
    			layoutStyles -> computeStyles(state, document, element, elementType, layoutStyles));
    
    	final BaseLayoutCase layoutCase = LayoutCases.determineLayoutCase(container, sub.layoutStyles, elementType);

    	if (debugListener != null) {
    		debugListener.onElementLayoutCase(getDebugDepth(state), elementType, layoutCase.getName());
    	}
    	
    	sub.setLayoutCase(layoutCase);
    	
    	layoutCase.onElementStart(container, element, sub, state);

		// Set resulting font of element, this is common for all display styles and is known at this point in time
    	setResultingFont(state, sub);

		// Got layout, set renderer from appropriate layer so that rendering can find it, rendering may happen already during this pass
    	setResultingRenderer(sub, state);

		// listener, eg renderer
		if (state.getListener() != null) {
			state.getListener().onElementStart(document, element, sub.resultingLayout);
		}
		
		if (debugListener!= null && sub.resultingLayout.areBoundsComputed()) {
			debugListener.onResultingLayoutAtStartTag(getDebugDepth(state), sub.resultingLayout, layoutCase.getName());
		}
	}
    
    private void setResultingFont(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, StackElement sub) {
		final FontSpec spec = sub.layoutStyles.getFont();
		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		sub.resultingLayout.setFont(font);
    }
    
    private void setResultingRenderer(StackElement sub, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		final short zIndex = sub.layoutStyles.getZIndex();

		setResultingRenderer(sub, state, zIndex);
    }
    
    private void setResultingRenderer(StackElement sub, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, int zIndex) {
    	setResultingRenderer(sub.resultingLayout, state, zIndex);
    }
    
    private IDelayedRenderer getRenderer(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, int zIndex) {
    	return  state.addOrGetLayer(zIndex, rendererFactory).getRenderer();
    }

    private void setResultingRenderer(ElementLayout layout, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, int zIndex) {
		layout.setRenderer(zIndex, getRenderer(state, zIndex));
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

		final StackElement container = state.getContainer();

		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		sub.getLayoutCase().onElementEnd(container, element, sub, state);

		state.pop();
    	
    	if (debugListener != null) {
    		debugListener.onElementEnd(getDebugDepth(state) , elementType, sub.getLayoutCaseName());
    	}

		// If not inline, bounds must always be computed at this stage since we now are at end-tag and must be known here, regardless of layout case
    	final Display display = sub.resultingLayout.getDisplay();
    	if (display == null) {
    		throw new IllegalStateException("display == null: " + sub.getDebugName() + "/" + sub.resultingLayout);
    	}

    	if (!display.isInline() && !sub.resultingLayout.areBoundsComputed()) {
			throw new IllegalStateException("Bounds were not computed for element " + elementType + " at end tag");
		}

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
			debugListener.onResultingLayoutAtEndTag(getDebugDepth(state), sub.resultingLayout, sub.getLayoutCaseName());
		}
	}


    @Override
	public void onText(DOCUMENT document, ELEMENT element, String text, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

    	if (text.trim().isEmpty()) {
    		return; // Skip empty text
    	}
    	
    	final ELEMENT_TYPE elementType = document.getType(element);

    	if (debugListener != null) {
    		debugListener.onTextStart(getDebugDepth(state), elementType, text);
    	}

    	if (!document.isLayoutElement(elementType)) {
    		return;
    	}

		final StackElement cur = state.getCur();
		
		// just add to current text line as many characters as there are room for, or all the text if needed
		computeAndAddInlineText_wrapAndRenderAsNecessary_textUtil(document, element, elementType, cur, text, state);
		

    	if (debugListener != null) {
    		debugListener.onTextEnd(getDebugDepth(state), elementType, text);
    	}

		// onTextComputeAndRender(document, element, text, cur, state);
	}
    
    
    // Renders text in multiple text elements.
    // It has to be multiple since we can add more text on the last line of the wrapping text if there is room for it
    // eg. <span>This is some text that wraps</span><span>This is more text</span> could result in the latter span text result on the same line of the first span, wven if the first span wrapped.
    // We need to compute layout for each element, if the display view is resized, we will recompute new elements.
    // Thus the layouted text elements are not necessarily corresponding to the HTML text elements. 
    

    private void computeAndAddInlineText_wrapAndRenderAsNecessary_textUtil(DOCUMENT document, ELEMENT element, ELEMENT_TYPE elementType, StackElement cur, String text, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state) {
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
					processOneTextElement(lineText, cur, numChars, x, y, lineHeight, lineWrapped, atStartOfLineInitial, state, element, elementType, document);
					
					return lineHeight;
				});
    }
    
    private int processOneTextElement(
    		String lineText,
    		StackElement cur,
    		NumberOfChars numChars,
    		int xPos, int yPos, int lineHeight,
    		boolean lineWrapped,
    		boolean atStartOfLine,
    		LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state,
    		ELEMENT element,
    		ELEMENT_TYPE elementType,
    		DOCUMENT document) {

		final ElementLayout containerLayout = cur.resultingLayout;
		final int zIndex = cur.layoutStyles.getZIndex();

		final IElementRenderLayout textChunkLayout = state.addTextChunk(cur, lineText, numChars.getWidth(), lineHeight, zIndex, getRenderer(state, zIndex), lineWrapped);

		// render each item of text in a separate callback
		if (state.getListener() != null) {
			state.getListener().onText(document, element, lineText, textChunkLayout);
		}

    	if (debugListener != null) {
    		debugListener.onTextLine(getDebugDepth(state), elementType, lineText, textChunkLayout);
    	}

    	return lineHeight;
    }

	private void computeStyles(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> state, DOCUMENT document, ELEMENT element, ELEMENT_TYPE elementType, LayoutStyles layoutStyles) {
		state.getLayoutContext().computeLayoutStyles(document, element, fontSettings, layoutStyles, getDebugDepth(state), debugListener);
	}
}

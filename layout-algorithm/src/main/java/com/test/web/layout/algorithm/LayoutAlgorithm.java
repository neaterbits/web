package com.test.web.layout.algorithm;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.common.FontStyle;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ILayoutStylesGetters;
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

public abstract class LayoutAlgorithm<
		ELEMENT,
		ELEMENT_TYPE,
		DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>,
		STACK_ELEMENT extends LayoutStackElement>

	implements IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT>> {

	private final ITextExtent textExtent;
	
	// For creating renderers, rendering occurs in the same pass (but renderer implenentation might just queue operations for later)
	private final IDelayedRendererFactory rendererFactory;
	private final ILayoutDebugListener<ELEMENT_TYPE> debugListener;
	
	private final IFontSettings<ELEMENT_TYPE> fontSettings;
	
	private final TextUtil textUtil;

	protected abstract BaseLayoutCase<?, ?> determineLayoutCase(STACK_ELEMENT container, ILayoutStylesGetters subLayoutStyles, ELEMENT_TYPE elementType);

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

	protected abstract LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> createLayoutState(
			ITextExtent textExtent,
			ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener);
	
	public void layout(
			DOCUMENT document,
			ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener) {
		
		final LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state = createLayoutState(textExtent, viewPort, layoutContext, pageLayout, listener, debugListener);
		
		document.iterate(this, state);
	}
	
	private int getDebugDepth(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state) {
		// state depth includes outer viewport so has to subtract one
		return state.getDepth() - 1;
	}

    @Override
	public void onElementStart(DOCUMENT document, ELEMENT element, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state) {
    	
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
    	final STACK_ELEMENT container = state.getCur();

    	// Push new sub-element onto stack with remaining width and height from current element
    	// Calls back to compute all style information from defaults, css files, in-document style text and style attributes.
    	// This must be done from .push() since .push() must know CSS display type
    	final STACK_ELEMENT sub = state.push(
    			container,
    			elementType.toString(),
    			layoutStyles -> computeStyles(state, document, element, elementType, layoutStyles));
    
    	final BaseLayoutCase<?, ?> layoutCase = determineLayoutCase(container, sub.getLayoutStyles(), elementType);

    	if (debugListener != null) {
    		debugListener.onElementLayoutCase(getDebugDepth(state), elementType, layoutCase.getName());
    	}
    	
    	sub.setLayoutCase(layoutCase);
    	
    	getLayoutCase(sub).onElementStart(container, element, sub, state);

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
    
    private void setResultingFont(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state, STACK_ELEMENT sub) {
		final FontSpec spec = sub.getLayoutStyles().getFont();
		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		sub.resultingLayout.setFont(font);
    }
    
    private void setResultingRenderer(STACK_ELEMENT sub, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state) {
		final short zIndex = sub.getLayoutStyles().getZIndex();

		setResultingRenderer(sub, state, zIndex);
    }
    
    private void setResultingRenderer(STACK_ELEMENT sub, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state, int zIndex) {
    	setResultingRenderer(sub.resultingLayout, state, zIndex);
    }
    
    private IDelayedRenderer getRenderer(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state, int zIndex) {
    	return  state.addOrGetLayer(zIndex, rendererFactory).getRenderer();
    }

    private void setResultingRenderer(ElementLayoutSetters layout, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state, int zIndex) {
		layout.setRenderer(zIndex, getRenderer(state, zIndex));
    }
  
    @Override
	public void onElementEnd(DOCUMENT document, ELEMENT element, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state) {
	
    	final ELEMENT_TYPE elementType = document.getType(element);
    	
    	if (!document.isLayoutElement(elementType)) {
    		return;
    	}
    	
    	// End of element where wer're at
		final STACK_ELEMENT sub = state.getCur();
	
		final boolean boundsAlreadyComputed = sub.resultingLayout.areBoundsComputed();

		final STACK_ELEMENT container = state.getContainer();

		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		getLayoutCase(sub).onElementEnd(container, element, sub, state);

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
		final short zIndex = sub.getLayoutStyles().getZIndex();
		
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private BaseLayoutCase<STACK_ELEMENT, Object> getLayoutCase(STACK_ELEMENT element) {
    	return (BaseLayoutCase)element.getLayoutCase();
    }


    @Override
	public void onText(DOCUMENT document, ELEMENT element, String text, LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state) {
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

		final STACK_ELEMENT cur = state.getCur();
		
		getLayoutCase(cur).onText(cur, text, textUtil, state,
				zIndex -> getRenderer(state, zIndex),
				(lineText, textChunkLayout) -> {
			// Called back for each chunk the text is split into
			// TODO should handle this here?
			// render each item of text in a separate callback
			if (state.getListener() != null) {
				state.getListener().onText(document, element, lineText, textChunkLayout);
			}

	    	if (debugListener != null) {
	    		debugListener.onTextLine(getDebugDepth(state), elementType, lineText, textChunkLayout);
	    	}
		});
		

    	if (debugListener != null) {
    		debugListener.onTextEnd(getDebugDepth(state), elementType, text);
    	}

		// onTextComputeAndRender(document, element, text, cur, state);
	}
    
    

	private void computeStyles(LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, STACK_ELEMENT> state, DOCUMENT document, ELEMENT element, ELEMENT_TYPE elementType, LayoutStyles layoutStyles) {
		state.getLayoutContext().computeLayoutStyles(document, element, fontSettings, layoutStyles, getDebugDepth(state), debugListener);
	}
}

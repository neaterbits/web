package com.test.web.layout.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.common.FontKey;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ILayoutState;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.page.PageLayer;
import com.test.web.layout.common.page.PageLayout;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;

// State maintained while doing layout, ie. while recursing the DOM
public abstract class LayoutState<
			ELEMENT,
			ELEMENT_TYPE,
			DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>,
			STACK_ELEMENT extends LayoutStackElement<ELEMENT>> 

		implements ILayoutState {
	
	private final ITextExtent textExtent;
	private final ViewPort viewPort;
	private final IDelayedRendererFactory rendererFactory;
	
	// For finding size of text strings when using a particular font for rendering
	private final ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext;

	// Optional listener to eg do rendering in the layout flow
	private final IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener;
	
	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<STACK_ELEMENT> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensions are collected here
	private final PageLayout<ELEMENT> pageLayout;

	protected final ILayoutDebugListener<ELEMENT_TYPE> debugListener;
	
	protected abstract STACK_ELEMENT createStackElement(int stackIdx);

	protected final String stackDebugString() {
		return stack.toString();
	}
	
	
	protected LayoutState(
			ITextExtent textExtent,
			ViewPort viewPort,
			IDelayedRendererFactory rendererFactory,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {

		this.textExtent = textExtent;
		this.viewPort = viewPort;
		this.rendererFactory = rendererFactory;
		this.layoutContext = layoutContext;
		this.listener = listener;

		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = pageLayout;

		this.debugListener = debugListener;
		
		// Push intial element on stack, which is not a real element
		push(viewPort, "viewport", cssLayout -> cssLayout.setDisplay(Display.BLOCK));
	}
	
	protected final STACK_ELEMENT getStackElementAt(int stackIdx) {
		return stack.get(stackIdx);
	}
	
	@Override
	public final ViewPort getViewPort() {
		return viewPort;
	}
	
	protected final ElementLayoutSettersGetters createElementLayout() {
		return new ElementLayout();
	}

	final ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> getLayoutContext() {
		return layoutContext;
	}
	
	final PageLayout<ELEMENT> getPageLayout() {
		return pageLayout;
	}
	
	final IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> getListener() {
		return listener;
	}

	final PageLayer<ELEMENT> addOrGetLayer(int index, IDelayedRendererFactory renderFactory) {
		return pageLayout.addOrGetLayer(index, renderFactory);
	}

	protected final STACK_ELEMENT getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	protected final STACK_ELEMENT getContainer() {
		if (curDepth < 1) {
			throw new IllegalStateException("curDepth < 1");
		}

		return curDepth == 1 ? null : stack.get(curDepth - 2);
	}
	
    final void addToLayerForComputedLayout(STACK_ELEMENT sub) {

		addToLayerForComputedLayout(sub.getElement(), sub.resultingLayout);
    }

	protected final void addToLayerForComputedLayout(ELEMENT element, IElementRenderLayout elementLayout) {
		
		if (!elementLayout.areBoundsComputed()) {
			throw new IllegalStateException("Bounds were not computed for element " + element + " at end tag");
		}

		final PageLayer<ELEMENT>layer = addOrGetLayer(elementLayout.getZIndex(), rendererFactory);

		// make copy since resulting layout is reused
		// TODO long-buffer version
		layer.add(element, elementLayout.makeCopy());
    }

	
	final STACK_ELEMENT push(STACK_ELEMENT container, ELEMENT element, String debugName, Consumer<LayoutStyles> computeStyles) {

		return push(debugName, computeStyles, stackElement -> stackElement.init(container, element, debugName));
		
	}

	final STACK_ELEMENT push(ViewPort viewPort, String debugName, Consumer<LayoutStyles> computeStyles) {
		
		return push(debugName, computeStyles, stackElement -> stackElement.init(viewPort, debugName));
		
	}

	
	protected STACK_ELEMENT push(String debugName, Consumer<LayoutStyles> computeStyles, Consumer<STACK_ELEMENT> init) {
		
		final STACK_ELEMENT ret;
		
		if (curDepth == stack.size()) {
			ret = createStackElement(curDepth);
						
			stack.add(ret);
		}
		else {
			// reuse existing
			final STACK_ELEMENT toReuse = stack.get(curDepth);
			
			// StackElement may be referenced from inline element tree within StackElement so must check whether can reuse
			final boolean mayReuse = toReuse.checkAndUpdateWhetherInStackElementTree();

			if (mayReuse) {
		    	// make sure is cleared since reused
				toReuse.clear();
							
				ret = toReuse;
			}
			else {
				// Cannot reuse, allocate new instance and set in stack
				ret = createStackElement(curDepth);
				
				stack.set(curDepth, ret);
			}
		}
		
		init.accept(ret);
		
		
		ret.initCSSLayoutStyles(computeStyles);
		
		// Knows CSS display so set in resulting layout
		ret.resultingElementLayout.setDisplay(ret.getLayoutStyles().getDisplay());

		++ curDepth;
	
		return ret;
	}

	protected void pop() {
		-- curDepth;
	}
	
	
	
	final IFont getOrOpenFont(FontSpec spec, short style) {
		final FontKey fontKey = new FontKey(spec, style);
		
		IFont font = fonts.get(fontKey);
		
		if (font == null) {
			font = textExtent.getFont(spec.getFamily(), spec.getSize(), style);

			if (font == null) {
				throw new IllegalStateException("Failed to open font " + spec + " with style " + style);
			}
		}

		return font;
	}
	
	final void close() {
		// Close all fonts
		for (IFont font : fonts.values()) {
			textExtent.closeFont(font);
		}

		this.fonts.clear();
	}
	
	protected final int getDepth() {
		return curDepth;
	}
}

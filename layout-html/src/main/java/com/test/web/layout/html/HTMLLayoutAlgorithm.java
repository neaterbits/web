package com.test.web.layout.html;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.layout.algorithm.BaseLayoutCase;
import com.test.web.layout.algorithm.LayoutAlgorithm;
import com.test.web.layout.algorithm.LayoutState;
import com.test.web.layout.blockinline.StackElement;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.page.PageLayout;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

public class HTMLLayoutAlgorithm<ELEMENT, ELEMENT_TYPE, DOCUMENT extends IDocumentBase<ELEMENT,ELEMENT_TYPE,DOCUMENT>>
		extends LayoutAlgorithm<ELEMENT, ELEMENT_TYPE, DOCUMENT, StackElement<ELEMENT>> {

	public HTMLLayoutAlgorithm(ITextExtent textExtent, IDelayedRendererFactory rendererFactory,
			IFontSettings<ELEMENT_TYPE> fontSettings, ILayoutDebugListener<ELEMENT_TYPE> debugListener) {
		super(textExtent, rendererFactory, fontSettings, debugListener);
	}

	@Override
	protected BaseLayoutCase<?, ?, ?> determineLayoutCase(StackElement<ELEMENT> container, ILayoutStylesGetters subLayoutStyles,
			ELEMENT_TYPE elementType) {
		return LayoutCases.determineLayoutCase(container, subLayoutStyles, (HTMLElement)elementType);
	}

	@Override
	protected LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, StackElement<ELEMENT>> createLayoutState(ITextExtent textExtent,
			ViewPort viewPort,
			IDelayedRendererFactory rendererFactory,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {
		return new HTMLLayoutState<>(textExtent, viewPort, rendererFactory, layoutContext, pageLayout, listener, debugListener);
	}
}

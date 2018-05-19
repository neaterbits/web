package com.test.web.layout.html;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.algorithm.PageLayout;
import com.test.web.layout.blockinline.BlockInlineLayoutState;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ViewPort;
import com.test.web.render.common.ITextExtent;

public class HTMLLayoutState<
	ELEMENT,
	ELEMENT_TYPE,
	DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>>

		extends BlockInlineLayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT> {

	public HTMLLayoutState(ITextExtent textExtent, ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext, PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {
		super(textExtent, viewPort, layoutContext, pageLayout, listener, debugListener);
	}
}

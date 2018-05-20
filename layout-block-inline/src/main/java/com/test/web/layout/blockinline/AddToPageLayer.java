package com.test.web.layout.blockinline;

import com.test.web.layout.common.IElementRenderLayout;

public interface AddToPageLayer<ELEMENT> {
	void add(ELEMENT element, IElementRenderLayout elementLayout);
}

package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOHTMLRootElement extends OOContainerElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.HTML;
	}
}
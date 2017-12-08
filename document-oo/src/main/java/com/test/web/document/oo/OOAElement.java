package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOAElement extends OODisplayableHRefElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.A;
	}
}

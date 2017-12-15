package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOLiElement extends OOListMemberElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.LI;
	}
}

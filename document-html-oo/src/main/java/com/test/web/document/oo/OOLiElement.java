package com.test.web.document.oo;

import com.test.web.document.html.common.HTMLElement;

final class OOLiElement extends OOListMemberElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.LI;
	}
}

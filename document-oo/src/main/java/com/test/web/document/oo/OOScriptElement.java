package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOScriptElement extends OOReferenceElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.SCRIPT;
	}
}

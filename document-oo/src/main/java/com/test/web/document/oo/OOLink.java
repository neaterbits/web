package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.enums.LinkRelType;

final class OOLink extends OOHRefElement {

	@Override
	HTMLElement getType() {
		return HTMLElement.LINK;
	}

}

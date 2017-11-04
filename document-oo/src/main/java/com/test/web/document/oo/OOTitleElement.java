package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOTitleElement extends OOContainerElement {

	private String title;

	@Override
	HTMLElement getType() {
		return HTMLElement.TITLE;
	}

	String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}
}

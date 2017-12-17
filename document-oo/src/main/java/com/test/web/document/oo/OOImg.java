package com.test.web.document.oo;

import com.test.web.document.html.common.HTMLElement;

final class OOImg extends OOLeafElement {

	private String url;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.IMG;
	}

	String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}
}

package com.test.web.document.html.oo;

import com.test.web.document.html.common.HTMLElement;

final class OOHTMLRootElement extends OOContainerElement {

	private String xmlns;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.HTML;
	}

	public String getXMLNS() {
		return xmlns;
	}

	void setXMLNS(String xmlns) {
		this.xmlns = xmlns;
	}
}

package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

public class OOMetaTagElement extends OOMetaElement {

	private String charset;
	private String content;
	private String httpEquiv;
	private String name;
	private String scheme;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.META;
	}

	final String getCharset() {
		return charset;
	}

	final void setCharset(String charset) {
		this.charset = charset;
	}

	final String getContent() {
		return content;
	}

	final void setContent(String content) {
		this.content = content;
	}

	final String getHttpEquiv() {
		return httpEquiv;
	}

	final void setHttpEquiv(String httpEquiv) {
		this.httpEquiv = httpEquiv;
	}

	final String getName() {
		return name;
	}

	final void setName(String name) {
		this.name = name;
	}

	final String getScheme() {
		return scheme;
	}

	final void setScheme(String scheme) {
		this.scheme = scheme;
	}
}

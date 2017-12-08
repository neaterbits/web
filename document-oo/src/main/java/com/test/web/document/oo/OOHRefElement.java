package com.test.web.document.oo;

public abstract class OOHRefElement extends OOReferenceElement {
	private String href;
	private String hrefLang;

	final String getHRef() {
		return href;
	}

	final void setHRef(String href) {
		this.href = href;
	}

	final String getHRefLang() {
		return hrefLang;
	}

	final void setHRefLang(String hrefLang) {
		this.hrefLang = hrefLang;
	}
}

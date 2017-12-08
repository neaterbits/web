package com.test.web.document.oo;

import com.test.web.document.common.enums.LinkRelType;

abstract class OOHRefElement extends OOReferenceElement {
	private String href;
	private String hrefLang;
	private String media;
	private String mediaType;
	private LinkRelType rel;
	

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

	final String getMedia() {
		return media;
	}

	final void setMedia(String media) {
		this.media = media;
	}
	
	final String getMediaType() {
		return mediaType;
	}

	final void setMediaType(String type) {
		this.mediaType = type;
	}

	final LinkRelType getRel() {
		return rel;
	}

	final void setRel(LinkRelType rel) {
		this.rel = rel;
	}
}

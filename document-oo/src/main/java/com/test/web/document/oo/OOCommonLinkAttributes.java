package com.test.web.document.oo;

import com.test.web.document.common.ICommonLinkAttributes;
import com.test.web.document.common.enums.LinkRelType;

class OOCommonLinkAttributes implements ICommonLinkAttributes {
	private String href;
	private String hrefLang;
	private String media;
	private String mediaType;
	private LinkRelType rel;
	
	@Override
	public final String getHRef() {
		return href;
	}

	@Override
	public final void setHRef(String href) {
		this.href = href;
	}

	@Override
	public final String getHRefLang() {
		return hrefLang;
	}

	@Override
	public final void setHRefLang(String hrefLang) {
		this.hrefLang = hrefLang;
	}

	@Override
	public final String getMedia() {
		return media;
	}

	@Override
	public final void setMedia(String media) {
		this.media = media;
	}
	
	@Override
	public final String getMediaType() {
		return mediaType;
	}

	@Override
	public final void setMediaType(String type) {
		this.mediaType = type;
	}

	@Override
	public final LinkRelType getRel() {
		return rel;
	}

	@Override
	public final void setRel(LinkRelType rel) {
		this.rel = rel;
	}
}

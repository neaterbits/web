package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOLink extends OOReferenceElement {

	private String rel;
	private String linkType;
	private String href;
	private String media;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.LINK;
	}

	String getRel() {
		return rel;
	}

	void setRel(String rel) {
		this.rel = rel;
	}

	String getHRef() {
		return href;
	}

	void setHRef(String href) {
		this.href = href;
	}
	
	String getLinkType() {
		return linkType;
	}

	void setLinkType(String type) {
		this.linkType = type;
	}

	final String getMedia() {
		return media;
	}

	final void setMedia(String media) {
		this.media = media;
	}
}

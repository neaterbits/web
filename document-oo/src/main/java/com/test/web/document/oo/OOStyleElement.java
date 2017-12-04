package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

public class OOStyleElement extends OOTagElement {

	private String type;
	private String media;
	private boolean scoped;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.STYLE;
	}

	final String getMedia() {
		return media;
	}

	final void setMedia(String media) {
		this.media = media;
	}

	final boolean isScoped() {
		return scoped;
	}

	final void setScoped(boolean scoped) {
		this.scoped = scoped;
	}

	final String getStyleType() {
		return type;
	}

	final void setStyleType(String type) {
		this.type = type;
	}
}

package com.test.web.document.oo;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;

public class OOStyleElement extends OOTagElement {

	private String type;
	private String media;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.STYLE;
	}

	final String getMedia() {
		return media;
	}

	final void setMedia(String media) {
		this.media = setOrClearAttribute(HTMLAttribute.TYPE, media);
	}

	final boolean isScoped() {
		return isAttributeSet(HTMLAttribute.SCOPED);
	}

	final void setScoped(boolean scoped) {
		setOrClearMinimizableAttribute(HTMLAttribute.SCOPED, scoped);
	}

	final String getStyleType() {
		return type;
	}

	final void setStyleType(String type) {
		this.type = setOrClearAttribute(HTMLAttribute.TYPE, type);
	}
}

package com.test.web.document.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLStringConversion;

public class OOStyleElement extends OOTagElement {

	private String type;
	private String media;
	private String scoped;

	@Override
	HTMLElement getType() {
		return HTMLElement.STYLE;
	}

	final String getMedia() {
		return media;
	}

	final void setMedia(String media) {
		this.media = setOrClearAttribute(HTMLAttribute.MEDIA, media);
	}

	final String getScoped() {
		return scoped;
	}

	final void setScoped(String scoped) {
		setOrClearMinimizableAttribute(HTMLAttribute.SCOPED, scoped != null);
	
		this.scoped = scoped;
	}

	final String getStyleType() {
		return type;
	}

	final void setStyleType(String type) {
		this.type = setOrClearAttribute(HTMLAttribute.TYPE, type);
	}

	@Override
	String getStandardAttributeValue(HTMLAttribute attribute) {
		final String value;
		
		switch (attribute) {
		case TYPE:
			value = type;
			break;

		case MEDIA:
			value = media;
			break;

		case SCOPED:
			value = minimizableValue(attribute, scoped);
			break;
			
		default:
			value = super.getStandardAttributeValue(attribute);
			break;
		}

		return value;
	}

	@Override
	void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state) {
		switch (attribute) {
		case TYPE:
			setStyleType(value);
			break;
			
		case MEDIA:
			setMedia(value);
			break;
			
		case SCOPED:
			setScoped(value);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}
}

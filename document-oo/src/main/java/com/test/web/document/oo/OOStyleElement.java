package com.test.web.document.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLStringConversion;

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
		this.media = setOrClearAttribute(HTMLAttribute.MEDIA, media);
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
			value = minimizableValue(attribute);
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
			setOrClearMinimizableAttribute(attribute, true);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}
}

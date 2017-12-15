package com.test.web.jsapi.dom;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;

final class AttributeTestCase {
	private final HTMLElement element;
	private final HTMLAttribute attribute;
	private final  String html;
	private final String elementId;
	private final String attributeValue;
	
	AttributeTestCase(HTMLElement element, HTMLAttribute attribute, String html, String elementId, String attributeValue) {
		this.element = element;
		this.attribute = attribute;
		this.html = html;
		this.elementId = elementId;
		this.attributeValue = attributeValue;
	}

	HTMLElement getElement() {
		return element;
	}

	HTMLAttribute getAttribute() {
		return attribute;
	}

	String getHtml() {
		return html;
	}

	String getElementId() {
		return elementId;
	}

	String getAttributeValue() {
		return attributeValue;
	}
}

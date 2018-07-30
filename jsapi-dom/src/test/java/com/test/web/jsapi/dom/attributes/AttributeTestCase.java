package com.test.web.jsapi.dom.attributes;

import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;

final class AttributeTestCase {
	private final HTMLElement element;
	private final HTMLAttribute attribute;
	private final String html;
	private final String elementId;
	private final String attributeValue;
	private final AttributeTestValues attributeTestValues;
	
	AttributeTestCase(HTMLElement element, HTMLAttribute attribute, String html, String elementId, String attributeValue, AttributeTestValues attributeTestValues) {
		this.element = element;
		this.attribute = attribute;
		this.html = html;
		this.elementId = elementId;
		this.attributeValue = attributeValue;
		this.attributeTestValues = attributeTestValues;
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

	AttributeTestValues getAttributeTestValues() {
		return attributeTestValues;
	}
}

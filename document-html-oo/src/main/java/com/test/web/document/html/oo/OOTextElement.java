package com.test.web.document.html.oo;

final class OOTextElement extends OODocumentElement {

	private final String text;

	OOTextElement(String text) {

		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}

		this.text = text;
	}

	String getText() {
		return text;
	}
}


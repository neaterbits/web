package com.test.web.layout.algorithm;

final class TextLineElement {
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	
	// -------------------------- Text specific --------------------------
	private String text; // Cache text to render when reaching end of text line

	// -------------------------- For all inline elements  --------------------------
	private ElementLayout resultingLayout; // The layout of this line element
	
	void init(String text) {
		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}
		
		this.text = text;
	}

	void init(ElementLayout layout) {
		if (layout == null) {
			throw new IllegalArgumentException("layout == null");
		}

		this.resultingLayout = layout;
	}
}

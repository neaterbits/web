package com.test.web.document.common;

public enum Content {
	ELEMS(true, true),
	ELEMS_OR_TEXT(true, true),
	TEXT(true, true),
	STYLING(true, true),
	SCRIPT(true, true),
	NONE(true, false);
	
	private final boolean isContainer;
	private final boolean hasAnyContent;

	private Content(boolean isContainer, boolean hasAnyContent) {
		this.isContainer = isContainer;
		this.hasAnyContent = hasAnyContent;
	}

	public boolean isElemOrTextContainer() {
		return isContainer;
	}

	public boolean hasAnyContent() {
		return hasAnyContent;
	}
}

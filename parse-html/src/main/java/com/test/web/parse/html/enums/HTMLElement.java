package com.test.web.parse.html.enums;

/**
 * All known HTML elements
 * 
 * @author nhl
 *
 */

public enum HTMLElement {

	HTML("html", true),
	HEAD("head", true),
	BODY("body", true),
	TITLE("title", false),
	SCRIPT("script", false, HTMLAttribute.TYPE),
	DIV("div", true),
	SPAN("span", true),
	INPUT("input", false),
	FIELDSET("fieldset", true),
	UL("ul", true),
	LI("li", true)

	;
	
	private final String name;
	private final boolean container;
	private final HTMLAttribute [] attributes;
	
	private HTMLElement(String name, boolean container, HTMLAttribute ... attributes) {
		this.name = name;
		this.container = container;
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public HTMLAttribute[] getAttributes() {
		return attributes;
	}
	
	public boolean isContainerElement() {
		return container;
	}
}

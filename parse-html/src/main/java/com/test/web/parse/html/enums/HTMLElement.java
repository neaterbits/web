package com.test.web.parse.html.enums;

/**
 * All known HTML elements
 * 
 * @author nhl
 *
 */

public enum HTMLElement {

	HTML("html"),
	HEAD("head"),
	BODY("body"),
	TITLE("title"),
	SCRIPT("script", HTMLAttribute.TYPE),
	DIV("div"),
	SPAN("span"),
	INPUT("input"),
	FIELDSET("fieldset"),
	UL("ul"),
	LI("li")

	;
	
	private final String name;
	private final HTMLAttribute [] attributes;
	
	private HTMLElement(String name, HTMLAttribute ... attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public HTMLAttribute[] getAttributes() {
		return attributes;
	}
}

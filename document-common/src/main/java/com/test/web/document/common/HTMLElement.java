package com.test.web.document.common;


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
	LINK("link", false, HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF),
	SCRIPT("script", false, HTMLAttribute.TYPE),
	DIV("div", true),
	SPAN("span", true),
	INPUT("input", false),
	FIELDSET("fieldset", true),
	UL("ul", true),
	LI("li", true),
	
	IMG("img", false)

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

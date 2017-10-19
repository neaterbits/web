package com.test.web.document.common;

import com.test.web.css.common.enums.CSSDisplay;

/**
 * All known HTML elements
 * 
 * @author nhl
 *
 */

public enum HTMLElement {

	HTML(	"html",		true,		CSSDisplay.BLOCK),
	HEAD(	"head",		true,		null),
	BODY(	"body", 		true,		null),
	TITLE(	"title", 		false,	null),
	LINK(	"link", 		false,	null, 						HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF),
	SCRIPT("script", 	false,	null, 						HTMLAttribute.TYPE),
	DIV("div", 				true, 	CSSDisplay.BLOCK),
	SPAN("span", 		true, 	CSSDisplay.INLINE),
	INPUT("input", 		false,	CSSDisplay.INLINE),
	FIELDSET("fieldset", true, 	null),
	UL("ul", 				true,		null),
	LI("li", 					true,		null),
	
	IMG("img", 			false,	CSSDisplay.INLINE)

	;
	
	private final String name;
	private final boolean container;
	private final CSSDisplay defaultDisplay;
	private final HTMLAttribute [] attributes;
	
	private HTMLElement(String name, boolean container, CSSDisplay defaultDisplay, HTMLAttribute ... attributes) {
		this.name = name;
		this.container = container;
		this.defaultDisplay = defaultDisplay;
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

	public CSSDisplay getDefaultDisplay() {
		return defaultDisplay;
	}
	
	public boolean isLayoutElement() {
		return defaultDisplay != null;
	}
}

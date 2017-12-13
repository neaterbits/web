package com.test.web.document.common;

import com.test.web.css.common.enums.CSSDisplay;

/**
 * All known HTML elements
 * 
 * @author nhl
 *
 */

public enum HTMLElement {

	HTML(	"html",		true,		Place.ROOT,	CSSDisplay.BLOCK,	HTMLAttribute.XMLNS, HTMLAttribute.XML_LANG),
	HEAD(	"head",		true,		Place.HTML, 	null),
	BODY(	"body", 		true,		Place.HTML, 	null),
	TITLE(	"title", 		false,	Place.HEAD, 	null),
	META(  "meta",     false,    Place.HEAD, 	null,							HTMLAttribute.CHARSET, HTMLAttribute.CONTENT, HTMLAttribute.HTTP_EQUIV, HTMLAttribute.NAME, HTMLAttribute.SCHEME),
	LINK(	"link", 		false,	Place.HEAD, 	null, 						HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA, HTMLAttribute.REV),
	SCRIPT("script", 	false,	Place.BOTH, 	null, 						HTMLAttribute.TYPE),
	STYLE("style",		false,   	Place.BOTH,  null,							HTMLAttribute.TYPE, HTMLAttribute.MEDIA, HTMLAttribute.SCOPED),
	DIV("div", 				true, 	Place.BODY,	CSSDisplay.BLOCK),
	SPAN("span", 		true, 	Place.BODY, 	CSSDisplay.INLINE),
	INPUT("input", 		false,	Place.BODY,	CSSDisplay.INLINE),
	FIELDSET("fieldset", true, 	Place.BODY, 	null),
	UL("ul", 				true,		Place.BODY,	null),
	LI("li", 					true,		Place.BODY, 	null),
	
	A("a",					true,		Place.BODY,	CSSDisplay.INLINE,	HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA, HTMLAttribute.REV),
	AREA("area",		false,	Place.BODY,	null,							HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA, HTMLAttribute.REV),
	IMG("img", 			false,	Place.BODY,	CSSDisplay.INLINE),
	
	PROGRESS("progress", false, Place.BODY,	CSSDisplay.INLINE, HTMLAttribute.MAX, HTMLAttribute.VALUE)

	;
	
	private final String name;
	private final boolean container;
	private final Place placement;
	private final CSSDisplay defaultDisplay;
	private final HTMLAttribute [] attributes;
	
	private HTMLElement(String name, boolean container, Place placement, CSSDisplay defaultDisplay, HTMLAttribute ... attributes) {
		this.name = name;
		this.container = container;
		this.placement = placement;
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
	
	public Place getPlacement() {
		return placement;
	}

	public CSSDisplay getDefaultDisplay() {
		return defaultDisplay;
	}
	
	public boolean isLayoutElement() {
		return defaultDisplay != null;
	}
}

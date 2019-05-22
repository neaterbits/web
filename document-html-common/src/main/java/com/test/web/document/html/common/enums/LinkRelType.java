package com.test.web.document.html.common.enums;

import com.neaterbits.util.IEnum;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.types.Status;

public enum LinkRelType implements IEnum {
	ALTERNATE		("alternate", 		Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	ARCHIVES		("archives", 		Status.OBSOLETE,			HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	AUTHOR			("author", 			Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	BOOKMARK		("bookmark", 	Status.OK,						HTMLElement.A, HTMLElement.AREA),
	CANONICAL		("canonical", 		Status.OK,						HTMLElement.LINK),
	DNS_PREFETCH("dns-prefetch", Status.EXPERIMENTAL,	HTMLElement.LINK),
	EXTERNAL		("external", 		Status.OK,						HTMLElement.A, HTMLElement.AREA),
	FIRST				("first", 				Status.OBSOLETE,			HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	HELP				("help", 				Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	ICON				("icon", 				Status.OK,						HTMLElement.LINK),
	INDEX				("index", 			Status.OBSOLETE,			HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	LAST					("last", 				Status.OBSOLETE,			HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	LICENSE			("license", 			Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	MANIFEST		("manifest", 		Status.OK,						HTMLElement.LINK),
	NEXT				("next", 			Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),

	NOFOLLOW		("nofollow", 		Status.OK,						HTMLElement.A, HTMLElement.AREA),	
	NOOPENER		("noopener", 		Status.OK,						HTMLElement.A, HTMLElement.AREA),	
	NOREFERRER	("noreferrer", 	Status.OK,						HTMLElement.A, HTMLElement.AREA),	
	PINGBACK		("pingback", 		Status.OK,						HTMLElement.LINK),
	PRECONNECT	("preconnect", 	Status.EXPERIMENTAL,	HTMLElement.LINK),
	PREFETCH		("prefetch", 		Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	PRELOAD			("preload", 		Status.OK,						HTMLElement.LINK),
	PRERENDER		("prerender", 	Status.EXPERIMENTAL,	HTMLElement.LINK),
	PREV				("prev", 			Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	SEARCH			("search", 			Status.OK,						HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	SHORTLINK		("shortlink", 		Status.OK,						HTMLElement.LINK),
	STYLESHEET		("stylesheet", 	Status.OK,						HTMLElement.LINK),
	SIDEBAR			("sidebar", 		Status.NON_STANDARD, HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	TAG					("tag", 				Status.OK,						HTMLElement.A, HTMLElement.AREA),
	UP					("up", 				Status.OBSOLETE,			HTMLElement.A, HTMLElement.AREA, HTMLElement.LINK),
	
	;

	private final String name;
	private final Status specStatus;
	private final HTMLElement [] elements;
	
	private LinkRelType(String name, Status specStatus, HTMLElement ... elements) {
		if (name == null) {
			throw new IllegalArgumentException("name == null");
		}
		
		if (elements.length == 0) {
			throw new IllegalArgumentException("no elements");
		}
		
		this.name= name;
		this.specStatus = specStatus;
		this.elements = elements;
	}

	@Override
	public String getName() {
		return name;
	}

	public Status getSpecStatus() {
		return specStatus;
	}

	public HTMLElement[] getElements() {
		return elements;
	}
}

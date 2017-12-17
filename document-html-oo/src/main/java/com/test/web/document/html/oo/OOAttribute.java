package com.test.web.document.html.oo;

import com.test.web.document.html.common.HTMLAttribute;

final class OOAttribute {
	
	private final HTMLAttribute standard;
	private final OOCustomAttribute custom;

	OOAttribute(HTMLAttribute standard) {
		
		if (standard == null) {
			throw new IllegalArgumentException("standard == null");
		}
		
		this.standard = standard;
		this.custom = null;
	}

	OOAttribute(OOCustomAttribute custom) {
		if (custom == null) {
			throw new IllegalArgumentException("custom == null");
		}

		this.custom = custom;
		this.standard = null;
	}
}

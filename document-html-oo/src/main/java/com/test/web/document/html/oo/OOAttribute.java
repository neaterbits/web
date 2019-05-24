package com.test.web.document.html.oo;

import com.neaterbits.util.StringUtils;
import com.test.web.document.html.common.HTMLAttribute;

public final class OOAttribute {
	
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

	HTMLAttribute getStandard() {
		return standard;
	}

	OOCustomAttribute getCustom() {
		return custom;
	}

	String getName() {
		return standard != null ? standard.getAttributeName() : custom.getName();
	}

	String getNamespaceURI() {
		return standard != null ? standard.getAttributeNamespaceURI() : custom.getNamespaceURI();
	}
	
	String getPrefix() {
		return standard != null ? standard.getAttributePrefix() : custom.getPrefix();
	}

	
	String getLocalName() {
		return standard != null ? standard.getAttributeLocalName() : custom.getLocalName();
	}

	boolean matches(String namespaceURI, String localName) {
		final boolean matches;
		
		if (standard != null) {
			matches = standard.matches(namespaceURI, localName);
		}
		else {
			matches = StringUtils.equals(namespaceURI, custom.getNamespaceURI())
						&& localName.equals(custom.getLocalName());
		}
		
		return matches;
	}
	
	boolean matches(String name) {

		final boolean matches;

		if (standard != null) {
			matches = name.equals(standard.getAttributeName());
		}
		else {
			final String [] parts = StringUtils.split(name, ':');

			if (parts.length == 1 && parts[0].equals(custom.getLocalName())) {
				matches = true;
			}
			else if (parts.length == 2 && parts[0].equals(custom.getPrefix()) && parts[1].equals(custom.getLocalName())) {
				matches = true;
			}
			else if (parts.length == 2 && parts[0].equals(custom.getNamespaceURI()) && parts[1].equals(custom.getLocalName())) {
				matches = true;
			}
			else {
				matches = false;
			}
		}
		
		return matches;
	}
}

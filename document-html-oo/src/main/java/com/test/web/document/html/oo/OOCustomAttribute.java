package com.test.web.document.html.oo;

import com.test.web.types.StringUtils;

// Supporting setting whatever attributes on an element
final class OOCustomAttribute {
	private final String namespaceURI;
	private final String prefix;
	private final String localName;

	private String value;

	OOCustomAttribute(String namespaceURI, String prefix, String localName, String value) {
		
		if (localName == null) {
			throw new IllegalArgumentException("localName == null");
		}
		
		this.namespaceURI = namespaceURI;
		this.prefix = prefix;
		this.localName = localName;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getLocalName() {
		return localName;
	}
	
	public String getName() {
		return prefix != null
				? prefix + ':' + localName
				: localName;
	}
	
	public static OOCustomAttribute parse(String name) {
		final String [] parts = StringUtils.split(name, ':');
		
		final String namespaceURI;
		final String prefix;
		final String localName;
		
		final boolean validName;

		if (parts.length == 1) {
			namespaceURI = null;
			prefix = null;
			localName = parts[0];
			validName = true;
		}
		else if (parts.length == 2) {
			if (parts[0].startsWith("http")) {
				namespaceURI = parts[0];
				prefix = null;
			}
			else {
				namespaceURI = null;
				prefix = parts[0];
			}

			localName = parts[1];
			validName = true;
		}
		else {
			namespaceURI = prefix = localName = null;
			validName = false;
		}
		
		return validName ? new OOCustomAttribute(namespaceURI, prefix, localName, null) : null;
	}
}

package com.test.web.document.oo;

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
}

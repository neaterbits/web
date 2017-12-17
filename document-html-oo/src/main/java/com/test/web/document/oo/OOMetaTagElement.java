package com.test.web.document.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;

public class OOMetaTagElement extends OOMetaElement {

	private String charset;
	private String content;
	private String httpEquiv;
	private String name;
	private String scheme;
	
	@Override
	HTMLElement getType() {
		return HTMLElement.META;
	}

	final String getCharset() {
		return charset;
	}

	final void setCharset(String charset) {
		this.charset = setOrClearAttribute(HTMLAttribute.CHARSET, charset);
	}

	final String getContent() {
		return content;
	}

	final void setContent(String content) {
		this.content = setOrClearAttribute(HTMLAttribute.CONTENT, content);
	}

	final String getHttpEquiv() {
		return httpEquiv;
	}

	final void setHttpEquiv(String httpEquiv) {
		this.httpEquiv = setOrClearAttribute(HTMLAttribute.HTTP_EQUIV, httpEquiv);
	}

	final String getName() {
		return name;
	}

	final void setName(String name) {
		this.name = setOrClearAttribute(HTMLAttribute.NAME, name);
	}

	final String getScheme() {
		return scheme;
	}

	final void setScheme(String scheme) {
		this.scheme = setOrClearAttribute(HTMLAttribute.SCHEME, scheme);
	}

	@Override
	String getStandardAttributeValue(HTMLAttribute attribute) {
		final String value;
		
		switch (attribute) {
		case CHARSET:
			value = charset;
			break;
			
		case CONTENT:
			value = content;
			break;
			
		case HTTP_EQUIV:
			value = httpEquiv;
			break;
			
		case NAME:
			value = name;
			break;
			
		case SCHEME:
			value = scheme;
			break;
			
		default:
			value = super.getStandardAttributeValue(attribute);
			break;
		}

		return value;
	}

	@Override
	void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state) {
		
		switch (attribute) {
		case CHARSET:
			setCharset(value);
			break;
			
		case CONTENT:
			setContent(value);
			break;
			
		case HTTP_EQUIV:
			setHttpEquiv(value);
			break;
			
		case NAME:
			setName(value);
			break;
			
		case SCHEME:
			setScheme(value);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}
}

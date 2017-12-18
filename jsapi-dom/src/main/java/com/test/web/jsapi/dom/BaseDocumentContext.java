package com.test.web.jsapi.dom;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.IElementListener;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLElementListener;
import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.enums.LinkRelType;

abstract class BaseDocumentContext<ELEMENT, ATTRIBUTE> implements IDocument<ELEMENT, ATTRIBUTE> {
	
	final IDocument<ELEMENT, ATTRIBUTE> delegate;
	
	public BaseDocumentContext(IDocument<ELEMENT, ATTRIBUTE> delegate) {
		if (delegate == null) {
			throw new IllegalArgumentException("delegate == null");
		}

		this.delegate = delegate;
	}

	@Override
	public final int getNumAttributes(ELEMENT element) {
		return delegate.getNumAttributes(element);
	}

	@Override
	public final void setAttributeValue(ELEMENT element, int idx, String value) {
		delegate.setAttributeValue(element, idx, value);
	}

	@Override
	public void setAttributeValue(ELEMENT element, ATTRIBUTE attribute, String value) {
		delegate.setAttributeValue(element, attribute, value);
	}

	@Override
	public final ELEMENT getParentElement(ELEMENT element) {
		return delegate.getParentElement(element);
	}
	
	@Override
	public ATTRIBUTE getIdxOfAttributeWithName(ELEMENT element, String name) {
		return delegate.getIdxOfAttributeWithName(element, name);
	}

	@Override
	public final ATTRIBUTE getIdxOfAttributeWithNameNS(ELEMENT element, String namespaceURI, String localName) {
		return delegate.getIdxOfAttributeWithNameNS(element, namespaceURI, localName);
	}

	@Override
	public ATTRIBUTE getAttribute(ELEMENT element, int idx) {
		return delegate.getAttribute(element, idx);
	}

	@Override
	public final void setAttributeValue(ELEMENT element, String namespaceURI, String name, String value) {
		delegate.setAttributeValue(element, namespaceURI, name, value);
	}

	@Override
	public final String getAttributeName(ELEMENT element, ATTRIBUTE attribute) {
		return delegate.getAttributeName(element, attribute);
	}

	@Override
	public final void removeAttribute(ELEMENT element, String namespaceURI, String name) {
		delegate.removeAttribute(element, namespaceURI, name);
	}

	@Override
	public final String getAttributeNamespaceURI(ELEMENT element, ATTRIBUTE attribute) {
		return delegate.getAttributeNamespaceURI(element, attribute);
	}

	@Override
	public final String getAttributeLocalName(ELEMENT element, ATTRIBUTE attribute) {
		return delegate.getAttributeLocalName(element, attribute);
	}

	@Override
	public final String getAttributePrefix(ELEMENT element, ATTRIBUTE attribute) {
		return delegate.getAttributePrefix(element, attribute);
	}

	@Override
	public final String getAttributeValue(ELEMENT element, ATTRIBUTE attribute) {
		return delegate.getAttributeValue(element, attribute);
	}

	@Override
	public final HTMLElement getType(ELEMENT element) {
		return delegate.getType(element);
	}

	@Override
	public final ELEMENT getElementById(String id) {
		return delegate.getElementById(id);
	}

	@Override
	public final String getId(ELEMENT element) {
		return delegate.getId(element);
	}

	@Override
	public final String[] getClasses(ELEMENT element) {
		return delegate.getClasses(element);
	}

	@Override
	public final ICSSDocumentStyles<ELEMENT> getStyles(ELEMENT element) {
		return delegate.getStyles(element);
	}

	@Override
	public final List<ELEMENT> getElementsWithClass(String _class) {
		return delegate.getElementsWithClass(_class);
	}

	@Override
	public final int getNumElements(ELEMENT element) {
		return delegate.getNumElements(element);
	}

	@Override
	public final List<ELEMENT> getElementsWithType(HTMLElement type) {
		return delegate.getElementsWithType(type);
	}

	@Override
	public final String getScriptType(ELEMENT element) {
		return delegate.getScriptType(element);
	}

	@Override
	public final LinkRelType getLinkRel(ELEMENT element) {
		return delegate.getLinkRel(element);
	}

	@Override
	public final String getLinkType(ELEMENT element) {
		return delegate.getLinkType(element);
	}

	@Override
	public final String getLinkHRef(ELEMENT element) {
		return delegate.getLinkHRef(element);
	}

	@Override
	public final String getLinkHRefLang(ELEMENT element) {
		return delegate.getLinkHRefLang(element);
	}

	@Override
	public final String getImgUrl(ELEMENT element) {
		return delegate.getImgUrl(element);
	}

	@Override
	public final BigDecimal getProgressMax(ELEMENT element) {
		return delegate.getProgressMax(element);
	}

	@Override
	public final BigDecimal getProgressValue(ELEMENT element) {
		return delegate.getProgressValue(element);
	}

	@Override
	public final <PARAM> void iterate(IElementListener<ELEMENT, HTMLElement, IDocument<ELEMENT, ATTRIBUTE>, PARAM> listener, PARAM param) {
		delegate.iterate(listener, param);
	}

	@Override
	public final <PARAM> void iterateFrom(ELEMENT element, IElementListener<ELEMENT, HTMLElement, IDocument<ELEMENT, ATTRIBUTE>, PARAM> listener, PARAM param) {
		delegate.iterateFrom(element, listener, param);
	}

	@Override
	public final void dumpFlat(PrintStream out) {
		delegate.dumpFlat(out);
	}
}

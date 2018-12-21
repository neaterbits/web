package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

public class Element<
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

	extends Node<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {

	
	public Element() {

	}

	public Element(DOCUMENT_CONTEXT document, ELEMENT element) {
		super(document, element);
	}

	public final NamedNodeMap<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getAttributes() {
		return new NamedNodeMap<>(getDocument(), getElement(), this);
	}

	public final void setAttribute(String name, String value) {
		getDocument().setAttributeValue(getElement(), name, value);
	}
	
	public final ClassListDOMTokenList<ELEMENT> getClassList() {
	    return new ClassListDOMTokenList<ELEMENT>(getElement(), getDocument());
	}
	
	public final String getClassName() {
	    return getDocument().getClassName(getElement());
	}

	public final void setClassName(String className) {
        getDocument().setClassName(getElement(), className);
    }
	
	
	@JSNumber public final double getClientHeight() {
	    return getDocument().getClientHeight(getElement());
	}
	
	
	@JSNumber public final double getClientLeft() {
        return getDocument().getClientLeft(getElement());
    }


	@JSNumber public final double getClientTop() {
        return getDocument().getClientTop(getElement());
    }
	
	@JSNumber public final double getClientWidth() {
        return getDocument().getClientWidth(getElement());
    }

	
	public final String getId() {
	    return getDocument().getElementId(getElement());
	}
	
	public final void setId(String id) {
	    // TODO this requires CSS checks
	    getDocument().setElementId(getElement(), id);
	}
	
	public final String getInnerHTML() {
	    // No need to call layout updater here since done in main-loop
	    return HTMLUpdater.getInnerHTML(getElement(), getDocument());
	}
	
	public final void setInnerHTML(String html) {
        // No need to call layout updater here since done in main-loop
	    HTMLUpdater.setInnerHTML(getElement(), getDocument(), html);
	}
	
	public final String getLocalName() {
	    return getDocument().getLocalName(getElement()); 
	}

	public final String getNamespaceURI() {
        return getDocument().getNamespaceURI(getElement()); 
	}
	
	public final String getOuterHTML() {
        // No need to call layout updater here since done in main-loop
        return HTMLUpdater.getOuterHTML(getElement(), getDocument());
    }
	    
    public final void setOuterHTML(String html) {
        // No need to call layout updater here since done in main-loop
        HTMLUpdater.setOuterHTML(getElement(), getDocument(), html);
    }
    
    public final String getPrefix() {
        return getDocument().getPrefix(getElement());
    }
    
    @JSNumber public final double getScrollHeight() {
        return getDocument().getScrollHeight(getElement());
    }

    @JSNumber public final double getScrollLeft() {
        return getDocument().getScrollLeft(getElement());
    }

    public final void setScrollLeft(@JSNumber double left) {
        getDocument().setScrollLeft(getElement(), left);
    }

    @JSNumber public final double getScrollLeftMax() {
        return getDocument().getScrollLeftMax(getElement());
    }

    @JSNumber public final double getScrollTop() {
        return getDocument().getScrollTopMax(getElement());
    }

    public final void setScrollTop(@JSNumber double top) {
        getDocument().setScrollTop(getElement(), top);
    }

    @JSNumber public final double getScrollTopMax() {
        return getDocument().getScrollTopMax(getElement());
    }

    @JSNumber public final double getScrollWidth() {
        return getDocument().getScrollWidth(getElement());
    }
    
    public final boolean getTabStop() {
        return getDocument().getTabStop(getElement());
    }
    
    public final String getTagName() {
        return getDocument().getTagName(getElement());
    }
}

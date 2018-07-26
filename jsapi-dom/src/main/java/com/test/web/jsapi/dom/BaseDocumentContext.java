package com.test.web.jsapi.dom;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.IElementListener;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.IHTMLDocumentListener;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.jsapi.common.dom.IDocumentContext;

abstract class BaseDocumentContext<
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>
        implements IDocument<ELEMENT, ATTRIBUTE, DOCUMENT> {
	
	final DOCUMENT delegate;
	
	private final IHTMLDocumentListener<ELEMENT> listener;
	
	public BaseDocumentContext(DOCUMENT delegate, IHTMLDocumentListener<ELEMENT> listener) {
		if (delegate == null) {
			throw new IllegalArgumentException("delegate == null");
		}

		this.delegate = delegate;
		this.listener = listener;
	}
	
	@Override
    public final boolean isSameElement(ELEMENT element1, ELEMENT element2) {
        return delegate.isSameElement(element1, element2);
    }

	@Override
    public final ELEMENT getParentElement(ELEMENT element) {
        return delegate.getParentElement(element);
    }

	
    @Override
	public final int getNumAttributes(ELEMENT element) {
		return delegate.getNumAttributes(element);
	}
    
	@Override
    public HTMLAttribute getStandard(ATTRIBUTE attribute) {
        return delegate.getStandard(attribute);
    }

    @Override
	public final ATTRIBUTE setAttributeValue(ELEMENT element, int idx, String value) {
        boolean callListener = false;
        String beforeValue = null;

        if (listener != null) {
            // Might have to trigger UI, get last value
            final ATTRIBUTE attribute = delegate.getAttribute(element, idx);

            if (attribute != null && delegate.isStandardAttribute(attribute)) {
                callListener = true;

                beforeValue = delegate.getAttributeValue(element, attribute);
            }
        }
        
		final ATTRIBUTE attribute = delegate.setAttributeValue(element, idx, value);

		if (callListener) {
            listener.onAttributeUpdated(element, delegate.getStandard(attribute), beforeValue, value);
        }

        return attribute;
	}

    private ICSSDocumentStyles<ELEMENT> getStylesForListener(ELEMENT element) {
        
        final ICSSDocumentStyles<ELEMENT> beforeStyles = delegate.getStyles(element);
        
        return beforeStyles.makeCSSDocumentStylesCopy(element);
    }

	@Override
	public ATTRIBUTE setAttributeValue(ELEMENT element, ATTRIBUTE attribute, String value) {

	    boolean callListener = false;
        String beforeValue = null;
        ICSSDocumentStyles<ELEMENT> beforeStyles = null;
        HTMLAttribute standard = null;

        if (listener != null && attribute != null && null != (standard = delegate.getStandard(attribute))) {

            // Might have to trigger UI, get last value
            callListener = true;
            
            if (standard == HTMLAttribute.STYLE) {
                beforeStyles = getStylesForListener(element);
            }
            else {
                beforeValue = delegate.getAttributeValue(element, attribute);
            }
        }
	    
		final ATTRIBUTE resultAttribute = delegate.setAttributeValue(element, attribute, value);

		if (callListener) {
		    if (standard == HTMLAttribute.STYLE) {
		        listener.onStyleAttributeUpdated(element, beforeStyles, delegate.getStyles(element));
		    }
		    else {
                listener.onAttributeUpdated(element, standard, beforeValue, value);
		    }
        }

		return resultAttribute;
	}

	@Override
	public ATTRIBUTE setAttributeValue(ELEMENT element, String name, String value) {

        boolean callListener = false;
        String beforeValue = null;
        ICSSDocumentStyles<ELEMENT> beforeStyles = null;
        HTMLAttribute standard = null;

        if (listener != null) {
            // Might have to trigger UI, get last value
            final ATTRIBUTE attribute = delegate.getAttributeWithName(element, name);

            if (attribute != null) {
                standard = delegate.getStandard(attribute);
                
                if (standard != null) {
                
                    callListener = true;

                    if (standard == HTMLAttribute.STYLE) {
                        beforeStyles = getStylesForListener(element);
                    }
                    else {
                        beforeValue = delegate.getAttributeValue(element, attribute);
                    }
                }
            }
        }

		final ATTRIBUTE attribute = delegate.setAttributeValue(element, name, value);

        if (callListener) {
            if (standard == HTMLAttribute.STYLE) {
                listener.onStyleAttributeUpdated(element, beforeStyles, delegate.getStyles(element));
            }
            else {
                listener.onAttributeUpdated(element, standard, beforeValue, value);
            }
        }

		return attribute;
	}

	@Override
	public final ATTRIBUTE setAttributeValue(ELEMENT element, String namespaceURI, String localName, String value) {
        boolean callListener = false;
        String beforeValue = null;
        ICSSDocumentStyles<ELEMENT> beforeStyles = null;
        HTMLAttribute standard = null;
        
        if (listener != null) {
            // Might have to trigger UI, get last value
            final ATTRIBUTE attribute = delegate.getAttributeWithNameNS(element, namespaceURI, localName);
            

            if (attribute != null) {
                
                standard = delegate.getStandard(attribute);
                
                if (standard != null) {
                    callListener = true;
                
                    if (standard == HTMLAttribute.STYLE) {
                        beforeStyles = getStylesForListener(element);
                    }
                    else {
                        beforeValue = delegate.getAttributeValue(element, attribute);
                    }
                }
            }
        }

		final ATTRIBUTE attribute = delegate.setAttributeValue(element, namespaceURI, localName, value);

		if (callListener) {
            if (standard == HTMLAttribute.STYLE) {
                listener.onStyleAttributeUpdated(element, beforeStyles, delegate.getStyles(element));
            }
            else {
                listener.onAttributeUpdated(element, standard, beforeValue, value);
            }
		}

		return attribute;
	}

	@Override
	public ATTRIBUTE removeAttribute(ELEMENT element, String name) {
	    
	    boolean callListener = false;
	    String beforeValue = null;
        ICSSDocumentStyles<ELEMENT> beforeStyles = null;
	    HTMLAttribute standard = null;
	    
	    if (listener != null) {
	        // Might have to trigger UI, get last value
	        final ATTRIBUTE attribute = delegate.getAttributeWithName(element, name);

	        if (attribute != null) {
	            
	            standard = delegate.getStandard(attribute);

	            if (standard != null) {
	                callListener = true;
	            
	                if (standard == HTMLAttribute.STYLE) {
	                    beforeStyles = getStylesForListener(element);
	                }
	                else {
	                    beforeValue = delegate.getAttributeValue(element, attribute);
	                }
	            }
	        }
	    }

	    final ATTRIBUTE attribute = delegate.removeAttribute(element, name);

	    if (callListener) {
	        if (standard == HTMLAttribute.STYLE) {
	            listener.onStyleAttributeRemoved(element, beforeStyles);
	        }
	        else {
	            listener.onAttributeRemoved(element, delegate.getStandard(attribute), beforeValue);
	        }
	    }

		return attribute;
	}

	@Override
	public final ATTRIBUTE removeAttribute(ELEMENT element, String namespaceURI, String name) {
        boolean callListener = false;
        String beforeValue = null;
        ICSSDocumentStyles<ELEMENT> beforeStyles = null;
        HTMLAttribute standard = null;

        if (listener != null) {
            // Might have to trigger UI, get last value
            final ATTRIBUTE attribute = delegate.getAttributeWithNameNS(element, namespaceURI, name);

            if (attribute != null) {
                standard = delegate.getStandard(attribute);
    
                if (standard != null) {

                    callListener = true;

                    if (standard == HTMLAttribute.STYLE) {
                        beforeStyles = getStylesForListener(element);
                    } else {
                        beforeValue = delegate.getAttributeValue(element, attribute);
                    }
                }
            }
        }
        
        final ATTRIBUTE attribute = delegate.removeAttribute(element, namespaceURI, name);

        if (callListener) {
            if (standard == HTMLAttribute.STYLE) {
                listener.onStyleAttributeRemoved(element, beforeStyles);
            }
            else {
                listener.onAttributeRemoved(element, delegate.getStandard(attribute), beforeValue);
            }
        }
                
        return attribute;
	}

    @Override
    public ATTRIBUTE getAttributeWithName(ELEMENT element, String name) {
        return delegate.getAttributeWithName(element, name);
    }

    @Override
    public final ATTRIBUTE getAttributeWithNameNS(ELEMENT element, String namespaceURI, String localName) {
        return delegate.getAttributeWithNameNS(element, namespaceURI, localName);
    }

    @Override
    public ATTRIBUTE getAttribute(ELEMENT element, int idx) {
        return delegate.getAttribute(element, idx);
    }

    @Override
    public final String getAttributeName(ELEMENT element, ATTRIBUTE attribute) {
        return delegate.getAttributeName(element, attribute);
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
	public final <PARAM> void iterate(IElementListener<ELEMENT, HTMLElement, DOCUMENT, PARAM> listener, PARAM param) {
		delegate.iterate(listener, param);
	}

	@Override
	public final <PARAM> void iterateFrom(ELEMENT element, IElementListener<ELEMENT, HTMLElement, DOCUMENT, PARAM> listener, PARAM param) {
		delegate.iterateFrom(element, listener, param);
	}

	@Override
	public final void dumpFlat(PrintStream out) {
		delegate.dumpFlat(out);
	}
}

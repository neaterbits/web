package com.test.web.jsapi.dom;

import java.util.Iterator;


import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.IHTMLDocumentListener;
import com.test.web.document.html.common.elements.LAYOUTElement;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.util.StringUtils;

/**
 * Abstract classs in DocumentContext tree that handles all JS Element related calls.
 * 
 * @param <ELEMENT>
 * @param <ATTRIBUTE>
 * @param <DOCUMENT>
 * @param <DOCUMENT_CONTEXT>
 */


abstract class JSElementDocumentContext<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>> 

    extends BaseDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>

            implements LAYOUTElement<ELEMENT> {

    JSElementDocumentContext(DOCUMENT delegate, IHTMLDocumentListener<ELEMENT> listener) {
        super(delegate, listener);
    }
    
    private ATTRIBUTE getClassAttribute(ELEMENT element) {
        return getAttributeWithName(element, "class");
    }
    
    private void setClassAttributeValue(ELEMENT element, String value) {
        setAttributeValue(element, "class", value);
    }

    // Called form the DOMToken list
    @Override
    public final int getClassListLength(ELEMENT element) {

        final ATTRIBUTE attribute = getClassAttribute(element);
        
        final int length;
        
        if (attribute == null) {
            length = 0;
        }
        else {
            // Just use class attribute for this
            length = StringUtils.countTokens(getAttributeValue(element, attribute)); 
        }

        return length;
    }

    @Override
    public final String getClassListValue(ELEMENT element) {
        final ATTRIBUTE attribute = getClassAttribute(element);
        
        return attribute == null ? "" : getAttributeValue(element, attribute);
    }

    @Override
    public final String classListItem(ELEMENT element, int index) {
        final ATTRIBUTE attribute = getClassAttribute(element);
        
        if (attribute == null) {
            throw new IllegalStateException("No attribute, ought to have checked for length in JS Element class");
        }
        
        // Split, removing all whitespace
        final String value = getClassListValue(element);
        
        final String [] s = StringUtils.splitToTokens(value);

        return s[index];
    }

    @Override
    public final boolean classListContains(ELEMENT element, String token) {
        
        final boolean contains;
        
        final ATTRIBUTE attribute = getClassAttribute(element);
        
        if (attribute == null) {
            contains = false;
        }
        else {
            final String value = getAttributeValue(element, attribute);
            
            contains = value != null
                    ? StringUtils.hasToken(value, token)
                    : false;
        }

        return contains;
    }

    @Override
    public final void classListAdd(ELEMENT element, String token) {
        
        final ATTRIBUTE attribute = getClassAttribute(element);

        final String newValue;
        
        if (attribute == null) {
            newValue = token;

            setClassAttributeValue(element, newValue);
        }
        else {
            final String value = getAttributeValue(element, attribute);
            if (value == null) {
                newValue = token;
            }
            else {
                newValue = value + ' ' + token;
            }
            setAttributeValue(element, attribute, newValue);
        }

    }

    @Override
    public final void classListRemove(ELEMENT element, String ... tokens) {

        final ATTRIBUTE attribute = getClassAttribute(element);
        
        if (attribute != null) {
            final String value = getAttributeValue(element, attribute);
            
            if (value != null) {
                final String updated = StringUtils.removeTokens(value, tokens);

                // Matched tokens, so update
                if (updated != null) {
                    setAttributeValue(element, attribute, updated);
                }
            }
        }
    }

    @Override
    public final boolean classListReplace(ELEMENT element, String oldToken, String newToken) {
        
        boolean stringUpdated = false;
        
        final ATTRIBUTE attribute = getClassAttribute(element);
        
        if (attribute != null) {
            final String value = getAttributeValue(element, attribute);
            
            if (value != null) {
                final String updated = StringUtils.replaceToken(value, oldToken, newToken);
                
                if (updated != null) {
                    setAttributeValue(element, attribute, value);
                    stringUpdated = true;
                }
            }
        }
        
        return stringUpdated;
    }

    @Override
    public boolean classListSupports(ELEMENT element, String token) {
        // TODO not supported yet
        return false;
    }

    @Override
    public final boolean classListToggle(ELEMENT element, String token, Boolean force) {

        boolean tokenInList;
        
        final ATTRIBUTE attribute = getClassAttribute(element);

        
        final String value;
        
        if (attribute != null && null != (value = getAttributeValue(element, attribute))) {
            
            // Make room for adding token and one space
            final StringBuilder sb = new StringBuilder(value.length() + token.length() + 1);
            
            tokenInList = StringUtils.toggleToken(value, sb, token, force);
        }
        else {
            // Attribute not set at all
            if (force != null) {
                if (force) {
                    setClassAttributeValue(element, token);
                    tokenInList = true;
                }
                else {
                    // remove so nothing to do since no value
                    tokenInList = false;
                }
                
            }
            else {
                setClassAttributeValue(element, token);
                tokenInList = true;
            }
        }
        
        return tokenInList;
    }

    @Override
    public Iterator<String> classListEntries() {

        
        return null;
    }

    @Override
    public String getClassName(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setClassName(ELEMENT element, String className) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getElementId(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setElementId(ELEMENT element, String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getLocalName(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespaceURI(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPrefix(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getTabStop(ELEMENT element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getTagName(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }


    // Layout related methods
    
    @Override
    public double getClientHeight(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getClientLeft(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getClientTop(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getClientWidth(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getScrollHeight(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getScrollLeft(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setScrollLeft(ELEMENT element, double left) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double getScrollLeftMax(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getScrollTop(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setScrollTop(ELEMENT element, double top) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double getScrollTopMax(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getScrollWidth(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}

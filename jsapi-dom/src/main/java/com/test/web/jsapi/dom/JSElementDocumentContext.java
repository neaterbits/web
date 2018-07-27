package com.test.web.jsapi.dom;

import java.util.Iterator;

import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.IHTMLDocumentListener;
import com.test.web.document.html.common.elements.LAYOUTElement;
import com.test.web.jsapi.common.dom.IDocumentContext;

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

    @Override
    public int getClassListLength(ELEMENT element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getClassListValue(ELEMENT element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String classListItem(ELEMENT element, int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean classListContains(ELEMENT element, String token) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void classListAdd(ELEMENT element, String token) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void classListRemove(ELEMENT element, String... tokens) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean classListReplace(ELEMENT element, String oldToken, String newToken) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean classListSupports(ELEMENT element, String token) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean classListToggle(ELEMENT element, String token, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<String> classListEntries() {
        // TODO Auto-generated method stub
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

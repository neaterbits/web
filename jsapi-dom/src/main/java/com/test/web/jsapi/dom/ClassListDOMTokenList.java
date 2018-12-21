package com.test.web.jsapi.dom;

import java.util.Iterator;
import java.util.Objects;

import com.test.web.document.html.common.elements.DOCElement;
import com.test.web.jsapi.dom.interfaces.JSDOMTokenList;

public final class ClassListDOMTokenList<ELEMENT> implements JSDOMTokenList {

    private final ELEMENT element;
    private final DOCElement<ELEMENT> docElement;

    public ClassListDOMTokenList(ELEMENT element, DOCElement<ELEMENT> docElement) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(docElement);
        
        this.element = element;
        this.docElement = docElement;
    }

    @Override
    public int getLength() {
        return docElement.getClassListLength(element);
    }

    @Override
    public String getValue() {
        return docElement.getClassListValue(element);
    }

    @Override
    public JSRef<String> item(int index) {
        
        final JSRef<String> item;
        
        if (index >= getLength()) {
            item = new JSRef<>(null, JSRefState.UNDEFINED);
        }
        else {
            final String value = docElement.classListItem(element, index);
            
            if (value == null) {
                throw new IllegalStateException("Expected non-null value");
            }
            
            item = new JSRef<>(value, JSRefState.REF);
        }
        
        return item;
    }

    @Override
    public boolean contains(String token) {
        return docElement.classListContains(element, token);
    }

    @Override
    public void add(String token) {
        docElement.classListAdd(element, token);
    }

    @Override
    public void remove(String... tokens) {
        docElement.classListRemove(element, tokens);
    }

    @Override
    public boolean replace(String oldToken, String newToken) {
        return docElement.classListReplace(element, oldToken, newToken);
    }

    @Override
    public boolean supports(String token) {
        return docElement.classListSupports(element, token);
    }

    @Override
    public boolean toggle(String token) {
        return docElement.classListToggle(element, token, null);
    }

    @Override
    public boolean toggle(String token, boolean force) {
        return docElement.classListToggle(element, token, force);
    }

    @Override
    public Iterator<String> entries() {
        return docElement.classListEntries();
    }

    @Override
    public void forEach(Object callback) {
        docElement.classListForEach(element, callback);
    }

    @Override
    public void forEach(Object callback, Object argument) {
        docElement.classListForEach(element, callback, argument);
    }
}

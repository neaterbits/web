package com.test.web.jsapi.dom;

import java.util.Iterator;
import java.util.Objects;

import com.test.web.document.html.common.elements.DOCElement;
import com.test.web.jsapi.dom.interfaces.JSDOMTokenList;
import com.test.web.jsapi.dom.interfaces.JSFunction;

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
    public String item(int index) {
        return docElement.classListItem(element, index);
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
    public boolean toggle(String token, boolean force) {
        return docElement.classListToggle(element, token, force);
    }

    @Override
    public Iterator<String> entries() {
        return docElement.classListEntries();
    }

    @Override
    public void foreach(JSFunction callback) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void foreach(JSFunction callback, Object argument) {
        throw new UnsupportedOperationException("TODO");
    }
}

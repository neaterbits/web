package com.test.web.page.common;

import com.test.web.layout.common.page.PageLayout;
import com.test.web.types.layout.Unit;

public abstract class ElementLayoutAccessImpl<ELEMENT> implements ElementLayoutAccess<ELEMENT> {

    private final PageLayout<ELEMENT> pageLayout;

    public ElementLayoutAccessImpl(PageLayout<ELEMENT> pageLayout) {
        
        if (pageLayout == null) {
            throw new IllegalArgumentException("pageLayout == null");
        }

        this.pageLayout = pageLayout;
    }
    
    @Deprecated
    @Override
    public final void setElementWidth(ELEMENT element, double width, Unit unit) {
        throw new UnsupportedOperationException("TODO");
    }

    @Deprecated
    @Override
    public final void setElementHeight(ELEMENT element, double height, Unit unit) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int getWidth(ELEMENT element) {
        return 0;
    }

    @Override
    public int getHeight(ELEMENT element) {
        return 0;
    }

    @Override
    public ELEMENT getElementAt(double pageX, double pageY) {
        return null;
    }
}

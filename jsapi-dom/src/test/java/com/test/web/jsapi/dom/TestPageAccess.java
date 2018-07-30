package com.test.web.jsapi.dom;

import com.test.web.page.common.PageAccess;
import com.test.web.types.layout.Unit;

public class TestPageAccess<ELEMENT> implements PageAccess<ELEMENT>{

    @Override
    public void setElementWidth(ELEMENT element, double width, Unit unit) {
    }

    @Override
    public void setElementHeight(ELEMENT element, double height, Unit unit) {
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

    @Override
    public boolean relayoutIfChangedDynamically() {
        return false;
    }

    @Override
    public void updateView() {
        
    }

    @Override
    public void addPendingRelayout(ELEMENT element) {
        
    }
}

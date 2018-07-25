package com.test.web.page.common;

import com.test.web.document.common.IDocumentBase;
import com.test.web.layout.common.page.PageLayout;

public class PageAccessImpl<ELEMENT, ELEMENT_TYPE, DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>>
        extends ElementLayoutAccessImpl<ELEMENT>

        implements PageAccess<ELEMENT> {
    
    private final DOCUMENT document;

    
    public PageAccessImpl(PageLayout<ELEMENT> pageLayout, DOCUMENT document) {
        super(pageLayout);
        
        this.document = document;
    }

    @Override
    public void relayoutAnyChangedElements() {
        
    }

    @Override
    public boolean relayoutIfChangedDynamically() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateView() {
        // TODO Auto-generated method stub
        
    }
}

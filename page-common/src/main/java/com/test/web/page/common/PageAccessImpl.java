package com.test.web.page.common;

import java.util.HashSet;
import java.util.Set;

import com.test.web.document.common.IDocumentBase;
import com.test.web.layout.common.page.PageLayout;

public class PageAccessImpl<ELEMENT, ELEMENT_TYPE, DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>>
        extends ElementLayoutAccessImpl<ELEMENT>

        implements PageAccess<ELEMENT> {
    
    enum ElementFacet {
        SIZE,
        POSITION_RELATIVE
    }
    
    // Elements that may trrigger relayout
    private final Set<ELEMENT> pendingLayoutElements; 
    
    
    private final DOCUMENT document;
    
    
    

    public PageAccessImpl(PageLayout<ELEMENT> pageLayout, DOCUMENT document) {
        super(pageLayout);
        
        this.document = document;
        this.pendingLayoutElements = new HashSet<>();
    }

    private void relayoutIfAffectedByPendingLayouts(ELEMENT element, ElementFacet facetToRetrieve) {
        if (element == null) {
            throw new IllegalArgumentException("element == null");
        }

        // Must check if the current element is effected by a change in the set of pending elements
        // It is not enough to check whether this element or any ancestor element is within the list of changed elements,
        // positioning can be changes in sibling elements
        // So whether relayout is necessary depends on what field we are trying to get
        
        
        switch (facetToRetrieve) {
        case POSITION_RELATIVE:
            // Asking for position relative to this container.
            // One needs to relayout if there is some previous element in this container, or a sub of these that have layouted
            break;
            
            
        case SIZE:
            // Asking for size, only has to re-layout if size of this element or any sub-elements have changed.
            break;
        }
    }
    
    
    @Override
    public int getWidth(ELEMENT element) {
        
        relayoutIfAffectedByPendingLayouts(element, ElementFacet.SIZE);
        
        return super.getWidth(element);
    }

    @Override
    public int getHeight(ELEMENT element) {
        relayoutIfAffectedByPendingLayouts(element, ElementFacet.SIZE);

        return super.getHeight(element);
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

    @Override
    public void addPendingRelayout(ELEMENT element) {
        
        if (element == null) {
            throw new IllegalArgumentException("element == null");
        }

        pendingLayoutElements.add(element);
    }
}

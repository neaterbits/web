package com.test.web.jsapi.dom;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.IHTMLDocumentListener;
import com.test.web.page.common.PageAccess;

/**
 * Detects DOM changes and converts them into layout agnostic calls on PageAccess
 * 
 * TODO for now always relayouts for style changes and not for other attributes.
 *      could be smarter with regards to figuring diff in style-sheet but there is also a cost for diffing
 * 
 */

final class UIDocumentChangeListener<ELEMENT> implements IHTMLDocumentListener<ELEMENT> {

    private final PageAccess<ELEMENT> pageAccess;

    UIDocumentChangeListener(PageAccess<ELEMENT> pageAccess) {

        if (pageAccess == null) {
            throw new IllegalArgumentException("pageAccess == null");
        }
        
        this.pageAccess = pageAccess;
    }

    
    private boolean styleChangeRequiresUpdate(ICSSDocumentStyles<ELEMENT> beforeStyles, ICSSDocumentStyles<ELEMENT> afterStyles) {
        
        final boolean requiresUpdate;
        
        if (afterStyles == null) {
            // Style-element removed completely, just relayout
            requiresUpdate = true;
        }
        else {
            // TODO perhaps diff here, but must make sure there are no undetected updates that require layout
            // so might not be worth the effort
            requiresUpdate = true;
        }
        
        return requiresUpdate;
    }
    
    
    @Override
    public void onStyleAttributeUpdated(ELEMENT element, ICSSDocumentStyles<ELEMENT> beforeStyle, ICSSDocumentStyles<ELEMENT> afterStyle) {
        
        if (styleChangeRequiresUpdate(beforeStyle, afterStyle)) {
            pageAccess.addPendingRelayout(element);
        }
    }



    @Override
    public void onAttributeUpdated(ELEMENT element, HTMLAttribute attribute, String beforeValue, String afterValue) {
        switch (attribute) {
        case STYLE:
            throw new UnsupportedOperationException("Should have called style-specific methods");
            
        default:
            break;
        }
    }


    @Override
    public void onStyleAttributeRemoved(ELEMENT element, ICSSDocumentStyles<ELEMENT> beforeStyles) {
        
        if (styleChangeRequiresUpdate(beforeStyles, null)) {
            pageAccess.addPendingRelayout(element);
        }
    }


    @Override
    public void onAttributeRemoved(ELEMENT element, HTMLAttribute attribute, String beforeValue) {
        switch (attribute) {
        case STYLE:
            throw new UnsupportedOperationException("Should have called style-specific methods");
            
        default:
            break;
        }
    }

}

package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IEvent;

public interface BrowserDefaultEventHandling<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>> {

    /**
     * Implements browser handling of event
     * @param event
     * @param element
     * 
     * @return true if event was processed (so do not propagate further), false otherwise
     */
    
    boolean onHandleEvent(IEvent event, DOCUMENT document, ELEMENT element);
    
}

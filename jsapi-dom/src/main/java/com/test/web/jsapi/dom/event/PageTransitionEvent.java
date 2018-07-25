package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

public final class PageTransitionEvent<ELEMENT> extends Event<ELEMENT> {
    
    private final boolean persisted;
    
    public PageTransitionEvent(Element<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,

            boolean persisted) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted);
        
        this.persisted = persisted;
    }

    public boolean isPersisted() {
        return persisted;
    }
}

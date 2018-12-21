package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

public final class PopStateEvent<ELEMENT> extends Event<ELEMENT> {

    private final Object state;
    
    public PopStateEvent(Element<ELEMENT, ?, ?, ?> target, long timeStamp, boolean composed, boolean isTrusted,
            
            Object state) {
        super(target, HTMLEvent.POPSTATE, timeStamp, composed,  isTrusted);
        
        this.state = state;
    }

    public Object getState() {
        return state;
    }
}

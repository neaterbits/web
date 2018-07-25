package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

public final class TransitionEvent<ELEMENT> extends Event<ELEMENT> {

    private final String propertyName;
    private final float elapsedTime;
    private final String pseudoElement;
    
    public TransitionEvent(Element<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            String propertyName, float elapsedTime, String pseudoElement) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted);

        this.propertyName = propertyName;
        this.elapsedTime = elapsedTime;
        this.pseudoElement = pseudoElement;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public String getPseudoElement() {
        return pseudoElement;
    }
}

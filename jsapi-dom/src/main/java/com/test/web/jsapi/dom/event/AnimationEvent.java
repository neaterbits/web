package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

public final class AnimationEvent<ELEMENT> extends Event<ELEMENT> {

    private final String animationName;
    private final float elapsedTime;
    private final String pseudoElement;
    
    public AnimationEvent(Element<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,

            String animationName,
            float elapsedTime,
            String pseudoElement) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted);

        this.animationName = animationName;
        this.elapsedTime = elapsedTime;
        this.pseudoElement = pseudoElement;
    }

    public final String getAnimationName() {
        return animationName;
    }

    public final float getElapsedTime() {
        return elapsedTime;
    }

    public final String getPseudoElement() {
        return pseudoElement;
    }
}

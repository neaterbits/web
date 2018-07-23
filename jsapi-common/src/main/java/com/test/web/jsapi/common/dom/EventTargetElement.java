package com.test.web.jsapi.common.dom;

public interface EventTargetElement<ELEMENT> extends IElement {

    ELEMENT getTargetElement();
    
    IEventListener getEventListener(IEvent event);
}

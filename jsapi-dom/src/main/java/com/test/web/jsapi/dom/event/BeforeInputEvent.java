package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

public final class BeforeInputEvent<ELEMENT> extends Event<ELEMENT> {

    public BeforeInputEvent(Element<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted) {
        super(target, htmlEvent, timeStamp, composed, isTrusted);
    }
    
}

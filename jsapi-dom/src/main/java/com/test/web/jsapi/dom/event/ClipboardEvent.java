package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Event;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.dnd.DataTransfer;

public final class ClipboardEvent<ELEMENT> extends Event<ELEMENT> {
    
    private final DataTransfer clipboardData;

    
    public ClipboardEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            DataTransfer clipboardData) {
        super(target, htmlEvent, timeStamp, composed, isTrusted);

        this.clipboardData = clipboardData;
    }

    public DataTransfer getClipboardData() {
        return clipboardData;
    }
}

package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.audio.AudioBuffer;
import com.test.web.jsapi.dom.Event;
import com.test.web.jsapi.dom.EventTarget;

public final class OfflineAudioCompletionEvent<ELEMENT> extends Event<ELEMENT> {

    private final AudioBuffer renderedBuffer;

    public OfflineAudioCompletionEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            AudioBuffer renderedBuffer) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted);
        
        this.renderedBuffer = renderedBuffer;
    }

    public AudioBuffer getRenderedBuffer() {
        return renderedBuffer;
    }
}

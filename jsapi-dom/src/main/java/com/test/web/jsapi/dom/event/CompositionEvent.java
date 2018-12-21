package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.InputDeviceCapabilties;
import com.test.web.jsapi.dom.WindowProxy;

public final class CompositionEvent<ELEMENT> extends UIEvent<ELEMENT> {

    private final String data;
    private final String locale;
    
    public CompositionEvent(Element<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            long detail, boolean isChar, UIEventNonStandard nonStandard, InputDeviceCapabilties sourceCapabilites, WindowProxy view,
            
            String data, String locale) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted, detail, isChar, nonStandard, sourceCapabilites, view);

        this.data = data;
        this.locale = locale;
    }

    public String getData() {
        return data;
    }

    public String getLocale() {
        return locale;
    }
}

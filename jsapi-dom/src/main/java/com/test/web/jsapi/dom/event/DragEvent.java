package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.externalevents.MouseState;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.InputDeviceCapabilties;
import com.test.web.jsapi.dom.WindowProxy;
import com.test.web.jsapi.dom.dnd.DataTransfer;

public final class DragEvent<ELEMENT> extends MouseEvent<ELEMENT> {

    private final DataTransfer dataTransfer;
    
    public DragEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            long detail, boolean isChar, UIEventNonStandard nonStandard, InputDeviceCapabilties sourceCapabilites, WindowProxy view,
            
            MouseState mouseState, String region, EventTarget<ELEMENT, ?, ?, ?> relatedTarget,
            
            DataTransfer dataTransfer) {
        super(target, htmlEvent, timeStamp, composed, isTrusted, detail, isChar, nonStandard, sourceCapabilites, view,
                mouseState, region, relatedTarget);

        this.dataTransfer = dataTransfer;
    }

    public DataTransfer getDataTransfer() {
        return dataTransfer;
    }
}

package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.dom.Event;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.InputDeviceCapabilties;
import com.test.web.jsapi.dom.WindowProxy;

public class UIEvent<ELEMENT> extends Event<ELEMENT> {

    private final long detail;
    private final boolean isChar;
    private final UIEventNonStandard nonStandard;
    private final InputDeviceCapabilties sourceCapabilites;
    private final WindowProxy view;
    
    public UIEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            long detail, boolean isChar, UIEventNonStandard nonStandard, InputDeviceCapabilties sourceCapabilites, WindowProxy view) {  

        super(target, htmlEvent, timeStamp, composed, isTrusted);
        
        this.detail = detail;
        this.isChar = isChar;
        this.nonStandard = nonStandard;
        this.sourceCapabilites = sourceCapabilites;
        this.view = view;
    }

    public final long getDetail() {
        return detail;
    }

    public final boolean isChar() {
        return isChar;
    }

    public final int getLayerX() {
        return nonStandard.getLayerX();
    }

    public final int getLayerY() {
        return nonStandard.getLayerY();
    }

    /* Crashes with MouseEvent.pageX/pageY
    public final int getPageX() {
        return nonStandard.getPageX();
    }

    public final int getPageY() {
        return nonStandard.getPageY();
    }
    */

    public final InputDeviceCapabilties getSourceCapabilites() {
        return sourceCapabilites;
    }

    public final WindowProxy getView() {
        return view;
    }
}

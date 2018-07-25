package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.externalevents.MouseState;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.InputDeviceCapabilties;
import com.test.web.jsapi.dom.WindowProxy;

public final class WheelEvent<ELEMENT> extends MouseEvent<ELEMENT> {

    public enum DeltaMode {
        PIXEL(0x00),
        LINE(0x01),
        PAGE(0x02);
        
        private final int code;

        private DeltaMode(int code) {
            this.code = code;
        }
    }
    
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;
    
    private final DeltaMode deltaMode;

    public WheelEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed,
            boolean isTrusted,
            
            long detail, boolean isChar, UIEventNonStandard nonStandard, InputDeviceCapabilties sourceCapabilites, WindowProxy view,
            
            MouseState mouseState, String region, EventTarget<ELEMENT, ?, ?, ?> relatedTarget,

            double deltaX, double deltaY, double deltaZ, DeltaMode deltaMode) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted, detail, isChar, nonStandard, sourceCapabilites, view,
                mouseState, region, relatedTarget);
        
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.deltaMode = deltaMode;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public long getDeltaMode() {
        return deltaMode.code;
    }
}

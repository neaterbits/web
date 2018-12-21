package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.externalevents.MouseButton;
import com.test.web.externalevents.MouseState;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.InputDeviceCapabilties;
import com.test.web.jsapi.dom.WindowProxy;

public class MouseEvent<ELEMENT> extends UIEvent<ELEMENT> {

    private final MouseState mouseState;
    private final String region;
    private final EventTarget<ELEMENT, ?, ?, ?> relatedTarget;
    
    public MouseEvent(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted,
            
            long detail, boolean isChar, UIEventNonStandard nonStandard, InputDeviceCapabilties sourceCapabilites, WindowProxy view,
            
            MouseState mouseState, String region, EventTarget<ELEMENT, ?, ?, ?> relatedTarget) {
        
        super(target, htmlEvent, timeStamp, composed, isTrusted, detail, isChar, nonStandard, sourceCapabilites,
                view);
        
        this.mouseState = mouseState;
        this.region = region;
        this.relatedTarget = relatedTarget;
    }

    public final short getButton() {
        final short button;
        
        switch (mouseState.getButton()) {
        case MAIN:
            button = 0;
            break;
            
        case AUXILARY:
            button = 1;
            break;

        case SECONDARY:
            button = 2;
            break;

        case FOURTH:
            button = 3;
            break;
            
        case FIFTH:
            button = 4;
            break;
            
        default:
            throw new UnsupportedOperationException("Unknown button: " + mouseState.getButton());
        }

        return button;
    }

    public final short getButtons() {
        short flag = 0;

        for (MouseButton button : mouseState.getButtons()) {
            switch (button) {
            case MAIN:
                flag += 1;
                break;
                
            case AUXILARY:
                flag += 4;
                break;
                
            case SECONDARY:
                flag += 2;
                break;
                
            case FOURTH:
                flag += 8;
                break;
                
            case FIFTH:
                flag += 16;
                break;
                
                
            default:
                throw new UnsupportedOperationException("Unknown mouse button " + button);
            }
        }
        
        return flag;
    }

    public final double getScreenX() {
        return mouseState.getScreenX();
    }
    
    public final double getScreenY() {
        return mouseState.getScreenY();
    }
    
    public final double getClientX() {
        return mouseState.getClientX();
    }
    
    public final double getClientY() {
        return mouseState.getClientY();
    }
    
    public final double getMovementX() {
        return mouseState.getMovementX();
    }
    
    public final double getMovementY() {
        return mouseState.getMovementY();
    }
    
    public final double getOffsetX() {
        return mouseState.getOffsetX();
    }
    
    public final double getOffsetY() {
        return mouseState.getOffsetY();
    }

    public final double getPageX() {
        return mouseState.getPageX();
    }
    
    public final double getPageY() {
        return mouseState.getPageY();
    }

    public final boolean getCtrlKey() {
        return mouseState.getCtrlKey();
    }
    
    public final boolean getShiftKey() {
        return mouseState.getShiftKey();
    }
    
    public final boolean getAltKey() {
        return mouseState.getAltKey();
    }
    
    public final boolean getMetaKey() {
        return mouseState.getMetaKey();
    }

    public final String getRegion() {
        return region;
    }

    public final EventTarget<ELEMENT, ?, ?, ?> getRelatedTarget() {
        return relatedTarget;
    }
}

package com.test.web.externalevents;

import java.util.Arrays;

/**
 * Reusable object for mouse state
 */

public class MouseState {

    private boolean altKey;
    private MouseButton button;
    private MouseButton [] buttons;

    private double clientX;
    private double clientY;

    private boolean ctrlKey;
    private boolean metaKey;

    private double movementX;
    private double movementY;
    
    private double offsetX;
    private double offsetY;

    private double pageX;
    private double pageY;

    private double screenX;
    private double screenY;
    
    private boolean shiftKey;

    public MouseState() {
        
    }
    
    public MouseState(boolean altKey, MouseButton button, MouseButton[] buttons, double clientX, double clientY,
            boolean ctrlKey, boolean metaKey, double movementX, double movementY, double offsetX, double offsetY,
            double pageX, double pageY, double screenX, double screenY, boolean shiftKey) {
        
        init(altKey, button, buttons, clientX, clientY, ctrlKey, metaKey, movementX, movementY, offsetX, offsetY, pageX, pageY, screenX, screenY, shiftKey);
    }
    
    public MouseState(MouseState toCopy) {
        this(toCopy.altKey, 
             toCopy.button, Arrays.copyOf(toCopy.buttons, toCopy.buttons.length),
             toCopy.clientX, toCopy.clientY,
             toCopy.ctrlKey, toCopy.metaKey,
             toCopy.movementX, toCopy.movementY, toCopy.offsetX, toCopy.offsetY, toCopy.pageX, toCopy.pageY, toCopy.screenX, toCopy.screenY,
             toCopy.shiftKey);
    }
    

    public void init(boolean altKey, MouseButton button, MouseButton[] buttons, double clientX, double clientY,
                boolean ctrlKey, boolean metaKey, double movementX, double movementY, double offsetX, double offsetY,
                double pageX, double pageY, double screenX, double screenY, boolean shiftKey) {

        this.altKey = altKey;
        this.button = button;
        this.buttons = buttons;
        this.clientX = clientX;
        this.clientY = clientY;
        this.ctrlKey = ctrlKey;
        this.metaKey = metaKey;
        this.movementX = movementX;
        this.movementY = movementY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.pageX = pageX;
        this.pageY = pageY;
        this.screenX = screenX;
        this.screenY = screenY;
        this.shiftKey = shiftKey;
    }

    public final boolean isAltKey() {
        return altKey;
    }
    
    public final MouseButton getButton() {
        return button;
    }

    public final MouseButton[] getButtons() {
        return buttons;
    }

    public final double getClientX() {
        return clientX;
    }

    public final double getClientY() {
        return clientY;
    }

    public final boolean isCtrlKey() {
        return ctrlKey;
    }

    public final boolean isMetaKey() {
        return metaKey;
    }

    public final double getMovementX() {
        return movementX;
    }

    public final double getMovementY() {
        return movementY;
    }

    public final double getOffsetX() {
        return offsetX;
    }

    public final double getOffsetY() {
        return offsetY;
    }

    public final double getScreenX() {
        return screenX;
    }

    public final double getPageX() {
        return pageX;
    }

    public final double getPageY() {
        return pageY;
    }

    public final double getScreenY() {
        return screenY;
    }

    public final boolean getShiftKey() {
        return shiftKey;
    }
    
    public final boolean getCtrlKey() {
        return ctrlKey;
    }
    
    public final boolean getAltKey() {
        return altKey;
    }

    public final boolean getMetaKey() {
        return metaKey;
    }
}

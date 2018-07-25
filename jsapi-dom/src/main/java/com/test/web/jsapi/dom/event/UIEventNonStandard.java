package com.test.web.jsapi.dom.event;

public final class UIEventNonStandard {

    private final int layerX;
    private final int layerY;
    private final int pageX;
    private final int pageY;
    
    public UIEventNonStandard(int layerX, int layerY, int pageX, int pageY) {
        this.layerX = layerX;
        this.layerY = layerY;
        this.pageX = pageX;
        this.pageY = pageY;
    }

    public int getLayerX() {
        return layerX;
    }

    public int getLayerY() {
        return layerY;
    }

    public int getPageX() {
        return pageX;
    }

    public int getPageY() {
        return pageY;
    }
}

package com.test.web.externalevents;

/**
 * Browser-generated events summarized in a simple interface so as to not
 * construct JSAPI classes all around the place
 * 
 * TODO perhaps move to other package so can include this interface
 * without depending on DOM event classes
 */

public interface DocumentExternalEvents {

    
    // Print events
    void onBeforePrint();
    
    void onAfterPrint();
    
    void onClick(MouseState mouseState);
    
}

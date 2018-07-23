package com.test.web.jsapi.common.dom;

/**
 * Non-generic and non DOM specific interface for event propagation, though some methods reflect HTML/DOM semantics
 */

public interface IEvent {

    String getType();
    
    
    /**
     * Set current event target, updated when event bubbles.
     * 
     * @param element the current element
     */
    
    void setCurrentEventTarget(IElement element);
    
    /**
     * Checks after listener invocation whether default-action should not be executed
     * 
     * @return true if should not run default action (like follow a hyperlink if onclick calls preventDefault())
     */
    
    boolean isDefaultPrevented();
    
    /**
     * Checks whether event listener has indicated that event propagation should stop at the current level in the element hierarchy
     *  
     * @return true if stop propagation, false otherwise
     */
    
    boolean isPropagationStopped();
    
}

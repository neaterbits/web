package com.test.web.page.common;

import com.test.web.types.layout.Unit;

/**
 * For dynamically updating element layout, eg from scripts
 * 
 * @param <ELEMENT>
 */

public interface ElementLayoutAccess<ELEMENT> {
    
    // Dynamic update-functions for elements
    // All operations that might affect layout goes through here, so that one can track what requires refreshing
    // Note that CSS changes are re-applied on the outside of this (eg. dynamically changing an element's class)
    
    @Deprecated // CSS style instead?
    void setElementWidth(ELEMENT element, double width, Unit unit);

    @Deprecated // CSS style instead?
    void setElementHeight(ELEMENT element, double height, Unit unit);

    /**
     * Get width of an element. Might cause pending style updates to be applied before returns
     * 
     * @param element the element.
     * 
     * @return current width of element
     */
    
    int getWidth(ELEMENT element);

    /**
     * Get height of an element. Might cause pending style updates to be applied before returns
     * 
     * @param element the element.
     * 
     * @return current height of element
     */
    int getHeight(ELEMENT element);

    ELEMENT getElementAt(double pageX, double pageY);
}

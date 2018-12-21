package com.test.web.document.html.common.elements;

/**
 * Corresponding to JS Element object's method that are retrieved from computed layout 
 * 
 * @param <ELEMENT>
 */

public interface LAYOUTElement<ELEMENT> {
    double getClientHeight(ELEMENT element);
    
    double getClientLeft(ELEMENT element);
    
    double getClientTop(ELEMENT element);
    
    double getClientWidth(ELEMENT element);
    
    double getScrollHeight(ELEMENT element);
    
    double getScrollLeft(ELEMENT element);
    
    void setScrollLeft(ELEMENT element, double left);
    
    double getScrollLeftMax(ELEMENT element);
    
    double getScrollTop(ELEMENT element);

    void setScrollTop(ELEMENT element, double top);
    
    double getScrollTopMax(ELEMENT element);
    
    double getScrollWidth(ELEMENT element);
    
}

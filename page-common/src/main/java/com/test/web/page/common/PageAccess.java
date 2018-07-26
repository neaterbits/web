package com.test.web.page.common;

/**
 * Encapsulation of "page" eg. a tab in a browser window, maps one to one with JS 'window' variable.
 * 
 * @param <ELEMENT> type of DOM element in document model
 */

public interface PageAccess<ELEMENT> extends ElementLayoutAccess<ELEMENT> {


    // ElementAndLayout<ELEMENT> getElementAndLayoutAt(double pageX, double pageY);

    

    // Relayout page if anything changed dynamically, eg. JS set width or height or an element or any other part of an element
    // that has impact on thatt
    boolean relayoutIfChangedDynamically();
    
    void updateView();
    
    
    // This element must be laid out again
    void addPendingRelayout(ELEMENT element);
    
    
    /*
    public static final class ElementAndLayout<E> {
        private final E element;
        private final IElementLayout elementLayout;

        public ElementAndLayout(E element, IElementLayout elementLayout) {
            this.element = element;
            this.elementLayout = elementLayout;
        }

        public E getElement() {
            return element;
        }

        public IElementLayout getElementLayout() {
            return elementLayout;
        }
    }
    */
}

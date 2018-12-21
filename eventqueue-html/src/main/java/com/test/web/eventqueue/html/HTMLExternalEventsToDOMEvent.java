package com.test.web.eventqueue.html;

import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.externalevents.DocumentExternalEvents;
import com.test.web.externalevents.MouseState;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.jsapi.dom.HTMLJSElementFactory;
import com.test.web.jsapi.dom.event.MouseEvent;
import com.test.web.page.common.PageAccess;

abstract class HTMLExternalEventsToDOMEvent<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

    implements DocumentExternalEvents {

    private final DOCUMENT_CONTEXT documentContext;
    private final PageAccess<ELEMENT> pageLayout;
    
    HTMLExternalEventsToDOMEvent(DOCUMENT_CONTEXT documentContext, PageAccess<ELEMENT> pageLayout) {
        this.documentContext = documentContext;
        this.pageLayout = pageLayout;
    }

    abstract void addDOMEventToQueue(Event<ELEMENT> event);

    abstract EventTarget<ELEMENT, ?, ?, ?> getDefaultView();
    
    
    private long timeStamp() {
        return System.currentTimeMillis();
    }
    
    @Override
    public final void onBeforePrint() {
        addDOMEventToQueue(new Event<ELEMENT>(getDefaultView(), HTMLEvent.BEFOREPRINT, timeStamp(), false, true));
    }

    @Override
    public final void onAfterPrint() {
        addDOMEventToQueue(new Event<ELEMENT>(getDefaultView(), HTMLEvent.AFTERPRINT, timeStamp(), false, true));
    }

    @Override
    public final void onClick(MouseState mouseState) {
        
        // Figure out element based on index
        final ELEMENT element = pageLayout.getElementAt(mouseState.getPageX(), mouseState.getPageY());
        
        addDOMEventToQueue(new MouseEvent<>(makeTarget(element), HTMLEvent.CLICK, timeStamp(), false, true,
                0L, false, null, null, null,
                mouseState, null, null));
    }

    private Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> makeTarget(ELEMENT element) {
        return HTMLJSElementFactory.makeElement(element, documentContext);
    }
}

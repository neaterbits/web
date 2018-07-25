package com.test.web.eventqueue.html;

import com.test.web.document.html.common.IDocument;
import com.test.web.eventqueue.common.EventProcessor;
import com.test.web.eventqueue.common.EventQueue;
import com.test.web.externalevents.DocumentExternalEvents;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.dom.Event;
import com.test.web.jsapi.dom.EventTarget;
import com.test.web.page.common.PageAccess;

public final class HTMLEventQueue<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>


    extends HTMLExternalEventsToDOMEvent<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>
    implements DocumentExternalEvents {

    private final EventQueue<Event<ELEMENT>> dispatchQueue;

    // DocumentContext is kept in all JS Element wrapper objects, it keeps all event listeners
    // registered from JS and handles dispatch onto the DOM
    private final DOCUMENT_CONTEXT documentContext;
    
    // Must keep the page layout in order to re-layout based on event changes and to find element for
    // pointer clicks etc
    private final PageAccess<ELEMENT> pageLayout;
    
    private final EventProcessor<Event<ELEMENT>> eventProcessor;
    

    public HTMLEventQueue(DOCUMENT_CONTEXT documentContext, PageAccess<ELEMENT> pageLayout) {
        super(documentContext, pageLayout);
    
        this.documentContext = documentContext;
        this.pageLayout = pageLayout;
        
        this.eventProcessor = new EventProcessor<Event<ELEMENT>>() {
            @Override
            public void processEvent(Event<ELEMENT> event) {
                processDOMEvent(event);
            }
        };
        
        this.dispatchQueue = new EventQueue<>(eventProcessor);
    }
    
    public void processAllEvents() {
        
        // Scripts triggered by events may trigger more events so that is why we run till all scripts have completed
        this.dispatchQueue.procecssEventsUntilQueueEmpty();
        
        
        // Only place dynamic relayou occurs UNLESS some JS code tries
        // to dynamically read the coordinates of an element that is affected by prior dynamic updates.
        // In that case, we have do relayout straight in the place that tries to retrieve current width/height
        // so that we return correct values.
        // Not that relayout does not imply updating screen, that only happens here using double-buffering or similar
        final boolean layouted = pageLayout.relayoutIfChangedDynamically();
        
        if (layouted) {
            // updateView() only done here for dynamic updates
            // may also be done for display resize
            pageLayout.updateView();
        }
    }

    @Override
    void addDOMEventToQueue(Event<ELEMENT> event) {
        this.dispatchQueue.postEvent(event);
    }
    
    @Override
    EventTarget<ELEMENT, ?, ?, ?> getDefaultView() {
        throw new UnsupportedOperationException("TODO - return window object");
    }

    private void processDOMEvent(Event<ELEMENT> event) {

        documentContext.dispatchEvent(event, event.getCurrentTarget());

    }
    
}

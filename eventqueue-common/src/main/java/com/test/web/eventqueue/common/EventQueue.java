package com.test.web.eventqueue.common;

import java.util.LinkedList;

/**
 * Main event-queue, this is where the browser mainloop is
 * Processing events will cause Javascript to be run, which will trigger more events
 * 
 */

public class EventQueue<EVENT> {

    private final EventProcessor<EVENT> eventProcessor;
    private final LinkedList<EVENT> pendingEvents;
    
    public EventQueue(EventProcessor<EVENT> eventProcessor) {
        
        this.eventProcessor = eventProcessor;
        this.pendingEvents = new LinkedList<>();
    }
    
    
    public final void postEvent(EVENT event) {
        if (event == null) {
            throw new IllegalArgumentException("event == null");
        }
        
        if (pendingEvents.contains(event)) {
            throw new IllegalArgumentException("Already posted event");
        }
    }

    public void procecssEventsUntilQueueEmpty() {
        // Just schedule all events
        
        while (!pendingEvents.isEmpty()) {
            final EVENT next = pendingEvents.removeFirst();
            
            eventProcessor.processEvent(next);
        }
    }
}

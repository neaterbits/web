package com.test.web.eventqueue.common;

/**
 * Process events until completion
 * 
 * @param <EVENT>
 */

public interface EventProcessor<EVENT> {

    void processEvent(EVENT event);
    
}

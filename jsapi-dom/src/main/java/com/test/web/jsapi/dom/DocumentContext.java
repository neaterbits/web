package com.test.web.jsapi.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.EventTargetElement;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.common.dom.IEvent;
import com.test.web.jsapi.common.dom.IEventListener;
import com.test.web.page.common.ElementLayoutAccess;
import com.test.web.page.common.PageAccess;


/**
 * Maintains runtime state for events throughout document
 * 
 * This is necessary since API is stateless and so is DOM (we do not store refs to state in the DOM)
 * So eg. event listeners will be mapped here.
 * 
 * We track all element that may have listeners
 * 
 * @param <ELEMENT>
 * @param <ATTRIBUTE>
 */

class DocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>>
	extends JSElementDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT>>
	implements IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT>> {
    
    // This keeps track of all elements that have listeners.

    private final BrowserDefaultEventHandling<ELEMENT, ATTRIBUTE, DOCUMENT> defaultEventHandling;
    
    // TODO Just keep a list for now, can be many listeners so may be too slow
    
    private final List<EventTargetElement<ELEMENT>> allEventTargets;
    private final ElementLayoutAccess<ELEMENT> elementLayoutAccess;

	DocumentContext(DOCUMENT delegate, BrowserDefaultEventHandling<ELEMENT, ATTRIBUTE, DOCUMENT> defaultEventHandling, PageAccess<ELEMENT> pageAccess) {
		super(delegate, new UIDocumentChangeListener<>(pageAccess));
		
		this.defaultEventHandling = defaultEventHandling;
		this.elementLayoutAccess = pageAccess;

		this.allEventTargets = new ArrayList<>();
	}

    @Override
    public void addEventTargetNowWithListeners(EventTargetElement<ELEMENT> target) {
        
        if (target == null) {
            throw new IllegalArgumentException("target == null");
        }
        
        if (allEventTargets.contains(target)) {
            throw new IllegalArgumentException("Already contains target element");
        }

        allEventTargets.add(target);
    }

    @Override
    public void removeEventTargetWithNoMoreListeners(EventTargetElement<ELEMENT> target) {
        if (target == null) {
            throw new IllegalArgumentException("target == null");
        }
        
        if (!allEventTargets.contains(target)) {
            throw new IllegalArgumentException("Does not contain target element");
        }

        allEventTargets.remove(target);
    }

    @Override
    public boolean dispatchEvent(IEvent event, EventTargetElement<ELEMENT> target) {
        
        if (target == null) {
            throw new IllegalArgumentException("target == null");
        }

        // Must still check if this target is registered or not
        return dispatchEvent(event, target.getTargetElement());
    }

    private EventTargetElement<ELEMENT> findTarget(ELEMENT element) {

        for (EventTargetElement<ELEMENT> target : allEventTargets) {

            if (isSameElement(target.getTargetElement(), element)) {
                return target;
            }
        }
        
        return null;
    }
    
    
    // Finds target among those that have registered event handlers, or just creates one in Event.currentTarget
    // Since Event objects are just JS wrappers, we can create these on the fly
    // causes some GC churn but not too much (could perhaps use freelist) 
    private EventTargetElement<ELEMENT> makeTarget(ELEMENT element) {
        return HTMLJSElementFactory.makeElement(element, this);
    }

    // Dispatch an event directly on an ELEMENT, typically done when caused by an internal change
    private boolean dispatchEvent(IEvent event, ELEMENT element) {

        // Must start at document level downwards to the target, just create a list from target upwards first

        if (element == null) {
            throw new IllegalArgumentException("element == null");
        }
        
        final int max = 20;
        
        boolean [] foundTargets = new boolean[max];
        final List<EventTargetElement<ELEMENT>> targets = new ArrayList<>(max);

        
        int idx = 0;
        
        for (ELEMENT curElement = element; curElement != null; curElement = getParentElement(curElement)) {

            final boolean foundTarget;
            
            EventTargetElement<ELEMENT> target = findTarget(curElement);
            if (target != null) {
                foundTarget = true; // might have listeners
            }
            else {
                target = makeTarget(curElement);
                foundTarget = false; // might not have listeners
            }

            if (idx == foundTargets.length) {
                foundTargets = Arrays.copyOf(foundTargets, foundTargets.length * 2);
            }
            
            foundTargets[idx] = foundTarget;
            
            targets.add(target);
            
            
            ++ idx;
        }
        
        // Now have the whole stack.
        
        capturePhase(event, foundTargets, targets, idx);
        
        if (!event.isPropagationStopped()) {
            targetPhase(event, foundTargets[0], targets.get(0));
        }
        
        if (!event.isPropagationStopped()) {
            bubblePhase(event, foundTargets, targets, idx);
        }
        
        return event.isDefaultPrevented();
    }
    
    private void capturePhase(IEvent event, boolean [] foundTargets, List<EventTargetElement<ELEMENT>> targets, int num) {
        for (int i = num - 1; i > 0; -- i) {
            final EventTargetElement<ELEMENT> target = targets.get(i);
            
            event.setCurrentEventTarget(target);

            if (foundTargets[i]) {
                // Target found list of registered, so may have listeners
                final IEventListener listener = target.getEventListener(event);
                
                if (listener != null) {
                    listener.handleEvent(event);
                    
                    if (event.isPropagationStopped()) {
                        break;
                    }
                }
            }
        }
    }
    
    private void targetPhase(IEvent event, boolean foundTarget, EventTargetElement<ELEMENT> target) {

        event.setCurrentEventTarget(target);
        
        if (foundTarget) {
            final IEventListener listener = target.getEventListener(event);
            
            if (listener != null) {
                listener.handleEvent(event);
            }
        }
        
        if (!event.isDefaultPrevented()) {
            // Call on browser to handle event, eg. a click
            defaultEventHandling.onHandleEvent(event, delegate, target.getTargetElement());
        }
    }
    

    private void bubblePhase(IEvent event, boolean [] foundTargets, List<EventTargetElement<ELEMENT>> targets, int num) {

        // Start from 1, since 0 is target phase
        for (int i = 1; i < num; ++ i) {

            final EventTargetElement<ELEMENT> target = targets.get(i);
            
            event.setCurrentEventTarget(target);

            if (foundTargets[i]) {
                // Target found list of registered, so may have listeners
                final IEventListener listener = target.getEventListener(event);
                
                if (listener != null) {
                    listener.handleEvent(event);
                    
                    if (event.isPropagationStopped()) {
                        break;
                    }
                }
            }
        }
    }
    
}

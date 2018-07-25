package com.test.web.jsapi.dom;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.common.dom.IElement;
import com.test.web.jsapi.common.dom.IEvent;
import com.test.web.jsapi.dom.Element;

public class Event<ELEMENT> implements IEvent {
	
	// TODO perhaps use bitflags for fewer memwrites
	private EventTarget<ELEMENT, ?, ?, ?> target ;
	private final HTMLEvent htmlEvent;
	private final long timeStamp;
	private String type;
	private boolean defaultPrevented;
	private boolean bubbles;
	private boolean cancelable;
	private boolean composed;
	private EventTarget<ELEMENT, ?, ?, ?> currentTarget ;
	private final boolean isTrusted;
	
	private EventPropagation propagation;

    public Event(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean composed, boolean isTrusted) {
       this(target, htmlEvent, timeStamp, htmlEvent.getBubbles(), htmlEvent.getCancelable(), composed, isTrusted);
    }

    private Event(EventTarget<ELEMENT, ?, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, boolean bubbles,
			boolean cancelable, boolean composed, boolean isTrusted) {

		this.target = target;
		this.htmlEvent = htmlEvent;
		this.timeStamp = timeStamp;
		this.type = htmlEvent.getType();
		this.defaultPrevented = false;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
		this.composed = composed;
		this.currentTarget = target;
		this.isTrusted = isTrusted;
		this.propagation = EventPropagation.CONTINUE;
	}

	@Deprecated
	public final void initEvent(String type, boolean bubbles, boolean cancelable) {
		this.type = type;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
	}

	public final EventTarget<ELEMENT, ?, ?, ?> getCurrentTarget() {
		return currentTarget;
	}
	
	public final void setCurrentTarget(EventTarget<ELEMENT, ?, ?, ?> currentTarget) {
		this.currentTarget = currentTarget;
	}
	
	public final EventTarget<ELEMENT, ?, ?, ?> getTarget() {
		return target;
	}
	
	public final long getTimeStamp() {
		return timeStamp;
	}
	
	public final String getType() {
		return type;
	}
	
	public final boolean isBubbles() {
		return bubbles;
	}
	
	public final boolean isCancelable() {
		return cancelable;
	}
	
	public final boolean isComposed() {
		return composed;
	}

	public final boolean isScoped() {
		return composed;
	}

	public final boolean isTrusted() {
		return isTrusted;
	}
	
	public final void preventDefault() {
		if (cancelable) {
			this.defaultPrevented = true;
		}
	}
	
	// MS compatibiity
	public final Element<ELEMENT, ?, ?, ?> getSrcElement() {
	    throw new UnsupportedOperationException("TODO");
		//return getTarget();
	}

	public final void stopImmediatePropagation() {
		this.propagation = EventPropagation.STOP_IMMEDIATE;
	}
	
	public final void stopPropagation() {
		this.propagation = EventPropagation.STOP;
	}
	
	final EventPropagation getPropagation() {
		return propagation;
	}
	
	// Obsolete methods
	
	@Deprecated
	public final void preventBubble() {
		stopPropagation();
	}
	
	@Deprecated
	public final void preventCapture() {
		stopPropagation();
	}

	@Override
    @JSTransient
    public final boolean isDefaultPrevented() {
        return defaultPrevented;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @JSTransient
    public final void setCurrentEventTarget(IElement element) {
        setCurrentTarget((Element)element);
    }

    @Override
    @JSTransient
    public final boolean isPropagationStopped() {
        return propagation != null && (propagation == EventPropagation.STOP || propagation == EventPropagation.STOP_IMMEDIATE);
    }
}

package com.test.web.jsapi.dom;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.common.dom.IElement;
import com.test.web.jsapi.common.dom.IEvent;
import com.test.web.jsapi.dom.Element;

public class Event<ELEMENT> implements IEvent {
	
	// TODO perhaps use bitflags for fewer memwrites
	private Element<ELEMENT, ?, ?> target ;
	private final HTMLEvent htmlEvent;
	private final long timeStamp;
	private String type;
	private boolean defaultPrevented;
	private boolean bubbles;
	private boolean cancelable;
	private boolean composed;
	private Element<ELEMENT, ?, ?> currentTarget ;
	private final boolean isTrusted;
	
	private EventPropagation propagation;
	
	public Event(Element<ELEMENT, ?, ?> target, HTMLEvent htmlEvent, long timeStamp, String type, boolean defaultPrevented, boolean bubbles,
			boolean cancelable, boolean composed, Element<?, ?, ?> currentTarget, boolean isTrusted) {

		this.target = target;
		this.htmlEvent = htmlEvent;
		this.timeStamp = timeStamp;
		this.type = type;
		this.defaultPrevented = defaultPrevented;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
		this.composed = composed;
		this.currentTarget = target;
		this.isTrusted = isTrusted;
		this.propagation = EventPropagation.CONTINUE;
	}

	@Deprecated
	public void initEvent(String type, boolean bubbles, boolean cancelable) {
		this.type = type;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
	}

	public Element<ELEMENT, ?, ?> getCurrentTarget() {
		return currentTarget;
	}
	
	public void setCurrentTarget(Element<ELEMENT, ?, ?> currentTarget) {
		this.currentTarget = currentTarget;
	}
	
	public Element<ELEMENT, ?, ?> getTarget() {
		return target;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isBubbles() {
		return bubbles;
	}
	
	public boolean isCancelable() {
		return cancelable;
	}
	
	public boolean isComposed() {
		return composed;
	}

	public boolean isScoped() {
		return composed;
	}

	public boolean isTrusted() {
		return isTrusted;
	}
	
	public void preventDefault() {
		if (cancelable) {
			this.defaultPrevented = true;
		}
	}
	
	// MS compatibiity
	public Element<ELEMENT, ?, ?> getSrcElement() {
		return getTarget();
	}

	public void stopImmediatePropagation() {
		this.propagation = EventPropagation.STOP_IMMEDIATE;
	}
	
	public void stopPropagation() {
		this.propagation = EventPropagation.STOP;
	}
	
	EventPropagation getPropagation() {
		return propagation;
	}
	
	// Obsolete methods
	
	@Deprecated
	public void preventBubble() {
		stopPropagation();
	}
	
	@Deprecated
	public void preventCapture() {
		stopPropagation();
	}

	@Override
    @JSTransient
    public boolean isDefaultPrevented() {
        return defaultPrevented;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @JSTransient
    public void setCurrentEventTarget(IElement element) {
        setCurrentTarget((Element)element);
    }

    @Override
    @JSTransient
    public boolean isPropagationStopped() {
        return propagation != null && (propagation == EventPropagation.STOP || propagation == EventPropagation.STOP_IMMEDIATE);
    }
}

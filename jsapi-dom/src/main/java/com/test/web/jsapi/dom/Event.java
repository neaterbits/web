package com.test.web.jsapi.dom;

public class Event {
	
	// TODO perhaps use bitflags for fewer memwrites
	private Element target ;
	private final long timeStamp;
	private String type;
	private boolean defaultPrevented;
	private boolean bubbles;
	private boolean cancelable;
	private boolean composed;
	private Element currentTarget ;
	private final boolean isTrusted;
	
	private EventPropagation propagation;
	
	public Event(Element target, long timeStamp, String type, boolean defaultPrevented, boolean bubbles,
			boolean cancelable, boolean composed, Element currentTarget, boolean isTrusted) {

		this.target = target;
		this.timeStamp = timeStamp;
		this.type = type;
		this.defaultPrevented = defaultPrevented;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
		this.composed = composed;
		this.currentTarget = currentTarget;
		this.isTrusted = isTrusted;
		this.propagation = EventPropagation.CONTINUE;
	}

	@Deprecated
	public void initEvent(String type, boolean bubbles, boolean cancelable) {
		this.type = type;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
	}

	public boolean isDefaultPrevented() {
		return defaultPrevented;
	}

	public Element getCurrentTarget() {
		return currentTarget;
	}
	
	public void setCurrentTarget(Element currentTarget) {
		this.currentTarget = currentTarget;
	}
	
	public Element getTarget() {
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
	public Element getSrcElement() {
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
}

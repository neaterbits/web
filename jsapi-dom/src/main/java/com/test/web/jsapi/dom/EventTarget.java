package com.test.web.jsapi.dom;

import java.util.HashMap;
import java.util.Map;

import com.test.web.jsapi.common.dom.EventTargetElement;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.common.dom.IEvent;
import com.test.web.jsapi.common.dom.IEventListener;
import com.test.web.jsapi.common.dom.IEventTarget;

public abstract class EventTarget<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
		extends DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT>
		implements IEventTarget, EventTargetElement<ELEMENT> {

	private Map<ListenerKey, IEventListener> listeners;

	EventTarget() {
	}
	
	EventTarget(DOCUMENT document) {
		super(document);
	}

	public EventTarget(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}

	// EventTargetElement
    @Override
    @JSTransient
    public ELEMENT getTargetElement() {
        return getElement();
    }

    @Override
    @JSTransient
    public IEventListener getEventListener(IEvent event) {
        
        // TODO optimize this? perhaps use map for this
        for (Map.Entry<ListenerKey, IEventListener> entry : listeners.entrySet()) {
            if (entry.getKey().type.equals(event.getType())) {
                return entry.getValue();
            }
        }

        return null;
    }

	private void addListener(ListenerKey key, IEventListener listener) {
		
		final boolean wasEmpty;
		
		if (listeners == null) {
			wasEmpty = true;
			this.listeners = new HashMap<>();
		}
		else if (listeners.isEmpty()) {
			wasEmpty = true;
		}
		else {
			wasEmpty = false;
		}
		
		listeners.put(key, listener);
		
		if (wasEmpty) {
			getDocument().addEventTargetNowWithListeners(this);
		}
	}
	
	private void removeListener(ListenerKey key, IEventListener listener) {
		
		final boolean hadEntries = ! listeners.isEmpty();
		
		listeners.remove(key, listener);
		
		if (hadEntries && listeners.isEmpty()) {
			getDocument().removeEventTargetWithNoMoreListeners(this);
		}
	}
	
	@Override
	public final void addEventListener(String type, IEventListener listener) {
		addListener(new ListenerKey(type, null), listener);
	}

	@Override
	public final void addEventListener(String type, IEventListener listener, Options options) {
		addListener(new ListenerKey(type, options), listener);
	}

	@Override
	public final void addEventListener(String type, IEventListener listener, boolean useCapture) {
		final Options options = new Options();
		
		options.setCapture(useCapture);
		
		addListener(new ListenerKey(type, options), listener);
	}

	@Override
	public final void removeEventListener(String type, IEventListener listener) {
		removeListener(new ListenerKey(type, null), listener);
	}

	@Override
	public final void removeEventListener(String type, IEventListener listener, Options options) {
		removeListener(new ListenerKey(type, options), listener);
	}

	@Override
	public final void removeEventListener(String type, IEventListener listener, boolean useCapture) {
		final Options options = new Options();

		options.setCapture(useCapture);
	
		removeListener(new ListenerKey(type, options), listener);
	}

	@Override
	public final boolean dispatchEvent(IEvent event) {
	    
	    boolean canceled;

        canceled = getDocument().dispatchEvent(event, this);
	    
	    return canceled;
	}
	

	private static class ListenerKey {
		private final String type;
		private final Options options;
		
		ListenerKey(String type, Options options) {
			if (type == null) {
				throw new IllegalArgumentException("type == null");
			}
			
			this.type = type;
			this.options = options;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((options == null) ? 0 : options.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ListenerKey other = (ListenerKey) obj;
			if (options == null) {
				if (other.options != null)
					return false;
			} else if (!options.equals(other.options))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}
}

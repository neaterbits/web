package com.test.web.jsapi.dom;

import java.util.HashMap;
import java.util.Map;

import com.test.web.document.common.IDocumentNavigation;

public abstract class EventTarget<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
		extends DocumentAccess<ELEMENT, DOCUMENT>
		implements IEventTarget {

	private final Map<ListenerKey, IEventListener> listeners;
	

	EventTarget() {
		this.listeners = new HashMap<>();
	}


	private void addListener(ListenerKey key, IEventListener listener) {
		
		final boolean wasEmpty = listeners.isEmpty();
		
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
	public final boolean dispatchEvent(Event event) {
		// TODO Bubble events upwards
		throw new UnsupportedOperationException("TODO");
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

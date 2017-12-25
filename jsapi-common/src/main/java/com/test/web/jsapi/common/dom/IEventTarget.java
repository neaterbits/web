package com.test.web.jsapi.common.dom;

public interface IEventTarget {

	void addEventListener(String type, IEventListener listener);

	void addEventListener(String type, IEventListener listener, Options options);
	
	void addEventListener(String type, IEventListener listener, boolean useCapture);
	
	void removeEventListener(String type, IEventListener listener);

	void removeEventListener(String type, IEventListener listener, Options options);
	
	void removeEventListener(String type, IEventListener listener, boolean useCapture);

	boolean dispatchEvent(IEvent event);

	public static class Options {
		private Boolean capture;
		private Boolean once;
		private Boolean passive;
		
		public Boolean getCapture() {
			return capture;
		}
		
		public void setCapture(Boolean capture) {
			this.capture = capture;
		}
		
		public Boolean getOnce() {
			return once;
		}
		
		public void setOnce(Boolean once) {
			this.once = once;
		}
		
		public Boolean getPassive() {
			return passive;
		}
		
		public void setPassive(Boolean passive) {
			this.passive = passive;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((capture == null) ? 0 : capture.hashCode());
			result = prime * result + ((once == null) ? 0 : once.hashCode());
			result = prime * result + ((passive == null) ? 0 : passive.hashCode());
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
			Options other = (Options) obj;
			if (capture == null) {
				if (other.capture != null)
					return false;
			} else if (!capture.equals(other.capture))
				return false;
			if (once == null) {
				if (other.once != null)
					return false;
			} else if (!once.equals(other.once))
				return false;
			if (passive == null) {
				if (other.passive != null)
					return false;
			} else if (!passive.equals(other.passive))
				return false;
			return true;
		}
	}
}

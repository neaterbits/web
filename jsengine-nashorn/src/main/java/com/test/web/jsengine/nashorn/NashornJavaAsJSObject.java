package com.test.web.jsengine.nashorn;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collection;
import java.util.Set;

import com.test.web.jsengine.common.IJSFuntionCallThis;
import com.test.web.jsengine.common.IJavaAsJSObject;
import com.test.web.jsengine.common.JSObjectType;

class NashornJavaAsJSObject implements JSObject, IJSFuntionCallThis {

	private final IJavaAsJSObject delegate;
	
	NashornJavaAsJSObject(IJavaAsJSObject delegate) {

		if (delegate == null) {
			throw new IllegalArgumentException("delegate == null");
		}

		this.delegate = delegate;
	}
	
	@Override
	public IJavaAsJSObject getJavaObject() {
		return delegate;
	}

	@Override
	public Object call(Object thiz, Object... params) {
		
		if (thiz == this) {
			throw new IllegalStateException("was passed same instance");
		}
		Object [] converted = null; // set to null, no allocation unless required
		Object [] toPass = params;
		
		// Convert any common API Java objects wrapped as JS objects into Nashorn JSObject equivalents
		
		for (int i = 0; i < params.length; ++ i) {
			final Object param = params[i];
			
			if (param instanceof ScriptObjectMirror) {
				// Java object to be exposed as JS object, convert
				if (converted == null) {
					converted = new Object[params.length];

					// Copy any params up to now
					for (int j =0; j < i; ++ j) {
						converted[j] = params[j];
					}
					
					toPass = converted;
				}

				converted[i] = new NashornJSAsJavaObject((ScriptObjectMirror)params[i]);
			}
			else if (converted != null) {
				converted[i] = params[i];
			}
		}
		
		return delegate.call(((IJSFuntionCallThis)thiz).getJavaObject(), toPass);
	}

	@Override
	public Object eval(String arg0) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public String getClassName() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Object getMember(String name) {
		return delegate.getProperty(name);
	}

	@Override
	public Object getSlot(int index) {
		return delegate.getSlot(index);
	}

	@Override
	public boolean hasMember(String name) {
		return delegate.hasProperty(name);
	}

	@Override
	public boolean hasSlot(int index) {
		return delegate.hasSlot(index);
	}

	@Override
	public boolean isArray() {
		return delegate.getType() == JSObjectType.ARRAY;
	}

	@Override
	public boolean isFunction() {
		return delegate.getType() == JSObjectType.FUNCTION;
	}

	@Override
	public boolean isInstance(Object arg0) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public boolean isInstanceOf(Object arg0) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public boolean isStrictFunction() {
		return delegate.getType() == JSObjectType.STRICT_FUNCTION;
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Object newObject(Object... arg0) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public void removeMember(String value) {
		delegate.removeProperty(value);
	}

	@Override
	public void setMember(String name, Object value) {
		
		final Object toPass;
		
		if (value instanceof ScriptObjectMirror) {
			toPass = new NashornJSAsJavaObject((ScriptObjectMirror)value);
		}
		else {
			toPass = value;
		}
		
		delegate.setProperty(name, toPass);
	}

	@Override
	public void setSlot(int index, Object value) {
		delegate.setSlot(index, value);
	}

	@Override
	public double toNumber() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException("TODO");
	}
}

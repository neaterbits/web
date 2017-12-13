package com.test.web.jsengine.nashorn;

import com.test.web.jsengine.common.IJSAsJavaObject;
import com.test.web.jsengine.common.IJavaAsJSObject;
import com.test.web.jsengine.common.JSObjectType;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

class NashornJSAsJavaObject implements IJSAsJavaObject {

	private final ScriptObjectMirror delegate;
	
	public NashornJSAsJavaObject(ScriptObjectMirror delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setProperty(String name, Object value) {
		delegate.setMember(name, value);
	}

	@Override
	public Object getProperty(String name) {
		return delegate.getMember(name);
	}

	@Override
	public void removeProperty(String name) {
		delegate.removeMember(name);
	}

	@Override
	public boolean hasProperty(String name) {
		return delegate.hasMember(name);
	}

	@Override
	public JSObjectType getType() {
		
		final JSObjectType ret;

		if (delegate.isFunction()) {
			ret = JSObjectType.FUNCTION;
		}
		else if (delegate.isArray()){
			ret = JSObjectType.ARRAY;
		}
		else if (delegate.isStrictFunction()) {
			ret = JSObjectType.OBJECT;
		}
		else {
			ret = JSObjectType.OBJECT;
		}

		return ret;
	}

	@Override
	public Object getSlot(int index) {
		return delegate.getSlot(index);
	}

	@Override
	public void setSlot(int index, Object value) {
		delegate.setSlot(index, value);
	}

	@Override
	public boolean hasSlot(int index) {
		return delegate.hasSlot(index);
	}

	@Override
	public Object call(Object thiz, Object... params) {
		
		Object [] converted = null; // set to null, no allocation unless required
		Object [] toPass = params;
		
		// Convert any common API Java objects wrapped as JS objects into Nashorn JSObject equivalents
		
		for (int i = 0; i < params.length; ++ i) {
			final Object param = params[i];
			
			if (param instanceof IJavaAsJSObject) {
				// Java object to be exposed as JS object, convert
				if (converted == null) {
					converted = new Object[params.length];

					// Copy any params up to now
					for (int j =0; j < i; ++ j) {
						converted[j] = params[j];
					}
					
					toPass = converted;
				}

				converted[i] = new NashornJavaAsJSObject((IJavaAsJSObject)params[i]);
			}
			else if (converted != null) {
				converted[i] = params[i];
			}
		}
		
		return delegate.call(thiz, toPass);
	}
}

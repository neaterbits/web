package com.test.web.jsengine.common;

public class JavaAsJSObjectAdapter implements IJavaAsJSObject {

	@Override
	public void setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getProperty(String name) {
		throw new UnsupportedOperationException("getProperty: " + name);
	}

	@Override
	public void removeProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JSObjectType getType() {
		return JSObjectType.FUNCTION;
	}

	@Override
	public Object getSlot(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSlot(int index, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasSlot(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object call(Object thiz, Object... params) {
		throw new UnsupportedOperationException();
	}
}

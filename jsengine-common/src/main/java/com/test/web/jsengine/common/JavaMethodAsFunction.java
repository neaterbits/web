package com.test.web.jsengine.common;

public abstract class JavaMethodAsFunction extends JavaAsJSObjectAdapter {

	@Override
	public final JSObjectType getType() {
		return JSObjectType.FUNCTION;
	}

	@Override
	public final void setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Object getProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void removeProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean hasProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Object getSlot(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void setSlot(int index, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean hasSlot(int index) {
		throw new UnsupportedOperationException();
	}
}

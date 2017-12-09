package com.test.web.jsengine.common;

public interface IJSJavaObjectMapping {
	void setProperty(String name, Object value);
	Object getProperty(String name);
	void removeProperty(String name);
	boolean hasProperty(String name);
	
	JSObjectType getType();
	
	// For arrays
	Object getSlot(int index);
	void setSlot(int index, Object value);
	boolean hasSlot(int index);

	// For functions
	Object call(Object thiz, Object ... params);

}

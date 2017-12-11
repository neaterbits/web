package com.test.web.jsengine.common;

//For exposing an object as array as well
public interface IJSObjectAsArray {
	Object getArrayElem(int index);
	
	void setArrayElem(int index, Object value);
	
	long getArrayLength();
}

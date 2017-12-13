package com.test.web.jsengine.common;

public interface ICompiledJS {

	// Run JS with a set of prepared variables
	Object run(JSVariableMap variables) throws JSExecutionException;

}

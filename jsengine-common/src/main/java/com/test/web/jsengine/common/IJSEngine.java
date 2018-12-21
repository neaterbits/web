package com.test.web.jsengine.common;

import java.io.Reader;

public interface IJSEngine extends JSInvocation {

	ICompiledJS compileJS(String string) throws JSCompileException;
	
	ICompiledJS compileJS(Reader reader) throws JSCompileException;
	
	default Object evalJS(String string, JSVariableMap variables) throws JSCompileException, JSExecutionException {
		return compileJS(string).run(variables);
	}
	
	default Object evalJS(Reader reader, JSVariableMap variables) throws JSCompileException, JSExecutionException {
		return compileJS(reader).run(variables);
	}
}

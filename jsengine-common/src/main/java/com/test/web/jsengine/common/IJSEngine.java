package com.test.web.jsengine.common;

import java.io.Reader;

public interface IJSEngine {

	ICompiledJS compileJS(String string) throws JSCompileException;
	
	ICompiledJS compileJS(Reader reader) throws JSCompileException;
}

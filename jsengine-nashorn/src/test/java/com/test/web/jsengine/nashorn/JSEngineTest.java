package com.test.web.jsengine.nashorn;

import com.test.web.jsengine.common.ICompiledJS;
import com.test.web.jsengine.common.IJavaAsJSObject;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.nashorn.NashornEngine;
import com.test.web.jsengine.test.BaseJSEngineTest;

public class JSEngineTest extends BaseJSEngineTest {

	private final NashornEngine engine = new NashornEngine();
	
	@Override
	protected ICompiledJS compileJS(String js) throws JSCompileException, JSExecutionException {
		return engine.compileJS(js);
	}

	@Override
	protected Object makeJavaToJSObject(IJavaAsJSObject javaObject) {
		return new NashornJavaAsJSObject(javaObject);
	}
}


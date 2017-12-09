package com.test.web.jsengine.nashorn;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.test.web.jsengine.common.ICompiledJS;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;

final class NashornCompiledJS implements ICompiledJS {

	private final CompiledScript compiledScript;
	
	NashornCompiledJS(CompiledScript compiledScript) {
		this.compiledScript = compiledScript;
	}

	@Override
	public Object run(JSVariableMap variables) throws JSExecutionException {
		final Bindings bindings = new SimpleBindings();
		
		variables.forEachKeyValue((key, value) -> bindings.put(key, new NashornJavaAsJSObject(value)));
		
		final Object result;
		try {
			result = compiledScript.eval(bindings);
		} catch (ScriptException ex) {
			throw new JSExecutionException("Caught exception while executing JS", ex);
		}
		
		return result;
	}
}

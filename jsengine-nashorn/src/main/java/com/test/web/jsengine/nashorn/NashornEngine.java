package com.test.web.jsengine.nashorn;

import java.io.Reader;
import java.io.StringReader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.test.web.jsengine.common.ICompiledJS;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.IJSEngine;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collection;
import java.util.Set;

import com.test.web.jsengine.common.IJSFuntionCallThis;
import com.test.web.jsengine.common.IJavaAsJSObject;
import com.test.web.jsengine.common.JSObjectType;

@SuppressWarnings("restriction")
public class NashornEngine implements IJSEngine {

	private final ScriptEngine engine;
	
	public NashornEngine() {
		final ScriptEngineManager factory = new ScriptEngineManager();
		
		this.engine = factory.getEngineByName("nashorn");
	}
	
	@Override
	public ICompiledJS compileJS(String string) throws JSCompileException {
		return compileJS(new StringReader(string));
	}

	@Override
	public ICompiledJS compileJS(Reader reader) throws JSCompileException {
		
		final CompiledScript compiled;
		
		try {
			compiled = ((Compilable)engine).compile(reader);
		} catch (ScriptException ex) {
			throw new JSCompileException("Failed to compile script", ex);
		}
		
		return new NashornCompiledJS(compiled);
	}

    @Override
    public Object callFunction(Object function, Object... parameters) {
        
        final Object result;
        
        if (function instanceof ScriptObjectMirror) {
            result = ((ScriptObjectMirror)function).call(null, parameters);
        }
        else {
            throw new UnsupportedOperationException("Not a JS function");
        }
        
        return result;
    }

    @Override
    public Object callFunctionWithThis(Object function, Object t, Object... parameters) {
        final Object result;
        
        if (function instanceof ScriptObjectMirror) {
            result = ((ScriptObjectMirror)function).call(t, parameters);
        }
        else {
            throw new UnsupportedOperationException("Not a JS function");
        }
        
        return result;
    }
}

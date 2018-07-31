package com.test.web.jsapi.dom;

import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.jsengine.nashorn.NashornEngine;

import junit.framework.TestCase;

public class BaseJSExecutingTest extends TestCase {

    protected final IJSEngine getJSEngine() {
        return new NashornEngine();
    }
    
    protected final Object evalJS(String js, JSVariableMap varMap) throws JSCompileException, JSExecutionException {
        return getJSEngine().evalJS(js, varMap);
    }
}

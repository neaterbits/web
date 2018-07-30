package com.test.web.jsapi.dom;

import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.nashorn.NashornEngine;

import junit.framework.TestCase;

public class BaseJSExecutingTest extends TestCase {

    protected final IJSEngine getJSEngine() {
        return new NashornEngine();
    }
}

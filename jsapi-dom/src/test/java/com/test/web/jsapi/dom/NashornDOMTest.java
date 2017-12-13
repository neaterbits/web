package com.test.web.jsapi.dom;

import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.nashorn.NashornEngine;

public class NashornDOMTest extends BaseDOMTest {

	@Override
	IJSEngine getJSEngine() {
		return new NashornEngine();
	}
}

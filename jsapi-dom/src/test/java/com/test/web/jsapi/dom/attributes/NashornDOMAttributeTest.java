package com.test.web.jsapi.dom.attributes;

import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.nashorn.NashornEngine;

public class NashornDOMAttributeTest extends BaseDOMAttributeTest {

	@Override
	IJSEngine getJSEngine() {
		return new NashornEngine();
	}
}

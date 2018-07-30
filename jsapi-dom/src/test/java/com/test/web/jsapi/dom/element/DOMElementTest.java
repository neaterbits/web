package com.test.web.jsapi.dom.element;

import com.test.web.jsapi.dom.BaseJSDOMTest;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.parse.common.ParserException;

/**
 * Tests various features of the Element JS object
 */

public class DOMElementTest extends BaseJSDOMTest {

    public void testGetElementId() {
        
    }
    
    private static String wrapHTML(String html) {
        return "<html><body>" + html + "</body></html>";
    }
    
    public void testClassList() throws JSCompileException, JSExecutionException, ParserException {
        
        final String html = wrapHTML("<div id='the_id' class='aabc def hgi'></div>");
        
        final JSVariableMap varMap = prepareVarMap(html);
        
        getJSEngine().evalJS("document.getElementById('the_id').classList", varMap);
    }
    
}

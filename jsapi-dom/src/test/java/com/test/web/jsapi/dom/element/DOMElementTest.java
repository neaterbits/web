package com.test.web.jsapi.dom.element;

import com.test.web.jsapi.dom.BaseJSDOMTest;
import com.test.web.jsapi.dom.JSRef;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.parse.common.ParserException;
import com.test.web.testdata.TestData;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests various features of the Element JS object
 */

public class DOMElementTest extends BaseJSDOMTest {

    public void testGetElementId() {
        
    }
    
    private static String wrapHTML(String html) {
        return TestData.DOCTYPE + "<html><body>" + html + "</body></html>";
    }
    
    public void testClassList() throws JSCompileException, JSExecutionException, ParserException {
        
        final String html = wrapHTML("<div id='the_id' class='abc  def   ghi'></div>");
        
        final JSVariableMap varMap = prepareVarMap(html);
        
        assertThat(getClassListItem("the_id", varMap, 0)).isEqualTo("abc");
        assertThat(getClassListItem("the_id", varMap, 1)).isEqualTo("def");
        assertThat(getClassListItem("the_id", varMap, 2)).isEqualTo("ghi");

        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def ghi");
        
        assertThat(evalJS("document.getElementById('the_id').classList.contains('abc')", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('def')", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('ghi')", varMap)).isEqualTo(true);

        assertThat(evalJS("document.getElementById('the_id').classList.contains('bc')", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('ef')", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('gh')", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('xyz')", varMap)).isEqualTo(false);

        // add
        evalJS("document.getElementById('the_id').classList.add('xyz')", varMap);
        assertThat(evalJS("document.getElementById('the_id').classList.contains('xyz')", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(4);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def ghi xyz");

        // remove
        evalJS("document.getElementById('the_id').classList.remove('ghi')", varMap);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def xyz");

        // replace
        assertThat(evalJS("document.getElementById('the_id').classList.replace('xzy', 'zyx')", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.replace('xyz', 'zyx')", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx");

        // toggle
        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy')", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx xzy");
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(4);

        
        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy')", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx");

        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy', true)", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(4);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx xzy");
        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy', true)", varMap)).isEqualTo(true);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(4);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx xzy");

        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy', false)", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx");
        assertThat(evalJS("document.getElementById('the_id').classList.toggle('xzy', false)", varMap)).isEqualTo(false);
        assertThat(evalJS("document.getElementById('the_id').classList.length", varMap)).isEqualTo(3);
        assertThat(evalJS("document.getElementById('the_id').classList.value", varMap)).isEqualTo("abc def zyx");
        
        
        // Try to remove an attribute
        //"document.getElementById('the_id').classList.", varMap
    }
    

    private String getClassListItem(String elementId, JSVariableMap varMap, int itemNo) throws JSCompileException, JSExecutionException {
        @SuppressWarnings("unchecked")
        final JSRef<String> item = (JSRef<String>)evalJS("document.getElementById('" + elementId + "').classList.item(" + itemNo + ")", varMap);

        return item.getRef();
    }
}

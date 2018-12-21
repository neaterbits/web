package com.test.web.jsapi.dom;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.jsapi.common.dom.IEvent;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.util.ParseHTML;

public class BaseJSDOMTest extends BaseJSExecutingTest {

    protected final JSVariableMap prepareVarMap(String html) throws ParserException {
        final CSSContext<OOCSSRule> cssContext = new CSSContext<>();
        
        final OOHTMLDocument document = ParseHTML.parseOOHTMLDocument(
                html,
                (charInput, tokenizer) -> 
                null
                //DocumentParser.parseCSS(charInput, tokenizer, cssContext, new OOCSSDocument())
                );

        // Now run some JS tests
        final JSVariableMap varMap = new JSVariableMap();

        final BrowserDefaultEventHandling<OOTagElement, OOAttribute, OOHTMLDocument> browserEventHandling = new BrowserDefaultEventHandling<OOTagElement, OOAttribute, OOHTMLDocument>() {
            @Override
            public boolean onHandleEvent(IEvent event, OOHTMLDocument document, OOTagElement element) {
                return false;
            }
        };
        
        final TestDocumentContext documentContext = new TestDocumentContext(
                document,
                browserEventHandling,
                new TestPageAccess<>(),
                getJSEngine());

        varMap.addReflected("document", new TestDOMDocument(documentContext));

        return varMap;
    }

}

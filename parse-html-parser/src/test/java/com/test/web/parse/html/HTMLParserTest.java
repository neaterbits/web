package com.test.web.parse.html;

import java.io.IOException;

import com.test.web.css.oo.OOCSSDocument;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.io._long.StringBuffers;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.SimpleLoadStream;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.IParse;
import com.test.web.parse.common.ParserException;

import junit.framework.TestCase;

public class HTMLParserTest extends TestCase {

    private static class TestParser extends HTMLParser<OOTagElement, OOCSSDocument, OOHTMLDocument> {

        public TestParser(CharInput input, Tokenizer tokenizer, IHTMLParserListener<OOTagElement> htmlListener,
                IHTMLStyleParserListener<OOTagElement, OOHTMLDocument> styleAttributeListener,
                IParse<OOCSSDocument> parseStyleDocument) {
            super(input, tokenizer, htmlListener, styleAttributeListener, parseStyleDocument);
        }
    }
    
    private static TestParser createParser(String html) {
        
        final StringBuffers input = new StringBuffers(new SimpleLoadStream(html));
        
        
        final OOHTMLDocument document = new OOHTMLDocument();
        
        return new TestParser(input, input, document, null, (c, t) -> null);
    }
    
    
    
    public void testSimpleDocument() throws IOException, ParserException {
        
        final TestParser parser = createParser("<html></html>");
        
        parser.parseHTMLFile();
        
    }
    
}

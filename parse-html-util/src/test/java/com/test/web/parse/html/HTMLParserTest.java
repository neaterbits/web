package com.test.web.parse.html;

import java.io.IOException;

import com.neaterbits.util.io.loadstream.SimpleLoadStream;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.IParse;
import com.neaterbits.util.parse.ParserException;
import com.test.util.io.buffers.StringBuffers;
import com.test.web.css.oo.OOCSSBase;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.parse.html.util.ParseHTML;

public class HTMLParserTest 
        extends HTMLBaseParserTest<OOTagElement, OOAttribute, OOCSSBase, OOHTMLDocument> {

    
    @Override
    OOHTMLDocument parseHTMLDocument(String html) throws ParserException {
        return ParseHTML.parseOOHTMLDocument(html, null);
    }



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

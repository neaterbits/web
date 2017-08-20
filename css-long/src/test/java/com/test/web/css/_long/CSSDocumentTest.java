package com.test.web.css._long;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;

import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;

import junit.framework.TestCase;


public class CSSDocumentTest extends TestCase {

	public void testParser() throws IOException, ParserException {
		
		final String css = 
				
"h1 {\n" +
"  width : 20%;\n" +
"  height : 100px;\n" +
"  background-color : #AABBCC;\n" +
"}\n" +

"#an_element {\n" +
"  margin-left : 10;\n" +
"  margin-right : auto;\n" +

"  float: left;\n" +
"  position: relative;\n" +
"}\n" +

".a_class {\n" +
"  position : absolute;\n" +
"  float : right;\n" +
"  padding-top : 30px;\n" +
"}\n";				
		
		
		final LongCSSDocument doc = new LongCSSDocument();
		
		final StringBuffers buffers = new StringBuffers(new ByteArrayInputStream(css.getBytes()));
		
		
		final CSSParser<LongTokenizer, Void> parser = new CSSParser<>(buffers, doc);
		
		parser.parseCSS();
	}
	
}

package com.test.web.document.html.test;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.css.ICSSDocumentParserListener;

public class DocumentParser {

	public static <STYLE_ELEMENT, STYLE_DOCUMENT extends ICSSDocumentParserListener<STYLE_ELEMENT, Void>>
			STYLE_DOCUMENT parseCSS(CharInput charInput, Tokenizer tokenizer, CSSContext<STYLE_ELEMENT> cssContext, STYLE_DOCUMENT styleDocument) throws IOException, ParserException {
		
		final CSSParser<Void> cssParser = new CSSParser<>(charInput, tokenizer, styleDocument);

		// Just parse the CSS straight away
		cssParser.parseCSS();
		
		cssContext.addDocument(styleDocument);

		return styleDocument;
	}
}

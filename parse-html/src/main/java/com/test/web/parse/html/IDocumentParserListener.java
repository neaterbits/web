package com.test.web.parse.html;

import com.test.web.document.html.common.IDocument;
import com.test.web.io.common.Tokenizer;

public interface IDocumentParserListener<ELEMENT, ATTRIBUTE, TOKENIZER extends Tokenizer>
		extends IDocument<ELEMENT, ATTRIBUTE>, IHTMLParserListener<ELEMENT, TOKENIZER> {

	IHTMLStyleParserListener<ELEMENT, TOKENIZER> getStyleParserListener();
}

package com.test.web.parse.html;

import com.test.web.document.common.IDocument;
import com.test.web.io.common.Tokenizer;

public interface IDocumentParserListener<ELEMENT, TOKENIZER extends Tokenizer>
		extends IDocument<ELEMENT>, IHTMLParserListener<ELEMENT, TOKENIZER> {

	IHTMLStyleParserListener<ELEMENT, TOKENIZER> getStyleParserListener();
}

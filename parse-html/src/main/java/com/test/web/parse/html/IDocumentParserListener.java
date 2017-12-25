package com.test.web.parse.html;

import com.test.web.document.html.common.IDocument;

public interface IDocumentParserListener<ELEMENT, ATTRIBUTE>
		extends IDocument<ELEMENT, ATTRIBUTE>, IHTMLParserListener<ELEMENT> {

	IHTMLStyleParserListener<ELEMENT> getStyleParserListener();
}

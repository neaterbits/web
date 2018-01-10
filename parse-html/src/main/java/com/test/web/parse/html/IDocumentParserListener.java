package com.test.web.parse.html;

import com.test.web.document.html.common.IDocument;

public interface IDocumentParserListener<ELEMENT, ATTRIBUTE, CSS_LISTENER_CONTEXT>
		extends IDocument<ELEMENT, ATTRIBUTE>, IHTMLParserListener<ELEMENT> {

	IHTMLStyleParserListener<ELEMENT, CSS_LISTENER_CONTEXT> getStyleParserListener();
}

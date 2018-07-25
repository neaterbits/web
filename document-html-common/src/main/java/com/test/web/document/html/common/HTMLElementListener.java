package com.test.web.document.html.common;

import com.test.web.document.common.IElementListener;

public interface HTMLElementListener<ELEMENT, ATTRIBUTE, PARAM, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>>
	extends IElementListener<ELEMENT, HTMLElement, DOCUMENT, PARAM>{

}

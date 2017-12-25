package com.test.web.parse.css;

import com.test.web.css.common.ICSSDocument;

public interface ICSSDocumentParserListener<ELEMENT, CONTEXT>
	extends ICSSDocument<ELEMENT>, CSSParserListener<CONTEXT>{

}

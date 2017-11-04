package com.test.web.parse.css;

import com.test.web.css.common.ICSSDocument;
import com.test.web.io.common.Tokenizer;

public interface ICSSDocumentParserListener<ELEMENT, TOKENIZER extends Tokenizer, CONTEXT>
	extends ICSSDocument<ELEMENT>, CSSParserListener<TOKENIZER, CONTEXT>{

}

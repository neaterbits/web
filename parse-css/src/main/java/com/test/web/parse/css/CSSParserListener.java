package com.test.web.parse.css;

import com.test.web.css.common.CSSEntity;
import com.test.web.css.common.CSSUnit;
import com.test.web.io.common.Tokenizer;

public interface CSSParserListener<TOKENIZER extends Tokenizer, CONTEXT> {

	CONTEXT onEntityStart(CSSEntity entity, String id);
	
	void onEntityEnd(CONTEXT context);
	
	void onWidth(CONTEXT context, int width, CSSUnit unit);
	
	void onHeight(CONTEXT context, int width, CSSUnit unit);
}

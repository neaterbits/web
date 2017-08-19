package com.test.web.css._long;


import com.test.web.css.common.CSSEntity;
import com.test.web.css.common.CSSUnit;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.css.CSSParserListener;

public class LongCSSDocument implements CSSParserListener<LongTokenizer, long[]> {

	@Override
	public long[] onEntityStart(CSSEntity entity, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEntityEnd(long[] context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWidth(long [] context, int width, CSSUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeight(long[] context, int width, CSSUnit unit) {
		// TODO Auto-generated method stub
		
	}
}

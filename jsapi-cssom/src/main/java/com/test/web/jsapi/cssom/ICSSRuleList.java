package com.test.web.jsapi.cssom;

import com.test.web.jsengine.common.IJSObjectAsArray;

public interface ICSSRuleList<RULE> extends IJSObjectAsArray {

	int getLength();
	
	CSSRule<RULE> item(int index);

	@Override
	default Object getArrayElem(int index) {
		return item(index);
	}

	@Override
	default void setArrayElem(int index, Object value) {
		throw new UnsupportedOperationException("read-only");
	}

	@Override
	default long getArrayLength() {
		return getLength();
	}
}

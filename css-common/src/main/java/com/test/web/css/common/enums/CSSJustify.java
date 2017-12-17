package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.Justify;

public enum CSSJustify {
	NONE(Justify.NONE),
	SIZE(Justify.SIZE),
	AUTO(Justify.AUTO),
	INITIAL(null),
	INHERIT(null);
	
	private final Justify layoutJustify;

	private CSSJustify(Justify layoutJustify) {
		this.layoutJustify = layoutJustify;
	}

	public Justify getLayoutJustify() {
		return layoutJustify;
	}
}

package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.Justify;
import com.test.web.types.IEnum;

public enum CSSJustify implements IEnum {
	NONE("none", Justify.NONE),
	SIZE(null, Justify.SIZE),
	SIZE_COMPACTED(null, Justify.SIZE),
	AUTO("auto", Justify.AUTO),
	INITIAL("initial", null),
	INHERIT("inherit", null);
	
	private final String name;
	private final Justify layoutJustify;

	private CSSJustify(String name, Justify layoutJustify) {
		this.name = name;
		this.layoutJustify = layoutJustify;
	}

	public Justify getLayoutJustify() {
		return layoutJustify;
	}

	@Override
	public String getName() {
		return name;
	}
}

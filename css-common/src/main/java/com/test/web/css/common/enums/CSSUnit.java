package com.test.web.css.common.enums;

import com.test.web.types.IEnum;
import com.test.web.types.layout.Unit;

public enum CSSUnit implements IEnum {
	PX("px", Unit.PX),
	EM("em", Unit.EM),
	PCT("%", Unit.PCT);

	private final String name;
	private final Unit layoutUnit;

	private CSSUnit(String name, Unit layoutUnit) {
		this.name = name;
		this.layoutUnit = layoutUnit;
	}

	public Unit getLayoutUnit() {
		return layoutUnit;
	}

	@Override
	public String getName() {
		return name;
	}
}

package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.Unit;

public enum CSSUnit {
	PX(Unit.PX),
	EM(Unit.EM),
	PCT(Unit.PCT);

	private final Unit layoutUnit;

	private CSSUnit(Unit layoutUnit) {
		this.layoutUnit = layoutUnit;
	}

	public Unit getLayoutUnit() {
		return layoutUnit;
	}
}

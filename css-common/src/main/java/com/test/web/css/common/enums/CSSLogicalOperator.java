package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSLogicalOperator implements IEnum {

	AND("and"),
	OR("or"),
	NOT("not");

	private final String name;

	private CSSLogicalOperator(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}

package com.test.web.css.common;

import com.test.web.types.IOptionEnum;

public enum CSSDisplay implements IOptionEnum {
	INLINE("inline"),
	BLOCK("block"),
	FLEX("flex"),
	INLINE_BLOCK("inline-block"),
	INLINE_FLEX("inline-flex"),
	INLINE_TABLE("inline-table"),
	LIST_ITEM("list-item"),
	RUN_IN("run-in"),
	TABLE("table"),
	TABLE_CAPTION("table-caption"),
	TABLE_COLUMN_GROUP("table-column-group"),
	TABLE_HEADER_GROUP("table-header-group"),
	TABLE_FOOTER_GROUP("table-footer-group"),
	TABLE_ROW_GROUP("table-row-group"),
	TABLE_CELL("table-cell"),
	TABLE_COLUMN("table-column"),
	TABLE_ROW("table-row"),
	NONE("none"),
	INITIAL("initial"),
	INHERIT("inherit")

	;

	private final String name;

	private CSSDisplay(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}

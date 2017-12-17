package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.Display;
import com.test.web.types.IOptionEnum;

public enum CSSDisplay implements IOptionEnum {
	INLINE("inline", Display.INLINE),
	BLOCK("block", Display.BLOCK),
	FLEX("flex", Display.FLEX),
	INLINE_BLOCK("inline-block", Display.INLINE_BLOCK),
	INLINE_FLEX("inline-flex", Display.INLINE_FLEX),
	INLINE_TABLE("inline-table", Display.INLINE_TABLE),
	LIST_ITEM("list-item", Display.LIST_ITEM),
	RUN_IN("run-in", Display.RUN_IN),
	TABLE("table", Display.TABLE),
	TABLE_CAPTION("table-caption", Display.TABLE_CAPTION),
	TABLE_COLUMN_GROUP("table-column-group", Display.TABLE_COLUMN_GROUP),
	TABLE_HEADER_GROUP("table-header-group", Display.TABLE_HEADER_GROUP),
	TABLE_FOOTER_GROUP("table-footer-group", Display.TABLE_FOOTER_GROUP),
	TABLE_ROW_GROUP("table-row-group", Display.TABLE_ROW_GROUP),
	TABLE_CELL("table-cell", Display.TABLE_CELL),
	TABLE_COLUMN("table-column", Display.TABLE_COLUMN),
	TABLE_ROW("table-row", Display.TABLE_ROW),
	NONE("none", Display.NONE),
	INITIAL("initial", null),
	INHERIT("inherit", null)

	;

	private final String name;
	private final Display layoutDisplay;

	private CSSDisplay(String name, Display layoutDisplay) {
		this.name = name;
		this.layoutDisplay = layoutDisplay;
	}

	@Override
	public String getName() {
		return name;
	}

	public Display getLayoutDisplay() {
		return layoutDisplay;
	}
}

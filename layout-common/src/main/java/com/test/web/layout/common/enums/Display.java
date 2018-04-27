package com.test.web.layout.common.enums;

public enum Display {
	INLINE,
	BLOCK,
	FLEX,
	INLINE_BLOCK,
	INLINE_FLEX,
	INLINE_TABLE,
	LIST_ITEM,
	RUN_IN,
	TABLE,
	TABLE_CAPTION,
	TABLE_COLUMN_GROUP,
	TABLE_HEADER_GROUP,
	TABLE_FOOTER_GROUP,
	TABLE_ROW_GROUP,
	TABLE_CELL,
	TABLE_COLUMN,
	TABLE_ROW,
	NONE,
	INITIAL,
	INHERIT;
	
	public boolean isInline() {
		final boolean isInline;
		
		switch (this) {
		case INLINE:
		case INLINE_BLOCK:
		case INLINE_FLEX:
		case INLINE_TABLE:
			isInline = true;
			break;
			
		case NONE:
		case INITIAL:
		case INHERIT:
			throw new IllegalStateException("Called for non-display value: " + this);
			
		default:
			isInline = false;
			break;
			
		}

		return isInline;
	}

	public boolean isBlock() {
		final boolean isBlock;
		
		switch (this) {
		case BLOCK:
		case INLINE_BLOCK:
			isBlock = true;
			break;
			
		case NONE:
		case INITIAL:
		case INHERIT:
			throw new IllegalStateException("Called for non-display value: " + this);
			
		default:
			isBlock = false;
			break;
			
		}

		return isBlock;
	}
}

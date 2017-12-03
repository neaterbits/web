package com.test.web.ui.common;

// Layout data for box layout
public abstract class BoxLayoutData extends UILayoutData {

	private final Alignment alignment;
	private final boolean expandHorizontally;
	private final boolean expandVertically;

	BoxLayoutData(Alignment alignment, boolean expandHorizontally, boolean expandVertically) {
		this.alignment = alignment;
		this.expandHorizontally = expandHorizontally;
		this.expandVertically = expandVertically;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public boolean isExpandHorizontally() {
		return expandHorizontally;
	}

	public boolean isExpandVertically() {
		return expandVertically;
	}
}

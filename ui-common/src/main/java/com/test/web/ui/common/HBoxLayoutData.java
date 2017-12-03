package com.test.web.ui.common;

public class HBoxLayoutData extends BoxLayoutData {

	public HBoxLayoutData(Alignment alignment, boolean expandVertically) {
		super(alignment, false, expandVertically);
	}
	
	public HBoxLayoutData(boolean expandHorizontally, boolean expandVertically) {
		super(null, expandHorizontally, expandVertically);
	}
}

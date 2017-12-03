package com.test.web.ui.common;

public class VBoxLayoutData extends BoxLayoutData {

	public VBoxLayoutData(Alignment alignment, boolean expandHorizontally) {
		super(alignment, expandHorizontally, false);
	}
	
	public VBoxLayoutData(boolean expandHorizontally, boolean expandVertically) {
		super(null, expandHorizontally, expandVertically);
	}
}

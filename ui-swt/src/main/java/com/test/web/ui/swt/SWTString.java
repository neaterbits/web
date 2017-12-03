package com.test.web.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.test.web.ui.common.IUIString;

final class SWTString extends Text implements IUIString {

	public SWTString(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {

	}
}

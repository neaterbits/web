package com.test.web.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import com.test.web.ui.common.IUICanvas;
import com.test.web.ui.common.IUIString;
import com.test.web.ui.common.IUIStringListener;
import com.test.web.ui.common.IUIWidgets;
import com.test.web.ui.common.UILayoutData;

abstract class SWTPanel extends SWTContainer implements IUIWidgets {

	public SWTPanel(Composite container) {
		super(container);
	}

	@Override
	public final IUIString createString(UILayoutData layoutData, IUIStringListener listener) {
		final SWTString string = new SWTString(getContainer(), SWT.BORDER);
		
		string.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				listener.onValueChanged(string.getText());
			}
		});
		
		add(layoutData, string);
		
		return string;
	}

	@Override
	public final IUICanvas createCanvas(UILayoutData layoutData) {
		final SWTCanvas canvas = new SWTCanvas(getContainer(), SWT.BORDER);
		
		add(layoutData, canvas);
		
		return canvas;
	}
}

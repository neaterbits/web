package com.test.web.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.test.web.ui.common.HBoxLayoutData;
import com.test.web.ui.common.IUIHBox;
import com.test.web.ui.common.UILayoutData;

final class SWTHBox extends SWTBox implements IUIHBox {

	private final GridLayout gridLayout;
	
	SWTHBox(Composite container, GridLayout gridLayout) {
		super(container);
		
		this.gridLayout = gridLayout;
	}

	@Override
	void add(UILayoutData layoutData, Control component) {
		// as many columns as there are widgets
		++ this.gridLayout.numColumns;
		
		
		final HBoxLayoutData hboxLayoutData = (HBoxLayoutData)layoutData;
		
		final GridData gridData = new GridData();

		if (hboxLayoutData.isExpandHorizontally()) {
			// Expands vertically so no alignment
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = SWT.FILL;
		}
		else {
			gridData.grabExcessHorizontalSpace = false;
			gridData.horizontalAlignment = alignmentToSWT(hboxLayoutData.getAlignment());
		}
		
		gridData.grabExcessVerticalSpace = hboxLayoutData.isExpandVertically();
		gridData.verticalAlignment = hboxLayoutData.isExpandVertically()
				? SWT.FILL
				: SWT.BEGINNING;

		component.setLayoutData(gridData);
	}
}

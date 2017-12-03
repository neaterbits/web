package com.test.web.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.test.web.ui.common.IUIVBox;
import com.test.web.ui.common.UILayoutData;
import com.test.web.ui.common.VBoxLayoutData;

final class SWTVBox extends SWTBox implements IUIVBox {

	SWTVBox(Composite container) {
		super(container);
	}

	@Override
	void add(UILayoutData layoutData, Control component) {
		final VBoxLayoutData vboxLayoutData = (VBoxLayoutData)layoutData;
		
		final GridData gridData = new GridData();

		if (vboxLayoutData.isExpandVertically()) {
			// Expands vertically so no alignment
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.FILL;
		}
		else {
			gridData.grabExcessVerticalSpace = false;
			gridData.verticalAlignment = alignmentToSWT(vboxLayoutData.getAlignment());
		}
		
		gridData.grabExcessHorizontalSpace = vboxLayoutData.isExpandHorizontally();
		gridData.horizontalAlignment = vboxLayoutData.isExpandHorizontally()
				? SWT.FILL
				: SWT.BEGINNING;
		
		component.setLayoutData(gridData);
	}
}

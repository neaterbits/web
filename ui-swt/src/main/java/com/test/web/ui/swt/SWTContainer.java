package com.test.web.ui.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.test.web.ui.common.IUIContainers;
import com.test.web.ui.common.IUIHBox;
import com.test.web.ui.common.IUIVBox;
import com.test.web.ui.common.UILayoutData;

abstract class SWTContainer implements IUIContainers {

	private final Composite container;
	
	SWTContainer(Composite container) {
		this.container = container;
	}

	@Override
	public final IUIHBox createHBox(UILayoutData layoutData) {
		
		final Composite subContainer = new Composite(container,0);
		
		final GridLayout boxLayout = new GridLayout();
		
		subContainer.setLayout(boxLayout);
		
		add(layoutData, subContainer);
		
		return new SWTHBox(subContainer, boxLayout);
	}

	@Override
	public final IUIVBox createVBox(UILayoutData layoutData) {
		final Composite subContainer = new Composite(container, 0);
		
		final GridLayout boxLayout = new GridLayout();
		
		boxLayout.numColumns = 1;

		subContainer.setLayout(boxLayout);

		add(layoutData, subContainer);
		
		return new SWTVBox(subContainer);
	}

	final Composite getContainer() {
		return container;
	}
	
	abstract void add(UILayoutData layoutData, Control component);
}

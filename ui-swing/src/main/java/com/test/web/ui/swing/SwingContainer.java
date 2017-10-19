package com.test.web.ui.swing;

import java.awt.Container;

import com.test.web.ui.common.IUIContainers;
import com.test.web.ui.common.IUIHBox;
import com.test.web.ui.common.IUIVBox;

abstract class SwingContainer implements IUIContainers {

	private final Container container;
	
	SwingContainer(Container container) {
		this.container = container;
	}

	@Override
	public final IUIHBox createHBox() {
		
		//final BoxLayout boxLayout = new BoxLayout(target, axis)
		
		//final JPanel subContainer = new JPanel(boxLayout);
		
		return null;
	}

	@Override
	public final IUIVBox createVBox() {
		// TODO Auto-generated method stub
		return null;
	}

	final Container getContainer() {
		return container;
	}
}

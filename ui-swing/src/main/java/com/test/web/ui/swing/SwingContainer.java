package com.test.web.ui.swing;

import java.awt.Component;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

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
		
		final JPanel subContainer = new JPanel();
		
		final BoxLayout boxLayout = new BoxLayout(subContainer, BoxLayout.X_AXIS);
		
		subContainer.setLayout(boxLayout);
		
		add(subContainer);
		
		return new SwingHBox(subContainer);
	}

	@Override
	public final IUIVBox createVBox() {
		final JPanel subContainer = new JPanel();
		
		final BoxLayout boxLayout = new BoxLayout(subContainer, BoxLayout.Y_AXIS);

		subContainer.setLayout(boxLayout);

		add(subContainer);
		
		return new SwingVBox(subContainer);
	}

	final Container getContainer() {
		return container;
	}
	
	final void add(Component component) {
		container.add(component);
	}
}

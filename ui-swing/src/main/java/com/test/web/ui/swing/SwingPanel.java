package com.test.web.ui.swing;

import java.awt.Container;

import com.test.web.ui.common.IUICanvas;
import com.test.web.ui.common.IUIString;
import com.test.web.ui.common.IUIStringListener;
import com.test.web.ui.common.IUIWidgets;
import com.test.web.ui.common.UILayoutData;

abstract class SwingPanel extends SwingContainer implements IUIWidgets {

	public SwingPanel(Container container) {
		super(container);
	}

	@Override
	public final IUIString createString(UILayoutData layoutData, IUIStringListener listener) {
		final SwingString string = new SwingString();
		
		add(layoutData, string);
		
		return string;
	}

	@Override
	public final IUICanvas createCanvas(UILayoutData layoutData) {
		final SwingCanvas canvas = new SwingCanvas();
		
		add(layoutData, canvas);
		
		return canvas;
	}
}

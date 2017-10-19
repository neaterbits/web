package com.test.web.ui.swing;

import java.awt.Container;

import com.test.web.ui.common.IUICanvas;
import com.test.web.ui.common.IUIString;
import com.test.web.ui.common.IUIStringListener;
import com.test.web.ui.common.IUIWidgets;

abstract class SwingPanel extends SwingContainer implements IUIWidgets {

	public SwingPanel(Container container) {
		super(container);
	}

	@Override
	public final IUIString createString(IUIStringListener listener) {
		final SwingString string = new SwingString();
		
		add(string);
		
		return string;
	}

	@Override
	public final IUICanvas createCanvas() {
		final SwingCanvas canvas = new SwingCanvas();
		
		add(canvas);
		
		return canvas;
	}
}

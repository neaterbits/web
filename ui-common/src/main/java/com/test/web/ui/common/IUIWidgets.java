package com.test.web.ui.common;


public interface IUIWidgets {
	
	/**
	 * Create single-line text widget
	 * @param listener listener for events
	 * 
	 * @return reference to String widget
	 */
	IUIString createString(UILayoutData layoutData, IUIStringListener listener);

	/**
	 * Create canvas for rendering, eg for rendering the whole
	 * browser window
	 * 
	 * @return canvas
	 */
	
	IUICanvas createCanvas(UILayoutData layoutData);
}

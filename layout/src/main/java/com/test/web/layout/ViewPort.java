package com.test.web.layout;

public class ViewPort {
	private final int viewPortWidth;
	private final int viewPortHeight;
	
	public ViewPort(int viewPortWidth, int viewPortHeight) {
		super();
		this.viewPortWidth = viewPortWidth;
		this.viewPortHeight = viewPortHeight;
	}

	public int getWidth() {
		return viewPortWidth;
	}

	public int getHeight() {
		return viewPortHeight;
	}
}

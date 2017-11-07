package com.test.web.layout;

/*
 * Helper scratch area into which to compute layout.
 * Everything is given in pixels.
 */

final class Dimensions implements IBounds {
	private int left;
	private int top;
	private int width;
	private int height;
	
	Dimensions() {
		
	}
	
	private Dimensions(Dimensions toCopy) {
		this.left = toCopy.left;
		this.top = toCopy.top;
		this.width = toCopy.width;
		this.height = toCopy.height;
	}
	
	Dimensions makeCopy() {
		return new Dimensions(this);
	}
	
	void init(int left, int top, int width, int height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}

	void setLeft(int left) {
		this.left = left;
	}

	void setTop(int top) {
		this.top = top;
	}

	void setWidth(int width) {
		this.width = width;
	}

	void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getLeft() {
		return left;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "Dimensions [left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + "]";
	}
}

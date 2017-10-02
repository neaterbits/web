package com.test.web.layout;

/*
 * Helper scratch area into which to compute layout.
 * Everything is given in pixels.
 */

final class Dimensions {
	private int left;
	private int top;
	private int width;
	private int height;
	
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

	void addToWidth(int width) {
		this.width += width;
	}

	void addToHeight(int height) {
		this.height += height;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

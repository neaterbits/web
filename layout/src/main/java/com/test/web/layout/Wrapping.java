package com.test.web.layout;

// wrapping of element, used for margin and padding, always computed in pixels
public class Wrapping implements IWrapping {

	private int top;
	private int right;
	private int bottom;
	private int left;
	
	public void init(int top, int right, int bottom, int left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public int getRight() {
		return right;
	}

	@Override
	public int getBottom() {
		return bottom;
	}

	@Override
	public int getLeft() {
		return left;
	}
}

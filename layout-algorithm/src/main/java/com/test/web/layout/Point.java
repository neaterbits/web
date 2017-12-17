package com.test.web.layout;

public class Point implements IPoint {
	private int left;
	private int top;
	
	Point() {
		
	}
	
	private Point(Point toCopy) {
		this.left = toCopy.left;
		this.top = toCopy.top;
	}
	
	Point makeCopy() {
		return new Point(this);
	}
	
	void init(int left, int top) {
		this.left = left;
		this.top = top;
	}

	void setLeft(int left) {
		this.left = left;
	}

	void setTop(int top) {
		this.top = top;
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
	public String toString() {
		return "[" + left + ", " + top + ", " + "]";
	}

}

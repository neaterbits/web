package com.test.web.render.common;

public abstract class BaseFont implements IFont {

	private static final String TESTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	protected abstract int textExtent(String s);
	
	@Override
	public int getAverageWidth() {
		return roundUp(textExtent(TESTCHARS) / (double)TESTCHARS.length());
	}

	protected static int roundUp(double d) {
		int asInteger = (int)d;
		
		if (d - asInteger >= 0.5) {
			++ asInteger;
		}

		return asInteger;
	}

}

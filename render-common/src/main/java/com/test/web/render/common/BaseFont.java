package com.test.web.render.common;

public abstract class BaseFont implements IFont {

	private static final String TESTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	protected abstract int textExtent(String s);
	
	private final String fontFamily;
	private final int fontSize;
	private final int styleFlags;
	
	protected BaseFont(String fontFamily, int fontSize, int styleFlags) {
	
		if (fontFamily == null) {
			throw new IllegalArgumentException("fontFamily == null");
		}

		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
		this.styleFlags = styleFlags;
	}

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

	@Override
	public final String getFontFamily() {
		return fontFamily;
	}

	@Override
	public final int getFontSize() {
		return fontSize;
	}

	@Override
	public final int getStyleFlags() {
		return styleFlags;
	}
}

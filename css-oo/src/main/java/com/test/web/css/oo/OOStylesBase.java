package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSStyle;

abstract class OOStylesBase {
	
	private long styles;

	static {
		if (CSStyle.values().length > 64) {
			throw new IllegalStateException("CSStyle.values().length > 64");
		}
	}
	
	final boolean hasStyle(CSStyle style) {
		return (styles & (1L << style.ordinal())) != 0L;
	}
	
	final void set(CSStyle style) {
		styles |= 1L << style.ordinal();
	}

	static int getColorR(int rgb, CSSColor color) {
		final int ret;
		
		if (color != null) {
			ret = color.getRed();
		}
		else if (rgb != -1) {
			ret = rgb >> 16;
		}
		else {
			throw new IllegalStateException("color not set");
		}
		
		return ret;
	}

	static int getColorG(int rgb, CSSColor color) {
		final int ret;
		
		if (color != null) {
			ret = color.getGreen();
		}
		else if (rgb != -1) {
			ret = (rgb & 0x0000FF00) >> 8;
		}
		else {
			throw new IllegalStateException("color not set");
		}
		
		return ret;
	}

	static int getColorB(int rgb, CSSColor color) {
		final int ret;
		
		if (color != null) {
			ret = color.getBlue();
		}
		else if (rgb != -1) {
			ret = rgb & 0x000000FF;
		}
		else {
			throw new IllegalStateException("color not set");
		}
		
		return ret;
	}

	static int getColorA(int a) {
		return a;
	}
}

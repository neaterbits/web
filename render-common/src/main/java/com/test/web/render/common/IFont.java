package com.test.web.render.common;

/*
 * wraps a font
 */

public interface IFont {

	public static final int FONT_STYLE_ITALIC = 0x01;
	public static final int FONT_STYLE_BOLD = 0x02;

	int getAverageWidth();
	
	int getHeight();
	
}

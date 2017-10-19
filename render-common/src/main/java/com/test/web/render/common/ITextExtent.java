package com.test.web.render.common;

public interface ITextExtent {
	
	public static final int FONT_STYLE_ITALIC = 0x01;
	public static final int FONT_STYLE_BOLD = 0x02;

	IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags);
	
	void closeFont(IFont font);
	
	int getTextExtent(IFont font, String text);
	
}

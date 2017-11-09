package com.test.web.render.common;

public interface ITextExtent {

	IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags);
	
	void closeFont(IFont font);
	
	int getTextExtent(IFont font, String text);
	
}

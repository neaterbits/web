package com.test.web.render.common;

public interface IFontLookup {

	IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags);
	
	void closeFont(IFont font);

}

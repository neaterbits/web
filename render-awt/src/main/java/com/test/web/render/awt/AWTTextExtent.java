package com.test.web.render.awt;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

public class AWTTextExtent implements ITextExtent {

	@Override
	public IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public void closeFont(IFont font) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public int getTextExtent(IFont font, String text) {

		final AWTFont f = (AWTFont)font;
		
		return f.getTextExtent(text);
	}
}

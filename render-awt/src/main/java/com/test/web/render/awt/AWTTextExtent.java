package com.test.web.render.awt;

import java.awt.Font;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

public class AWTTextExtent implements ITextExtent {

	@Override
	public IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags) {

		int awtStyleFlags = 0;
		
		if ((styleFlags & IFont.FONT_STYLE_BOLD) != 0) {
			awtStyleFlags |= Font.BOLD;
		}
		
		if ((styleFlags & IFont.FONT_STYLE_ITALIC) != 0) {
			awtStyleFlags |= Font.ITALIC;
		}

		final Font font = new Font(fontName, awtStyleFlags, fontSize);
		
		return new AWTFont(font);
	}

	@Override
	public void closeFont(IFont font) {
		// TODO: Nothing to do for AWT?
	}

	@Override
	public int getTextExtent(IFont font, String text) {

		final AWTFont f = (AWTFont)font;
		
		return f.textExtent(text);
	}
}

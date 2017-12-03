package com.test.web.render.awt;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.test.web.render.common.BaseFont;
import com.test.web.render.common.IFont;

public class AWTFont extends BaseFont implements IFont {

	private final Font font;

	AWTFont(Font font) {
		
		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}
		
		this.font = font;
	}


	@Override
	public int getHeight() {
		return font.getSize();
	}
	
	
	@Override
	public String toString() {
		return font.toString();
	}

	Font getFont() {
		return font;
	}
	
	@Override
	protected int textExtent(String s) {
		return getWidthOfString(font, s);
	}
	
	static int getWidthOfString(Font font, String s) {
		final FontRenderContext context = new FontRenderContext(null, true, true);
		
		final TextLayout textLayout = new TextLayout(s, font, context);

		// Round double up
		final double width = textLayout.getBounds().getWidth();
	
		return roundUp(width);
	}
}

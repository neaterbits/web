package com.test.web.render.awt;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.test.web.render.common.IFont;

public class AWTFont implements IFont {

	private final Font font;

	AWTFont(Font font) {
		
		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}
		
		this.font = font;
	}

	private static final String TESTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	@Override
	public int getAverageWidth() {
		return roundUp(getWidthOfString(font, TESTCHARS) / (double)TESTCHARS.length());
	}

	@Override
	public int getHeight() {
		return font.getSize();
	}
	
	Font getFont() {
		return font;
	}
	
	private static int roundUp(double d) {
		int asInteger = (int)d;
		
		if (d - asInteger >= 0.5) {
			++ asInteger;
		}

		return asInteger;
	}
	
	int getTextExtent(String s) {
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

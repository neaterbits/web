package com.test.web.layout.algorithm;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

public class MockTextExtent implements ITextExtent {

	@Override
	public IFont getFont(String fontFamily, int fontSize, int styleFlags) {
		return new IFont() {
			
			private final int height = 12;
			private final int averageWidth = 12;

			@Override
			public String getFontFamily() {
				return fontFamily;
			}

			@Override
			public int getFontSize() {
				return fontSize;
			}

			@Override
			public int getStyleFlags() {
				return styleFlags;
			}

			@Override
			public int getHeight() {
				return height;
			}
			
			@Override
			public int getAverageWidth() {
				return averageWidth;
			}

			@Override
			public String toString() {
				return "Font [h=" + getHeight() + ", aw=" + getAverageWidth() +"]";
			}
		};
	}

	@Override
	public void closeFont(IFont font) {
		
	}

	@Override
	public int getTextExtent(IFont font, String text) {
		return font.getAverageWidth() * text.length();
	}
}

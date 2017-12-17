package com.test.web.layout.algorithm;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

public class MockTextExtent implements ITextExtent {

	@Override
	public IFont getFont(String fontFamily, int fontSize, int styleFlags) {
		return new IFont() {
			
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
				return 12;
			}
			
			@Override
			public int getAverageWidth() {
				return 12;
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

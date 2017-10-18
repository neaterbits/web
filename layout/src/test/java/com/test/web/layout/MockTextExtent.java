package com.test.web.layout;

public class MockTextExtent implements ITextExtent {

	@Override
	public IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags) {
		return new IFont() {
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

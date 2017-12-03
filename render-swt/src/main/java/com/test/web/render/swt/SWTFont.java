package com.test.web.render.swt;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;

import com.test.web.render.common.BaseFont;
import com.test.web.render.common.IFont;

public class SWTFont extends BaseFont implements IFont {

	private final Font font;
	private final int height;
	
	private final GC gc;
	
	SWTFont(Font font, int height) {

		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}

		this.font = font;
		
		this.gc = new GC(font.getDevice());
		
		gc.setFont(font);
		
		final int gcFontHeight = gc.getFontMetrics().getHeight();
		
		/*
		if (gcFontHeight != height) {
			throw new IllegalStateException("gcFontHeight != height : " + gcFontHeight + "/" + height);
		}
		*/
		
		this.height = height;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	protected int textExtent(String s) {
		return gc.textExtent(s).x;
	}
	
	void close() {
		this.font.dispose();
		this.gc.dispose();
	}
}

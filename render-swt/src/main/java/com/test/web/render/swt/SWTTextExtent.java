package com.test.web.render.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

public class SWTTextExtent implements ITextExtent {

	private final Device device;
	
	public SWTTextExtent(Device device) {
		if (device == null) {
			throw new IllegalArgumentException("device == null");
		}

		this.device = device;
	}

	@Override
	public IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags) {
		
		int swtStyleFlags = 0;
		
		if ((styleFlags & IFont.FONT_STYLE_BOLD) != 0) {
			swtStyleFlags |= SWT.BOLD;
		}
		
		if ((styleFlags & IFont.FONT_STYLE_ITALIC) != 0) {
			swtStyleFlags |= SWT.ITALIC;
		}

		final Font font = new Font(device, new FontData(fontName, fontSize, swtStyleFlags));
		
		return new SWTFont(fontFamily, fontSize, styleFlags, font, fontSize);
	}

	@Override
	public void closeFont(IFont font) {
		((SWTFont)font).close();
	}

	@Override
	public int getTextExtent(IFont font, String text) {
		return ((SWTFont)font).textExtent(text);
	}
}

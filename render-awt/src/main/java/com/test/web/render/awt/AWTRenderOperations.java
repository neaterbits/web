package com.test.web.render.awt;

import java.awt.Color;
import java.awt.Graphics2D;

import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderOperations;

public class AWTRenderOperations implements IRenderOperations {

	private final Graphics2D gfx;

	public AWTRenderOperations(Graphics2D gfx) {
		this.gfx = gfx;
	}

	@Override
	public void setFgColor(int r, int g, int b) {
		gfx.setColor(new Color(r, g, b));
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		gfx.setBackground(new Color(r, g, b));
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		gfx.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void setFont(IFont font) {
		final AWTFont f = (AWTFont)font;

		gfx.setFont(f.getFont());
	}

	@Override
	public void drawText(int x, int y, String text) {
		gfx.drawChars(text.toCharArray(), 0, text.length(), x, y);
	}
}

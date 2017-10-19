package com.test.web.ui.swing;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.test.web.render.awt.AWTRenderOperations;
import com.test.web.render.awt.AWTTextExtent;
import com.test.web.render.common.IFont;
import com.test.web.ui.common.IUICanvas;

final class SwingCanvas extends JComponent implements IUICanvas {
	
	private static final long serialVersionUID = 1L;

	private final AWTRenderOperations renderOperations;
	private final AWTTextExtent textExtent;
	
	public SwingCanvas() {
		this.renderOperations = new AWTRenderOperations((Graphics2D)this.getGraphics());
		this.textExtent = new AWTTextExtent();
	}
	

	@Override
	public void setFgColor(int r, int g, int b) {
		renderOperations.setFgColor(r, g, b);
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		renderOperations.setBgColor(r, g, b);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		renderOperations.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void setFont(IFont font) {
		renderOperations.setFont(font);
	}

	@Override
	public void drawText(int x, int y, String text) {
		renderOperations.drawText(x, y, text);
	}

	@Override
	public IFont getFont(String fontFamily, String fontName, int fontSize, int styleFlags) {
		return textExtent.getFont(fontFamily, fontName, fontSize, styleFlags);
	}

	@Override
	public void closeFont(IFont font) {
		textExtent.closeFont(font);
	}

	@Override
	public int getTextExtent(IFont font, String text) {
		return textExtent.getTextExtent(font, text);
	}
}

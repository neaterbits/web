package com.test.web.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.test.web.render.awt.AWTRenderOperations;
import com.test.web.render.awt.AWTTextExtent;
import com.test.web.render.common.IFont;
import com.test.web.ui.common.IUICanvas;


final class SwingCanvas extends JComponent implements IUICanvas {
	
	private static final long serialVersionUID = 1L;

	private final BufferedImage image;
	private final AWTRenderOperations renderOperations;
	private final AWTTextExtent textExtent;
	
	public SwingCanvas() {
		
		final int width;
		final int height;
		
		// TODO quick-fix for layout
		width = 1000;
		height = 700;
		
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		final Graphics2D gfx = image.createGraphics();

		// Fill image with white color by default
		gfx.setBackground(new Color(0xFF, 0xFF, 0xFF));
		gfx.fillRect(0,  0, width, height);
		gfx.setColor(new Color(0, 0, 0));
		
		// We have to paint into a backround buffer since we can only paint in paint() method
		this.renderOperations = new AWTRenderOperations(gfx);
		this.textExtent = new AWTTextExtent();
		
		setPreferredSize(new Dimension(width, height));
		setSize(width, height); // TODO quick-fix for layout
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
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void sync() {
		repaint();
	}

	@Override
	public String toString() {
		return "SwingCanvas []";
	}
}


package com.test.web.ui.swt;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.test.web.render.common.IFont;
import com.test.web.render.swt.SWTRenderOperations;
import com.test.web.render.swt.SWTTextExtent;
import com.test.web.ui.common.IUICanvas;


final class SWTCanvas extends Canvas implements IUICanvas {
	
	private final Image image;
	private final SWTRenderOperations renderOperations;
	private final SWTTextExtent textExtent;
	
	public SWTCanvas(Composite parent, int style) {
		super(parent, style);
		
		final int width;
		final int height;
		
		// TODO quick-fix for layout
		width = 1000;
		height = 700;
		
		final Device device = parent.getDisplay();
		
		this.image = new Image(device, width, height);
		
		final GC gfx = new GC(image);
		
		final Color white = new Color(device, 0xFF, 0xFF, 0xFF);
		
		try {
			// Fill image with white color by default
			gfx.setBackground(white);
			gfx.fillRectangle(0,  0, width, height);
		}
		finally {
			white.dispose();
		}
		
		// We have to paint into a backround buffer since we can only paint in paint() method
		this.renderOperations = new SWTRenderOperations(device, gfx);
		this.textExtent = new SWTTextExtent(device);

		/*
		setPreferredSize(new Dimension(width, height));
		setSize(width, height); // TODO quick-fix for layout
		*/

		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}
		});
	}
	
	@Override
	public int getWidth() {
		return super.getSize().x;
	}

	@Override
	public int getHeight() {
		return super.getSize().y;
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
	public void close() {
		renderOperations.close();
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
	public void sync() {
		redraw();
	}

	@Override
	public String toString() {
		return "SwingCanvas []";
	}
}


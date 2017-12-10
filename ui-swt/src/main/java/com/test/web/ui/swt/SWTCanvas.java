package com.test.web.ui.swt;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.test.web.render.common.IFont;
import com.test.web.render.swt.SWTRenderOperations;
import com.test.web.render.swt.SWTTextExtent;
import com.test.web.ui.common.IUICanvas;


final class SWTCanvas extends Canvas implements IUICanvas {
	
	private Image image;
	private SWTRenderOperations renderOperations;
	private final SWTTextExtent textExtent;
	
	public SWTCanvas(Composite parent, int style) {
		super(parent, style);
		
		
		final Device device = parent.getDisplay();
		
		this.textExtent = new SWTTextExtent(device);

		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}
		});
		
		addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				close();
			}
		});
	}
	
	private void prepareImage() {
		final Point size = getSize();

		if (image == null || image.getImageData().width != size.x || image.getImageData().height != size.y) {
			if (this.image != null) {
				this.image.dispose();
			}
			
			this.image = prepareImage(getDisplay(), getSize().x, getSize().y);
		}
	}
	
	private Image prepareImage(Device device, int width, int height) {
		final Image image = new Image(device, width, height);
		
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
		
		return image;
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
		prepareImage();
	
		renderOperations.setFgColor(r, g, b);
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		prepareImage();
		
		renderOperations.setBgColor(r, g, b);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		prepareImage();
		
		renderOperations.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void setFont(IFont font) {
		prepareImage();
		
		renderOperations.setFont(font);
	}

	@Override
	public void drawText(int x, int y, String text) {
		prepareImage();
		
		renderOperations.drawText(x, y, text);
	}
	
	@Override
	public void close() {
		if (image != null) {
			image.dispose();
			image = null;
		}

		if (renderOperations != null) {
			renderOperations.close();
		}
	}

	@Override
	public IFont getFont(String fontFamily, int fontSize, int styleFlags) {
		return textExtent.getFont(fontFamily, fontSize, styleFlags);
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
		return "SWTCanvas []";
	}
}


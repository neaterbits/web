package com.test.web.render.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderOperations;

public class SWTRenderOperations implements IRenderOperations {

	private final Device device;
	private final GC gc;
	
	private Color fgColor;
	private Color bgColor;

	public SWTRenderOperations(Device device, GC gc) {
		
		if (device == null) {
			throw new IllegalArgumentException("device == null");
		}
		
		if (gc == null) {
			throw new IllegalArgumentException("gc == null");
		}
		
		this.device = device;
		this.gc = gc;
		
		this.fgColor = null;
		this.bgColor = null;
	}

	@Override
	public void setFgColor(int r, int g, int b) {
		
		if (this.fgColor != null) {
			this.fgColor.dispose();
		}
		
		this.fgColor = new Color(device, r, g, b);
		
		gc.setForeground(fgColor);
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		if (this.bgColor != null) {
			this.bgColor.dispose();
		}
		
		this.bgColor = new Color(device, r, g, b);
		
		gc.setBackground(bgColor);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		gc.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void setFont(IFont font) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawText(int x, int y, String text) {
		gc.drawText(text, x, y);
	}

	@Override
	public void close() {
		if (this.fgColor != null) {
			this.fgColor.dispose();
			this.fgColor = null;
		}

		if (this.bgColor != null) {
			this.bgColor.dispose();
			this.bgColor = null;
		}
	}
}

package com.test.web.render.common;

public interface IRenderOperations {

	void setFgColor(int r, int g, int b);

	void setBgColor(int r, int g, int b);
	
	void drawLine(int x1, int y1, int x2, int y2);
	
	void setFont(IFont font);
	
	void drawText(int x, int y, String text);
	
	// SWT uses native resources that may have to be cleaned up
	void close();
}

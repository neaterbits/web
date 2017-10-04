package com.test.web.render.queue;

import com.test.web.render.common.IRenderOperations;

// Collectes everything in a buffer of rendering operations that can be replayed
// This allows for passing rendering commands accross process boundaries and to split rendering
// operations across multiple threads
//
// So the queue also works a binary protocol for rendering operations
public class RenderQueue implements IRenderOperations {

	@Override
	public void setFgColor(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBgColor(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFont() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawText(int x, int y, String text) {
		// TODO Auto-generated method stub
		
	}
}

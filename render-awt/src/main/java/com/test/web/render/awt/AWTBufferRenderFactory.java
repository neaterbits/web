package com.test.web.render.awt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.IRenderer;

public class AWTBufferRenderFactory implements IBufferRenderFactory {

	@Override
	public IRenderer createRenderer() {
		
		// TODO should pass in dimensions
		final BufferedImage image = new BufferedImage(100, 100, 0);
		
		final Graphics2D gfx = image.createGraphics();

		return new BufferRenderer(gfx);
	}
	
	private static class BufferRenderer extends AWTRenderOperations implements IRenderer {

		BufferRenderer(Graphics2D gfx) {
			super(gfx);
		}

		@Override
		public void sync() {
			// Nothing to do since renders into buffer at once
		}
	}
}

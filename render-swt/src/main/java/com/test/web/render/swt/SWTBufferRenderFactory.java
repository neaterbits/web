package com.test.web.render.swt;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.IRenderer;


public class SWTBufferRenderFactory implements IBufferRendererFactory {

	private final Device device;
	
	public SWTBufferRenderFactory(Device device) {
		if (device == null) {
			throw new IllegalArgumentException("device == null");
		}

		this.device = device;
	}

	@Override
	public IRenderer createRenderer() {
		
		// TODO should pass in dimensions
		final Image image = new Image(device, 100, 100);
		
		final GC gc = new GC(image);
		
		return new BufferRenderer(device, gc);
	}
	
	private static class BufferRenderer extends SWTRenderOperations implements IRenderer {

		BufferRenderer(Device device, GC gc) {
			super(device, gc);
		}

		@Override
		public void sync() {
			// Nothing to do since renders into buffer at once
		}
	}
}
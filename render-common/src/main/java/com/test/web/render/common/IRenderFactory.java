package com.test.web.render.common;


public interface IRenderFactory {
	
	// Create new renderer, typically for particular layer
	IRenderer createRenderer();
}

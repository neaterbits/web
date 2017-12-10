package com.test.web.render.common;


public interface IDelayedRendererFactory {
	
	// Create new renderer, typically for particular layer
	IDelayedRenderer createRenderer();
}

package com.test.web.loadqueue.common;

import java.io.IOException;

public interface ILoadQueue {

	// Listener is called back on calling thread
	void addStyleSheet(String url, LoadCompletionListener completionListener) throws IOException;

	void addImageLoadingForDimensions(String url, LoadCompletionListener completionListener) throws IOException;

	boolean hasStyleSheet();
	
	boolean hasImageLoadingForDimensions();
}

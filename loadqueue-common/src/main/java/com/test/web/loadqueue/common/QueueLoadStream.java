package com.test.web.loadqueue.common;

import java.io.IOException;

import com.neaterbits.util.io.loadstream.LoadStream;

public abstract class QueueLoadStream extends LoadStream {
	protected abstract void blockOnDependency();

	protected abstract void unblockFromDependency();

	// Schedule on same context as this stream
	protected abstract QueueLoadStream schedule(String url, LoadCompletionListener listener) throws IOException;
	
	private final String url;
	
	public QueueLoadStream(String url) {
		
		if (url == null) {
			throw new IllegalArgumentException("url == null");
		}
		
		this.url = url;
	}

	final String getUrl() {
		return url;
	}
}

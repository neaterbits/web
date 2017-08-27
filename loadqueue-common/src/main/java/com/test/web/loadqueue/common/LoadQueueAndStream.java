package com.test.web.loadqueue.common;

import com.test.web.io.common.LoadStream;

/**
 * Reference to load queue and initial stream for loading page
 * @author nhl
 *
 */

public final class LoadQueueAndStream {

	private final LoadStream stream;
	private final ILoadQueue queue;
	
	LoadQueueAndStream(LoadStream stream, ILoadQueue queue) {
		
		if (stream == null) {
			throw new IllegalArgumentException("stream == null");
		}
		
		if (queue == null) {
			throw new IllegalArgumentException("queue == null");
		}
		
		this.stream = stream;
		this.queue = queue;
	}
	
	public LoadStream getStream() {
		return stream;
	}

	public ILoadQueue getQueue() {
		return queue;
	}
}

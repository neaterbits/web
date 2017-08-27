package com.test.web.loadqueue.common;

import java.io.IOException;
import java.util.function.BiFunction;

public abstract class LoadScheduler {

	// If multi-threaded, we might have to wrap load queue for concurrent access

	protected abstract LoadQueueAndStream scheduleMainPage(
			String url,
			BiFunction<QueueLoadStream, LoadScheduler, ILoadQueue> queueCtor,
			BiFunction<QueueLoadStream, ILoadQueue, LoadQueueAndStream> queueAndStreamCtor,
			LoadCompletionListener completionListener) throws IOException;
	
}

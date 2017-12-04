package com.test.web.loadqueue.common.scheduler;

import java.io.IOException;
import java.util.function.BiFunction;

import com.test.web.loadqueue.common.ILoadQueue;
import com.test.web.loadqueue.common.LoadCompletionListener;
import com.test.web.loadqueue.common.LoadQueueAndStream;
import com.test.web.loadqueue.common.LoadScheduler;
import com.test.web.loadqueue.common.QueueLoadStream;

public class AsyncIOLoadScheduler extends LoadScheduler {

	
	@Override
	protected LoadQueueAndStream scheduleMainPage(
			String url,
			BiFunction<QueueLoadStream, LoadScheduler, ILoadQueue> queueCtor,
			BiFunction<QueueLoadStream, ILoadQueue, LoadQueueAndStream> queueAndStreamCtor,
			LoadCompletionListener completionListener) {

		throw new UnsupportedOperationException("TODO");
	}


	private class AsyncIOLoadStream extends QueueLoadStream {
		
		AsyncIOLoadStream(String url) {
			super(url);
		}

		@Override
		protected void blockOnDependency() {
			throw new UnsupportedOperationException("TODO");
		}

		@Override
		protected void unblockFromDependency() {
			throw new UnsupportedOperationException("TODO");
		}

		@Override
		public long read(char[] buffer, int offset, int length) {
			throw new UnsupportedOperationException("TODO");
		}

		@Override
		protected QueueLoadStream schedule(String url, LoadCompletionListener listener) {
			throw new UnsupportedOperationException("TODO");
		}

		@Override
		public void close() throws IOException {
			throw new UnsupportedOperationException("TODO");
		}
	}
}

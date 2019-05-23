package com.test.web.loadqueue.common.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.BiFunction;

import com.neaterbits.util.io.loadstream.StreamStatus;
import com.test.web.loadqueue.common.ILoadQueue;
import com.test.web.loadqueue.common.LoadCompletionListener;
import com.test.web.loadqueue.common.LoadQueueAndStream;
import com.test.web.loadqueue.common.LoadScheduler;
import com.test.web.loadqueue.common.QueueLoadStream;
import com.test.web.loadqueue.common.IStreamFactory;

public class ThreadedLoadScheduler extends LoadScheduler {

	private final IStreamFactory streamFactory;
	
	public ThreadedLoadScheduler(IStreamFactory streamFactory) {
		
		if (streamFactory == null) {
			throw new IllegalArgumentException("streamFactory == null");
		}

		this.streamFactory = streamFactory;
	}

	@Override
	protected LoadQueueAndStream scheduleMainPage(
			String url,
			BiFunction<QueueLoadStream, LoadScheduler, ILoadQueue> queueCtor,
			BiFunction<QueueLoadStream, ILoadQueue, LoadQueueAndStream> queueAndStreamCtor,
			LoadCompletionListener listener) throws IOException {

		
		final SynchronizedLoadQueue synchronizedLoadQueue = new SynchronizedLoadQueue();
		
		final QueueLoadStream loadStream = new ThreadedQueueLoadStream(url, synchronizedLoadQueue, listener);
		
		final ILoadQueue loadQueue = queueCtor.apply(loadStream, this);
		
		synchronizedLoadQueue.delegate = loadQueue;
		
		return queueAndStreamCtor.apply(loadStream, loadQueue);
	}

	private class ThreadedQueueLoadStream extends QueueLoadStream {

		private final BufferedReader reader;
		
		private final SynchronizedLoadQueue synchronizedLoadQueue;
		private final LoadCompletionListener listener;
		
		private boolean blockedOnDependency;
		
		ThreadedQueueLoadStream(String url, SynchronizedLoadQueue synchronizedLoadQueue, LoadCompletionListener listener) throws IOException {
			super(url);
			
			this.reader = new BufferedReader(new InputStreamReader(streamFactory.getStream(url)));
			
			this.synchronizedLoadQueue = synchronizedLoadQueue;
			this.listener = listener;
			this.blockedOnDependency = false;
		}

		@Override
		protected void blockOnDependency() {

			synchronized (synchronizedLoadQueue) {
				if (this.blockedOnDependency) {
					throw new IllegalStateException("Already blocked");
				}
			}
		}

		@Override
		protected synchronized void unblockFromDependency() {
			synchronized (synchronizedLoadQueue) {
				if (!this.blockedOnDependency) {
					throw new IllegalStateException("Not blocked");
				}
				
				this.blockedOnDependency = true;
			}
		}
		
		@Override
		public long read(char[] buffer, int offset, int length) throws IOException {

			// Just read some data from reader, blocks until data available
			final int bytesRead = reader.read(buffer, offset, length);

			final long ret;
			
			for (;;) {
			
				if (bytesRead < 0) {
					ret = StreamStatus.eof();
					
					// If we're blocked on a dependency and reached EOF, we have to block here until is unblocked again
					for (;;) {
						final boolean blocked;
						synchronized (synchronizedLoadQueue) {
							blocked = blockedOnDependency;
						}
						
						if (blocked) {
							// TODO: do not busy-wait, rather use condition or other synchronization mechanism
							// and call notify when unblocking
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						}
						else {
							break;
						}
					}
					break;
				}
				else if (bytesRead == 0) {
					// Just continue until some data is available
				}
				else {
					// Read data
					final boolean blocked;

					// If blocked, then return blocked-flag so that input just continues reading
					synchronized (synchronizedLoadQueue) {
						blocked = blockedOnDependency;
					}
					
					ret = StreamStatus.of(bytesRead, blocked);
					break;
				}
			}
			
			return ret;
		}

		@Override
		public void close() throws IOException {
			reader.close();
		}

		@Override
		protected QueueLoadStream schedule(String url, LoadCompletionListener listener) throws IOException {
			
			final QueueLoadStream loadStream = new ThreadedQueueLoadStream(url, synchronizedLoadQueue, listener);

			return loadStream;
		}
	}

	private static class SynchronizedLoadQueue implements ILoadQueue {
		

		private ILoadQueue delegate;
		
		
		SynchronizedLoadQueue() {

		}
		
		SynchronizedLoadQueue(ILoadQueue delegate) {
			
			if (delegate == null) {
				throw new IllegalArgumentException("delegate == null");
			}

			this.delegate = delegate;
		}

		@Override
		public synchronized void addStyleSheet(String url, LoadCompletionListener listener) throws IOException {
			delegate.addStyleSheet(url, listener);
		}

		@Override
		public synchronized void addImageLoadingForDimensions(String url, LoadCompletionListener completionListener)
				throws IOException {
			delegate.addImageLoadingForDimensions(url, completionListener);
		}

		@Override
		public synchronized boolean hasStyleSheet() {
			return delegate.hasStyleSheet();
		}

		@Override
		public synchronized boolean hasImageLoadingForDimensions() {
			return delegate.hasImageLoadingForDimensions();
		}
	}
}

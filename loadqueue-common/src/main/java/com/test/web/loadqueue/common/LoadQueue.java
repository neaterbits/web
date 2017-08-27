package com.test.web.loadqueue.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Main queue for loading data
 * 
 * Threads may block on this queue if the depend on something that is not yet loaded,
 * eg we cannot start HTML layout until all CSS has been loaded,
 * this is the case if doing layout computation in the HTML parsing pass  
 * 
 * @author nhl
 *
 */

public class LoadQueue implements ILoadQueue {

	private final QueueLoadStream pageStream;
	private final List<LoadJob> jobsInProgress;
	
	
	private final LoadJob pageJob;
	
	
	/**
	 * Create a new loadqueue, typically one per complete HTML page we're loading
	 * 
	 * @return
	 */
	public static LoadQueueAndStream createLoadQueue(String pageUrl, LoadScheduler loadScheduler) throws IOException {
		final LoadQueueAndStream stream = loadScheduler.scheduleMainPage(pageUrl, LoadQueue::new, LoadQueueAndStream::new, () -> {});

		return stream;
	}
	
	private LoadQueue(QueueLoadStream pageStream, LoadScheduler loadScheduler) {
		this.pageStream = pageStream;
		this.jobsInProgress = new ArrayList<LoadJob>();
		
		this.pageJob = new LoadJob(pageStream);
	}
	
	private LoadJob findJob(String url) {
		return jobsInProgress.stream().filter(j -> j.getStream().getUrl().equals(url)).findAny().orElse(null);
	}

	@Override
	public void addStyleSheet(String url) throws IOException {
		
		if (findJob(url) != null) {
			throw new IllegalArgumentException("Already has ongoing job for " + url);
		}
		
		// We added a style-sheet, this means main job must block
		final Dependency styleSheetDependency = new StyleSheetDependency(url);

		final boolean hadDependencies = pageJob.hasAnyDependencies();
		
		pageJob.addDependency(styleSheetDependency);
		
		if (!hadDependencies) {
			// Initial dependency, so block
			pageStream.blockOnDependency();
		}
		
		// Schedule load and unblock stream when complete
		final QueueLoadStream stream = pageStream.schedule(url, () -> {
			
			pageJob.removeDependency(styleSheetDependency);

			if (!pageJob.hasAnyDependencies()) {
				pageStream.unblockFromDependency();

				// URL should be unique
				jobsInProgress.removeIf(j -> j.getStream().getUrl().equals(url));
			}
		});
		
		final LoadJob job = new LoadJob(stream);

		this.jobsInProgress.add(job);
	}
}

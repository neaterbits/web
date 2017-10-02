package com.test.web.loadqueue.common;

import java.util.HashSet;
import java.util.Set;

/**
 * Something being currently loaded
 * @author nhl
 *
 */

final class LoadJob {

	private final QueueLoadStream stream;
	
	// Tasks that this loadjob depends on in order to continue 
	private Set<Dependency> dependencies;

	LoadJob(QueueLoadStream stream) {
		
		if (stream == null) {
			throw new IllegalArgumentException("stream == null");
		}
		
		this.stream = stream;
	}
	
	QueueLoadStream getStream() {
		return stream;
	}

	void addDependency(Dependency dependency) {
		if (dependency == null) {
			throw new IllegalArgumentException("dependency == null");
		}
		
		if (this.dependencies == null) {
			this.dependencies = new HashSet<>();
		}
		
		dependencies.add(dependency);
	}
	
	void removeDependency(Dependency dependency) {
		
		if (!dependencies.remove(dependency)) {
			throw new IllegalStateException("Unknown dependency");
		}
	}
	
	boolean hasAnyDependencies() {
		return dependencies != null && !dependencies.isEmpty();
	}
	
	boolean hasDependencyOfType(Class<? extends Dependency> type) {
		boolean found = false;
		
		if (dependencies != null) {
			
			for (Dependency dependency : dependencies) {
				if (type.equals(dependency.getClass())) {
					found = true;
					break;
				}
			}
		}

		return found;
	}
}

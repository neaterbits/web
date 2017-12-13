package com.test.web.render.html;

import java.util.Arrays;

import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IRenderOperations;

// Sortable list for ID to offset mapping
// Only used as intermediate object during layout and rendering so we cache these for reuse to reduce number of allocations

public class IDToOffsetList {
	
	// increasing id for each element, for reordering
	private int idGen;

	private IDToOffset [] array;
	private int count;
	
	public IDToOffsetList() {
		this.array = new IDToOffset[100];
		this.idGen = 0;
		this.count = 0;
	}
	
	private int allocateId() {
		return ++ idGen;
	}

	void add(int zIndex, int startOffset, int endOffset) {
		if (count == array.length) {
			this.array = Arrays.copyOf(array, array.length * 2);
		}

		IDToOffset idToOffset = array[count];
		if (idToOffset == null) {
			idToOffset = new IDToOffset();
			array[count] = idToOffset;
		}

		++ count;

		idToOffset.init(allocateId(), zIndex, startOffset, endOffset);
	}
	
	private void reset() {
		this.idGen = 0;
		this.count = 0;
	}
	
	void replaySorted(IDelayedRenderer renderQueue, IRenderOperations ops, IFontLookup fontLookup) {
		
		// Sorting will make us render elements in the order they were specified in the document
		Arrays.sort(array, 0, count);
	
		// replay all element rendering operations TODO using coordinate transform for viewport so that properly clipped against viewport
		for (int i = 0; i < count; ++ i) {

			final IDToOffset idToOffset = array[i];

			renderQueue.replay(ops, idToOffset.startOffset, idToOffset.endOffset, fontLookup);
		}

		// for reuse in another layout and rendering pass
		reset();
	}

	private static class IDToOffset implements Comparable<IDToOffset> {
		private int id; // sortable ID for the element
		private int zIndex;
		private int startOffset; // into rendering queue
		private int endOffset;  // into rendering queue
		
		void init(int id, int zIndex, int startOffset, int endOffset) {
			this.id = id;
			this.zIndex = zIndex;
			this.startOffset = startOffset;
			this.endOffset = endOffset;
		}

		@Override
		public int compareTo(IDToOffset o) {
			return Integer.compare(this.id, o.id);
		}
	}
}

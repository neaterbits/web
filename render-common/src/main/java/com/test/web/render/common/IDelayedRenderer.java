package com.test.web.render.common;


public interface IDelayedRenderer extends IRenderOperations {
	public static final int MARK_NONE = 0xFFFFFFFF;
	
	public static final int OFFSET_NONE = -1;
	

	// mark a reference point where we can later add extra operations in case rendering happens out of order
	// we might not know width until all subelements have been processed
	int mark(); 

	IMarkRenderOperations getOperationsForMark(int markOffset);
	
	// get current offset in queue so can replay rendering ops for eg. a single element
	int getOffset();

	// replay all queued operations from and including startOffset to excluding endOffset (offset of next operation)
	void replay(IRenderOperations ops, int startOffset, int endOffset, IFontLookup fontLookup);

	// replay all queued operations from startOffset
	default void replay(IRenderOperations ops, int startOffset, IFontLookup fontLookup) {
		replay(ops, startOffset, OFFSET_NONE, fontLookup);
	}
	
	// replay all queued operations
	default void replay(IRenderOperations ops, IFontLookup fontLookup) {
		replay(ops, 0, fontLookup);
	}
}

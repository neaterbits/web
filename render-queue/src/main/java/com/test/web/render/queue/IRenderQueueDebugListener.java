package com.test.web.render.queue;

import com.test.web.render.common.IFont;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IRenderOperations;

public interface IRenderQueueDebugListener {
	
	void onSetFgColor(int offset, boolean primary, int r, int g, int b);

	void onSetBgColor(int offset, boolean primary, int r, int g, int b);
	
	void onDrawLine(int offset, boolean primary, int x1, int y1, int x2, int y2);
	
	void onSetFont(int offset, boolean primary, IFont font);
	
	void onDrawText(int offset, boolean primary, int x, int y, String text);

	void onMark(int offset); 

	void onGetOperationsForMark(int markOffset);

	void onEndOperationsForMark(int offset);

	// replay all queued operations from and including startOffset to excluding endOffset (offset of next operation)
	void onReplay(IRenderOperations ops, boolean primary, int startOffset, int endOffset, IFontLookup fontLookup);

	void onReplayOp(int offset, boolean primary, RenderOp op);
}

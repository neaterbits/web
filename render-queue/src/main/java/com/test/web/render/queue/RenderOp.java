package com.test.web.render.queue;

enum RenderOp {

	JADDA(1),
	MARK(1),
	MARK_END(1), // in secondary buffer to mark that these are all the appended ops
	
	// Note, may be serialized to disk so be careful about changing order
	SET_FG_COLOR(1),
	SET_BG_COLOR(1),
	DRAW_LINE(3),
	SET_FONT(1),
	DRAW_TEXT(2); // contains index into StringBuffer
	
	private final int numLongs;

	private RenderOp(int numLongs) {
		this.numLongs = numLongs;
	}
	
	int getOpCode() {
		return ordinal();
	}

	int getNumLongs() {
		return numLongs;
	}

	static {
		if (RenderOp.values().length > 255) {
			throw new IllegalStateException("Outside opcode range");
		}
	}
}

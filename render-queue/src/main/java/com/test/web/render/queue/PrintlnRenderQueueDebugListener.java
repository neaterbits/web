package com.test.web.render.queue;

import java.io.PrintStream;

import com.test.web.render.common.IFont;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IRenderOperations;

public class PrintlnRenderQueueDebugListener implements IRenderQueueDebugListener {

	private final PrintStream out;

	public PrintlnRenderQueueDebugListener(PrintStream out) {
		this.out = out;
	}

	private PrintStream printOp(String name, int offset, boolean primary) {
		out.append("RENDERQUEUE ").append(name).append(" ").append(String.valueOf(offset)).append(primary ? " primary " : " secondary ");
		
		return out;
	}

	@Override
	public void onSetFgColor(int offset, boolean primary, int r, int g, int b) {
		printOp("setFgColor", offset, primary).println("r=" + r + ", g=" + g + ", b=" + b);
	}

	@Override
	public void onSetBgColor(int offset, boolean primary, int r, int g, int b) {
		printOp("setBgColor", offset, primary).println("r=" + r + ", g=" + g + ", b=" + b);
	}

	@Override
	public void onDrawLine(int offset, boolean primary, int x1, int y1, int x2, int y2) {
		printOp("drawLine", offset, primary).println("x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2);
	}

	@Override
	public void onSetFont(int offset, boolean primary, IFont font) {
		printOp("setFont", offset, primary).println("family=" + font.getFontFamily() + ", size=" + font.getFontSize());
	}

	@Override
	public void onDrawText(int offset, boolean primary, int x, int y, String text) {
		printOp("drawText", offset, primary).println("x=" + x + ", y=" + y + ",text=\"" + text + "\"");
	}

	@Override
	public void onMark(int offset) {
		printOp("mark", offset, true).println();
	}

	@Override
	public void onGetOperationsForMark(int markOffset) {
		printOp("getOperationsForMark", markOffset, true).println();
	}

	@Override
	public void onEndOperationsForMark(int offset) {
		printOp("endOperationsForMark", offset, false).println();
	}

	@Override
	public void onReplay(IRenderOperations ops, boolean primary, int startOffset, int endOffset, IFontLookup fontLookup) {
		out.println("RENDERQUEUE replay onto " + ops.getClass().getSimpleName() + " for " + (primary ? " primary " : " secondary ") + "startOffset=" + startOffset + ", endOffset=" + endOffset);
	}

	@Override
	public void onReplayOp(int offset, boolean primary, RenderOp op) {
		printOp("replayOp", offset, primary).println("op=" + op);
	}
}

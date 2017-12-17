package com.test.web.render.html;

import java.io.PrintStream;

import com.test.web.document.html.common.HTMLElement;
import com.test.web.types.IIndent;

public class PrintlnDisplayRendererListener implements IDisplayRendererDebugListener, IIndent {

	private final PrintStream out;

	public PrintlnDisplayRendererListener(PrintStream out) {
		this.out = out;
	}

	@Override
	public void onVisibleElement(int depth, HTMLElement element, int startOffset, int endOffset) {
		indent(depth, out).println("DISPLAY VISIBLE for " + element + " at " + startOffset + ", " + endOffset) ;
	}

	@Override
	public void onVisibleText(int depth, HTMLElement element, String text, int startOffset, int endOffset) {
		indent(depth, out).println("DISPLAY VISIBLE TEXT for " + element + " at " + startOffset + ", " + endOffset + ":\"" + text + "\"") ;
	}

	@Override
	public void onAllVisibleProcessed_StartReplay(int depth) {
		indent(depth, out).println("DISPLAY START REPLAY" ) ;
	}

	@Override
	public void onAllVisibleProcessed_ReplayLayer(int depth, int zIndex) {
		indent(depth, out).println("DISPLAY REPLAY LAYER: " + zIndex) ;
	}

	@Override
	public void onAllVisibleProcessed_ReplayDone(int depth) {
		indent(depth, out).println("DISPLAY REPLAY DONE") ;
	}

	@Override
	public void onAllVisibleProcessed_DisplaySynced(int depth) {
		indent(depth, out).println("DISPLAY SYNC") ;
	}

}

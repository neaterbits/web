package com.test.web.render.html;

import java.io.PrintStream;

import com.test.web.document.html.common.HTMLElement;
import com.test.web.layout.IElementLayout;
import com.test.web.types.IIndent;

public class PrintlnRenderDebugListener implements IRenderDebugListener, IIndent {

	private final PrintStream out;
	
	public PrintlnRenderDebugListener(PrintStream out) {
		this.out = out;
	}

	@Override
	public void onElementStart(int depth, HTMLElement element, IElementLayout layout) {
		indent(depth, out).println("RENDER START " + element);
	}

	@Override
	public void onElementEnd(int depth, HTMLElement element, IElementLayout layout) {
		indent(depth, out).println("RENDER END " + element);
	}

	@Override
	public void onText(int depth, IElementLayout layout, String text) {
		indent(depth, out).println("RENDER TEXT \"" + text + "\" at " + layout);
	}

	@Override
	public void onSetRenderQueueOffsets(int depth, HTMLElement element, int startOffset, int endOffset) {
		indent(depth, out).println("RENDER OFFSETS for " + element + ": " + startOffset + ", " + endOffset);
	}
}

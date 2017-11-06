package com.test.web.layout;

import java.io.PrintStream;
import java.util.Arrays;

import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.document.common.HTMLElement;
import com.test.web.types.IIndent;

public class PrintlnLayoutDebugListener implements ILayoutDebugListener, IIndent {

	private final PrintStream out;
	
	public PrintlnLayoutDebugListener(PrintStream out) {
		this.out = out;
	}

	@Override
	public void onElementStart(int depth, HTMLElement element, String id, String tag, String[] classes) {
		indent(depth, out).println("LAYOUT START " + element + " id=" + id + ", classes=" + Arrays.toString(classes));
	}

	@Override
	public void onElementCSS(int depth, CSSLayoutStyles layoutStyles) {
		indent(depth, out).println("LAYOUT css: " + layoutStyles);
	}

	@Override
	public void onElementStyleAttribute(int depth, CSSLayoutStyles layoutStyles) {
		indent(depth, out).println("LAYOUT styleAttribute: " + layoutStyles);
	}

	@Override
	public void onComputedWidth(int depth, int width) {
		indent(depth, out).println("LAYOUT width: " + width);
	}

	@Override
	public void onComputedHeight(int depth, int height) {
		indent(depth, out).println("LAYOUT width: " + height);
	}

	@Override
	public void onElementEnd(int depth, HTMLElement element) {
		indent(depth, out).println("LAYOUT END " + element);
	}
}

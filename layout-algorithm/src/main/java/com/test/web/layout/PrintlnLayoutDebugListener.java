package com.test.web.layout;

import java.io.PrintStream;
import java.util.Arrays;

import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.document.html.common.HTMLElement;
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
	public void onComputedWidth(int depth, int curAvailableWidth, int subAvailableWidth, int subCSSWidth, boolean hasCSSWidth) {
		indent(depth, out).println(
				"LAYOUT curAvailableWidth: " + curAvailableWidth 
				+ ", subAvailableWidth=" + subAvailableWidth
				+ ", subCSSWidth=" + subCSSWidth);
	}

	@Override
	public void onComputedHeight(int depth, int curAvailableHeight, int subAvailableHeight, int subCSSHeight, boolean hasCSSHeight) {
		indent(depth, out).println("LAYOUT curAvailableHeight: " + curAvailableHeight
				+ ", subAvailableHeight=" + subAvailableHeight
				+ ", subCSSHeight=" + subCSSHeight);
	}
	
	@Override
	public void onResultingLayout(int depth, IElementLayout layout) {
		indent(depth, out).println("LAYOUT result: outer=" + layout.getOuterBounds() + ", inner=" + layout.getInnerBounds());
	}

	@Override
	public void onElementEnd(int depth, HTMLElement element) {
		indent(depth, out).println("LAYOUT END " + element);
	}
}

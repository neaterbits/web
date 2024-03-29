package com.test.web.layout.algorithm;

import java.io.PrintStream;
import java.util.Arrays;

import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.types.IIndent;

public class PrintlnLayoutDebugListener<ELEMENT_TYPE>
		implements ILayoutDebugListener<ELEMENT_TYPE>, IIndent {

	private final PrintStream out;
	
	public PrintlnLayoutDebugListener(PrintStream out) {
		this.out = out;
	}

	@Override
	public void onElementStart(int depth, ELEMENT_TYPE element, String id, String tag, String[] classes) {
		indent(depth, out).println("LAYOUT START " + element + " id=" + id + ", classes=" + Arrays.toString(classes));
	}
	
	@Override
	public void onElementLayoutCase(int depth, ELEMENT_TYPE element, String layoutCase) {
		indent(depth, out).println("LAYOUT CASE " + layoutCase);
	}
	

	@Override
	public void onElementCSS(int depth, ILayoutStylesGetters layoutStyles) {
		indent(depth, out).println("LAYOUT css: " + layoutStyles);
	}

	@Override
	public void onElementStyleAttribute(int depth, ILayoutStylesGetters layoutStyles) {
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
	
	private void onResultingLayout(int depth, IElementLayout layout, String tagStartOrEnd, String layoutCaseName) {
		indent(depth, out).println("LAYOUT result " + tagStartOrEnd + " : outer=" + layout.getOuterBounds() + ", inner=" + layout.getInnerBounds() + ", layoutCase=" + layoutCaseName);
	}

	@Override
	public void onResultingLayoutAtStartTag(int depth, IElementLayout layout, String layoutCaseName) {
		onResultingLayout(depth, layout, "start", layoutCaseName);
	}

	@Override
	public void onResultingLayoutAtEndTag(int depth, IElementLayout layout, String layoutCaseName) {
		onResultingLayout(depth, layout, "end", layoutCaseName);
	}

	@Override
	public void onElementEnd(int depth, ELEMENT_TYPE element, String layoutCaseName) {
		indent(depth, out).println("LAYOUT END " + element+ " layoutCase=" + layoutCaseName );
	}

	@Override
	public void onTextStart(int depth, ELEMENT_TYPE containerElement, String text) {
		indent(depth, out).println("LAYOUT TEXT START " + containerElement + ": " + text);
	}

	@Override
	public void onTextLine(int depth, ELEMENT_TYPE containerElement, String lineText, IElementLayout layout) {
		indent(depth, out).println("LAYOUT TEXT LINE " + containerElement + ": \"" + lineText + "\" : " + layout);
	}

	@Override
	public void onTextEnd(int depth, ELEMENT_TYPE containerElement, String text) {
		indent(depth, out).println("LAYOUT TEXT END " + containerElement + ": " + text);
	}

	@Override
	public void onApplyLineBreakStart(int depth, int lineNo) {
		indent(depth, out).println("LAYOUT  APPLY LINEBREAK START : " + lineNo);
	}

	@Override
	public void onApplyLineBreakEnd(int depth) {
		indent(depth, out).println("LAYOUT  APPLY LINEBREAK END");
	}
}

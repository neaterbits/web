package com.test.web.layout.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.common.FontKey;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ILayoutState;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.enums.Display;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;

// State maintained while doing layout, ie. while recursing the DOM
public final class LayoutState<
			ELEMENT,
			ELEMENT_TYPE,
			DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>> 

		implements ILayoutState, LayoutUpdate {
	
	private final ITextExtent textExtent;
	private final ViewPort viewPort;
	
	// For finding size of text strings when using a particular font for rendering
	private final ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext;

	// Optional listener to eg do rendering in the layout flow
	private final IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener;
	
	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<StackElement> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensions are collected here
	private final PageLayout<ELEMENT> pageLayout;

	private final ILayoutDebugListener<ELEMENT_TYPE> debugListener;
	
	// Index of current block element into stack
	// This so that we know which StackElement to push element tree to.
	// This will be updated when pushing or poping elements from the stack
	
	// TODO perhaps inline elements should not be pushed on this stack?
	// On the other hand we may have block elements within inline elements, or we might have inline-block elements nested within inline elements
	// so it might make sense to keep inline elements on this stack
	private int curBlockElement;
	
	public LayoutState(
			ITextExtent textExtent,
			ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {

		this.textExtent = textExtent;
		this.viewPort = viewPort;
		this.layoutContext = layoutContext;
		this.listener = listener;

		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = pageLayout;

		this.debugListener = debugListener;
		
		// Push intial element on stack, which is not a real element
		push(viewPort.getWidth(), viewPort.getHeight(), "viewport", cssLayout -> cssLayout.setDisplay(Display.BLOCK));
	}
	
	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> getLayoutContext() {
		return layoutContext;
	}
	
	PageLayout<ELEMENT> getPageLayout() {
		return pageLayout;
	}
	
	IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> getListener() {
		return listener;
	}

	PageLayer<ELEMENT> addOrGetLayer(int index, IDelayedRendererFactory renderFactory) {
		return pageLayout.addOrGetLayer(index, renderFactory);
	}

	StackElement getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	StackElement getContainer() {
		if (curDepth < 1) {
			throw new IllegalStateException("curDepth < 1");
		}

		return curDepth == 1 ? null : stack.get(curDepth - 2);
	}
	
	StackElement push(int availableWidth, int availableHeight, String debugName, Consumer<LayoutStyles> computeStyles) {
		
		final StackElement ret;
		
		if (curDepth == stack.size()) {
			ret = new StackElement(curDepth, availableWidth, availableHeight, debugName);
			
			stack.add(ret);
		}
		else {
			// reuse existing
			final StackElement toReuse = stack.get(curDepth);
			
			// StackElement may be referenced from inline element tree within StackElement so must check whether can reuse
			final boolean mayReuse = toReuse.checkAndUpdateWhetherInStackElementTree();
			
			if (mayReuse) {
		    	// make sure is cleared since reused
				toReuse.clear();
				toReuse.init(availableWidth, availableHeight, debugName);
				
				ret = toReuse;
			}
			else {
				// Cannot reuse, allocate new instance and set in stack
				ret = new StackElement(curDepth, availableWidth, availableHeight, debugName);
				stack.set(curDepth, ret);
			}
		}
		
		computeStyles.accept(ret.layoutStyles);
		
		// Knows CSS display so set in resulting layout
		ret.resultingLayout.setDisplay(ret.layoutStyles.getDisplay());
		
		if (isBlock(ret)) {
			// Update current block to point to this one
			this.curBlockElement = curDepth;
		}

		++ curDepth;
	
		return ret;
	}

	void pop() {
		-- curDepth;
		
		if (curDepth < this.curBlockElement) {
			// We must find closest block element upwards so that any inline elements are added to that
			boolean blockElementFound = false;
			
			for (int i = curDepth - 1; i >= 0; -- i) {
				if (isBlock(stack.get(i))) {
					this.curBlockElement = i;
					blockElementFound = true;
					break;
				}
			}

			if  (!blockElementFound) {
				throw new IllegalStateException("No block element found");
			}
		}
	}
	
	private void checkAddInlineElement(StackElement container, StackElement sub) {
		if (container != stack.get(curDepth - 2)) {
			throw new IllegalStateException("container does not match stack: " + container + "/" + stack);
		}
		
		if (sub != getCur()) {
			throw new IllegalStateException("sub is not cur");
		}
	}
	
	@Override
	public final void applyLineBreakAtEndOfBlockElement(StackElement sub) {
		if (!isBlock(sub)) {
			throw new IllegalStateException("Expected sub element to be block element");
		}

		sub.applyLineBreakAtEndOfBlockElement(getDepth(), debugListener);
	}

	// Add inline element to tree within current block-element and check whether width is known
	// This might be either called at start-tag for inline elements where dimensions are known beforehand or it might be upon end tag,
	// eg. for inline-block elements where width and height is not given.
	@Override
	public void addInlineElementAndWrapToNextTextLineIfNecessary(StackElement container, StackElement sub, int widthPx, int heightPx) {

		checkAddInlineElement(container, sub);

		final StackElement curBlockElement = getCurBlockElement();
		
		if (widthPx > curBlockElement.getAvailableWidth()) {
			throw new IllegalStateException("TODO no room for inline element in block element - should do overflow handling");
		}
		
		// Will update remaining-width for current block element, will also add to line-height
		final boolean addedToNewLine = curBlockElement.updateBlockRemainingForNewInlineElement(widthPx, heightPx, getDepth(), debugListener);

		// Add as inline element to container so building a tree of elements
		container.addInlineElementStart(sub, getCurInlineLineNoInBlock());
	}

	@Override
	public void addInlineWrapperElementStart(StackElement container, StackElement sub) {
		checkAddInlineElement(container, sub);

		container.addInlineElementStart(sub, getCurInlineLineNoInBlock());
	}

	@Override
	public void addInlineWrapperElementEnd(StackElement container, StackElement sub) {
		checkAddInlineElement(container, sub);

		container.addInlineElementEnd(sub, getCurInlineLineNoInBlock());
	}

	IElementRenderLayout  addTextChunk(StackElement cur, String text, int widthPx, int heightPx, int zIndex, IDelayedRenderer renderer, boolean lineWrapped) {
		if (cur != getCur()) {
			throw new IllegalStateException("Expected cur to be top of stack");
		}

		final StackElement curBlockElement = getCurBlockElement();
		
		if (widthPx > curBlockElement.getAvailableWidth()) {
			throw new IllegalStateException("TODO no room for text in block element - should do overflow handling");
		}

		// Must wrap block remaining-width
		curBlockElement.updateBlockInlineRemainingWidthForTextElement(widthPx, heightPx, lineWrapped, getDepth(), debugListener);

		return cur.addInlineTextChunk(getCurInlineLineNoInBlock(), text, cur.getResultingLayout().getFont(), widthPx, heightPx, zIndex, renderer);
	}
	
	private int getCurInlineLineNoInBlock() {
		return getCurBlockElement().getCurInlineLineNoInBlock();
	}

	int getInlineRemainingWidth() {
		return getCurBlockElement().getRemainingWidth();
	}

	private StackElement getCurBlockElement() {
		return stack.get(curBlockElement);
	}
	

	private static boolean isBlock(StackElement element) {
		return element.layoutStyles.getDisplay().isBlock();
	}

	private static boolean isInline(StackElement element) {
		return element.layoutStyles.getDisplay().isInline();
	}
	
	IFont getOrOpenFont(FontSpec spec, short style) {
		final FontKey fontKey = new FontKey(spec, style);
		
		IFont font = fonts.get(fontKey);
		
		if (font == null) {
			font = textExtent.getFont(spec.getFamily(), spec.getSize(), style);

			if (font == null) {
				throw new IllegalStateException("Failed to open font " + spec + " with style " + style);
			}
		}

		return font;
	}
	
	void close() {
		// Close all fonts
		for (IFont font : fonts.values()) {
			textExtent.closeFont(font);
		}

		this.fonts.clear();
	}
	
	int getDepth() {
		return curDepth;
	}
}

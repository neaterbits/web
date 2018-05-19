package com.test.web.layout.blockinline;

import java.util.function.Consumer;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.algorithm.LayoutState;
import com.test.web.layout.algorithm.PageLayout;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.ViewPort;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.ITextExtent;

public class BlockInlineLayoutState<
			ELEMENT,
			ELEMENT_TYPE,
			DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>> 
	extends LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, StackElement> 
	implements BlockInlineLayoutUpdate {

	// Index of current block element into stack
	// This so that we know which StackElement to push element tree to.
	// This will be updated when pushing or poping elements from the stack
	
	// TODO perhaps inline elements should not be pushed on this stack?
	// On the other hand we may have block elements within inline elements, or we might have inline-block elements nested within inline elements
	// so it might make sense to keep inline elements on this stack
	int curBlockElement;

	public BlockInlineLayoutState(ITextExtent textExtent, ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext, PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {
		super(textExtent, viewPort, layoutContext, pageLayout, listener, debugListener);
	}

	@Override
	protected StackElement createStackElement(int stackIdx) {
		return new StackElement(stackIdx);
	}

	
	@Override
	protected StackElement push(String debugName, Consumer<LayoutStyles> computeStyles, Consumer<StackElement> init) {
		final StackElement stackElement =  super.push(debugName, computeStyles, init);

		if (isBlock(stackElement)) {
			// Update current block to point to this one
			this.curBlockElement = stackElement.getStackIndex();
		}

		return stackElement;
	}

	@Override
	protected void pop() {
		super.pop();
		
		final int curDepth = getDepth();

		if (curDepth < this.curBlockElement) {
			// We must find closest block element upwards so that any inline elements are added to that
			boolean blockElementFound = false;
			
			for (int i = curDepth - 1; i >= 0; -- i) {
				if (isBlock(getStackElementAt(i))) {
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
		if (container != getContainer()) {
			throw new IllegalStateException("container does not match stack: " + container + "/" + stackDebugString());
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

	@Override
	public IElementRenderLayout  addTextChunk(StackElement cur, String text, int widthPx, int heightPx, int zIndex, IDelayedRenderer renderer, boolean lineWrapped) {
		if (cur != getCur()) {
			throw new IllegalStateException("Expected cur to be top of stack");
		}

		final StackElement curBlockElement = getCurBlockElement();
		
		if (widthPx > curBlockElement.getAvailableWidth()) {
			throw new IllegalStateException("TODO no room for text in block element - should do overflow handling");
		}

		// Must wrap block remaining-width
		curBlockElement.updateBlockInlineRemainingWidthForTextElement(widthPx, heightPx, lineWrapped, getDepth(), debugListener);

		return cur.addInlineTextChunk(getCurInlineLineNoInBlock(), text, cur.getResultingLayout().getFont(), widthPx, heightPx, zIndex, renderer, () -> createElementLayout());
	}
	
	private int getCurInlineLineNoInBlock() {
		return getCurBlockElement().getCurInlineLineNoInBlock();
	}

	@Override
	public final int getInlineRemainingWidth() {
		return getCurBlockElement().getRemainingWidth();
	}

	private StackElement getCurBlockElement() {
		return getStackElementAt(curBlockElement);
	}

	private static boolean isBlock(StackElement element) {
		return element.getDisplay().isBlock();
	}

	private static boolean isInline(StackElement element) {
		return element.getDisplay().isInline();
	}
}

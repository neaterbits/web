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
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

public class BlockInlineLayoutState<
			ELEMENT,
			ELEMENT_TYPE,
			DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>> 
	extends LayoutState<ELEMENT, ELEMENT_TYPE, DOCUMENT, StackElement<ELEMENT>> 
	implements BlockInlineLayoutUpdate<ELEMENT> {

	// Index of current block element into stack
	// This so that we know which StackElement to push element tree to.
	// This will be updated when pushing or poping elements from the stack
	
	// TODO perhaps inline elements should not be pushed on this stack?
	// On the other hand we may have block elements within inline elements, or we might have inline-block elements nested within inline elements
	// so it might make sense to keep inline elements on this stack
	int curBlockElement;

	public BlockInlineLayoutState(ITextExtent textExtent, ViewPort viewPort, IDelayedRendererFactory rendererFactory,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext, PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener) {
		super(textExtent, viewPort, rendererFactory, layoutContext, pageLayout, listener, debugListener);
	}

	@Override
	protected StackElement<ELEMENT> createStackElement(int stackIdx) {
		return new StackElement<>(stackIdx);
	}
	
	@Override
	protected StackElement<ELEMENT> push(String debugName, Consumer<LayoutStyles> computeStyles, Consumer<StackElement<ELEMENT>> init) {
		final StackElement<ELEMENT> stackElement =  super.push(debugName, computeStyles, init);

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

	private void checkAddInlineElement(StackElement<ELEMENT> container, StackElement<ELEMENT> sub) {
		if (container != getContainer()) {
			throw new IllegalStateException("container does not match stack: " + container + "/" + stackDebugString());
		}
		
		if (sub != getCur()) {
			throw new IllegalStateException("sub is not cur");
		}
	}
	
	@Override
	public final void applyLineBreakAtEndOfBlockElement(StackElement<ELEMENT> sub) {
		if (!isBlock(sub)) {
			throw new IllegalStateException("Expected sub element to be block element");
		}

		sub.applyLineBreakAtEndOfBlockElement(getDepth(), debugListener, this::addToPageLayer);
	}
	
	private void addToPageLayer(ELEMENT element, IElementRenderLayout elementLayout) {
		super.addToLayerForComputedLayout(element, elementLayout);
	}

	// Add inline element to tree within current block-element and check whether width is known
	// This might be either called at start-tag for inline elements where dimensions are known beforehand or it might be upon end tag,
	// eg. for inline-block elements where width and height is not given.
	@Override
	public void addInlineElementAndWrapToNextTextLineIfNecessary(StackElement<ELEMENT> container, StackElement<ELEMENT> sub, int widthPx, int heightPx) {

		checkAddInlineElement(container, sub);

		final StackElement<ELEMENT> curBlockElement = getCurBlockElement();
		
		if (widthPx > curBlockElement.getAvailableWidth()) {
			throw new IllegalStateException("TODO no room for inline element in block element - should do overflow handling");
		}
		
		// Will update remaining-width for current block element, will also add to line-height
		final boolean addedToNewLine = curBlockElement.updateBlockRemainingForNewInlineElement(widthPx, heightPx, getDepth(), debugListener, this::addToPageLayer);

		// Add as inline element to container so building a tree of elements
		container.addInlineWrapperElementStart(sub, getCurInlineLineNoInBlock());
	}

	@Override
	public void addInlineWrapperElementStart(StackElement<ELEMENT> container, StackElement<ELEMENT> sub) {
		checkAddInlineElement(container, sub);

		container.addInlineWrapperElementStart(sub, getCurInlineLineNoInBlock());
	}

	@Override
	public void addInlineWrapperElementEnd(StackElement<ELEMENT> container, StackElement<ELEMENT> sub) {
		checkAddInlineElement(container, sub);

		container.addInlineElementEnd(sub, getCurInlineLineNoInBlock());
	}

	@Override
	public IElementRenderLayout  addTextChunk(StackElement<ELEMENT> cur, String text, int widthPx, int heightPx, int zIndex, IDelayedRenderer renderer, boolean lineWrapped) {
		if (cur != getCur()) {
			throw new IllegalStateException("Expected cur to be top of stack");
		}

		final StackElement<ELEMENT> curBlockElement = getCurBlockElement();
		
		if (widthPx > curBlockElement.getAvailableWidth()) {
			throw new IllegalStateException("TODO no room for text in block element - should do overflow handling");
		}

		// Must wrap block remaining-width
		curBlockElement.updateBlockInlineRemainingWidthForTextElement(widthPx, heightPx, lineWrapped, getDepth(), debugListener, this::addToPageLayer);

		return cur.addInlineTextChunk(getCurInlineLineNoInBlock(), text, cur.getResultingLayout().getFont(), widthPx, heightPx, zIndex, renderer, () -> createElementLayout());
	}
	
	private int getCurInlineLineNoInBlock() {
		return getCurBlockElement().getCurInlineLineNoInBlock();
	}

	@Override
	public final int getInlineRemainingWidth() {
		return getCurBlockElement().getRemainingWidth();
	}

	private StackElement<ELEMENT> getCurBlockElement() {
		return getStackElementAt(curBlockElement);
	}

	private static <E>boolean isBlock(StackElement<E> element) {
		return element.getDisplay().isBlock();
	}

	private static <E> boolean isInline(StackElement<E> element) {
		return element.getDisplay().isInline();
	}
}

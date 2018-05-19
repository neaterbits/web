package com.test.web.layout.algorithm;

import java.util.function.Consumer;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.enums.Display;

public abstract class LayoutStackElement {
	// Since we can reuse stack elements on a stack, we keep track of allocation state.
	// We need to keep a nested tree of StackElement instances when processing inline elements since we cannot
	// compute layout until we have reached line wrap or start of a block element or end of current block element.
	// To avoid re-allocation of this and all subtypes (most of which will have all values re-initialiezed anyway), we re-use elements on the stack
	// but have to make sure we do not reuse if there are inline elements kept in inline-element tree
	// eg if we have <span>som text<span>sub span</span><span>another sub span</span></span>
	// the stack in LayoutState would try to reuse the StackElement for <span>sub span</span> unless we track that this
	// is referenced in the tree from the outer <span> StackElement. So for <span>another sub span</span>
	// one will have to allocate a new StackElement() (which is simpler and more efficient than implementing copy-on-write or similar)
	enum AllocationState {
		ALLOCATED,   						// Newly allocated with new operator
		CLEARED, 							// Reused, called by clear()
		IN_LAYOUT_STATE_STACK, // In layout state stack only
		IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE, // Still on layout stack but also in stack element tree of containing element (eg while processing <span>sub span</span> above)
		IN_STACK_ELEMENT_TREE, // Referenced from stack element tree so cannot be caleld clear() or init() on.
	}

	private final int stackIdx;

	// For debug printouts
	private String debugName;

	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	private final LayoutStyles layoutStyles;

	// The resulting layout after computation of width and height, this is what rendering sees
	// TODO not have this as public but pass in where needed
	final ElementLayout resultingElementLayout;
	public final ElementLayoutSettersGetters resultingLayout;

	// The layout case for this element, eg an inline-block element within a block element
	private BaseLayoutCase<?, ?> layoutCase;


	private AllocationState allocationState;

	protected LayoutStackElement(int stackIdx) {
		this.stackIdx = stackIdx;
		this.resultingLayout = this.resultingElementLayout = new ElementLayout();

		this.layoutStyles = new LayoutStyles();

		setAllocationState(AllocationState.ALLOCATED);
	}
	
	public final int getStackIndex() {
		return stackIdx;
	}
	
	protected void clear() {
		
		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Can only clear elements that are in use in layout stack: "  + allocationState);
		}
		
		setAllocationState(AllocationState.CLEARED);

		layoutStyles.clear();

		resultingElementLayout.clear();
	}
	
	
	
	public final ILayoutStylesGetters getLayoutStyles() {
		return layoutStyles;
	}

	public final boolean hasUserSpecifiedWidth() {
		return layoutStyles.hasWidth();
	}

	public final boolean hasUserSpecifiedHeight() {
		return layoutStyles.hasHeight();
	}

	public final Display getDisplay() {
		return layoutStyles.getDisplay();
	}
	
	public final void addedInSeparateTree() {
		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK) {
			throw new IllegalStateException("Expected element to be on layout stack: " + allocationState);
		}

		// Now in both layout state and on stack element tree
		setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE);
	}
	
	public final void removedFromSeparateTree() {
		
		if (allocationState != AllocationState.IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE) {
			throw new IllegalStateException("Expected element to be on layout stack: " + allocationState);
		}

		setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
	}
	
	
	final void initCSSLayoutStyles(Consumer<LayoutStyles> computeStyles) {
		// Just calls method to compute onto layoutStyles object
		computeStyles.accept(layoutStyles);
	}


	public final boolean isViewPort() {
		return this.stackIdx == 0;
	}

    protected void init(LayoutStackElement container, String debugName) {
    	init(debugName);
    }

    protected void init(ViewPort viewPort, String debugName) {
    	init(debugName);
    }

    private void init(String debugName) {
       	this.debugName = debugName;
    	
    		if (allocationState != AllocationState.ALLOCATED && allocationState != AllocationState.CLEARED) {
    			throw new IllegalStateException("Can only init elements that are either newly allocated or cleared: " + allocationState);
    		}

    		// Now in use in layout stack
    		setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
    }

    private void setAllocationState(AllocationState state) {
		if (state == null) {
			throw new IllegalArgumentException("state == null");
		}
		
		if (this.allocationState == state) {
			throw new IllegalArgumentException("Setting to same state: " + state);
		}
		
		this.allocationState = state;
	}
	
	final boolean checkAndUpdateWhetherInStackElementTree() {
		final boolean mayReuse;
		
		switch (allocationState) {
		case IN_LAYOUT_STATE_STACK:
			mayReuse = true;
			break;
			
		case IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE:
			mayReuse = false;
			// From now on only in stack element tree since LayoutState will have to allocate a new StackElement instance
			setAllocationState(AllocationState.IN_STACK_ELEMENT_TREE);
			break;
			
		default:
			throw new IllegalStateException("Cannot try reuse stack elements in state " + allocationState);
		}
		
		return mayReuse;
	}
	
	// Call this to release from stack element tree, eg. after we have computed layout for the element and added it to page layer
	final void releaseFromStackElementTree() {
		switch (allocationState) {
		case IN_LAYOUT_STATE_STACK_AND_STACK_ELEMENT_TREE:
			// Still can be reused in layout state since it is still on layout state stack
			setAllocationState(AllocationState.IN_LAYOUT_STATE_STACK);
			break;
			
		case IN_STACK_ELEMENT_TREE:
			// Was removed from stack so no use updating allocation state
			break;
			
		default:
			throw new IllegalStateException("Unexpected allocation state " + allocationState);
		}
	}

	public final ElementLayoutSettersGetters getResultingLayout() {
		return resultingLayout;
	}
	
	final BaseLayoutCase<?, ?> getLayoutCase() {
		return layoutCase;
	}

	final String getLayoutCaseName() {
		return layoutCase.getName();
	}

	final void setLayoutCase(BaseLayoutCase<?, ?> layoutCase) {
		this.layoutCase = layoutCase;
	}
	
	final String getDebugName() {
		return debugName;
	}
}

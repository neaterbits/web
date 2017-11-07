package com.test.web.layout;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.CSSDimensions;
import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.Tokenizer;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;


/*
/*
 * Main layout algorithm for document, implemented as a listener on start and end element events,
 * computes width for CSS speciefied elements on start element and tries to find dimensions
 * of other elements when processing end element, by adding up sizes.
 * 
 */

public class LayoutAlgorithm<ELEMENT, TOKENIZER extends Tokenizer>
	implements HTMLElementListener<ELEMENT, LayoutState<ELEMENT>> {

	private final ITextExtent textExtent;
	
	// For creating renderers, rendering occurs in the same pass (but renderer implenentation might just queue operations for later)
	private final IRenderFactory renderFactory;
	private final ILayoutDebugListener debugListener;
	
	private final FontSettings fontSettings;
	
	private final TextUtil textUtil;
	
	public LayoutAlgorithm(
			ITextExtent textExtent,
			IRenderFactory renderFactory,
			FontSettings fontSettings,
			ILayoutDebugListener debugListener) {
		this.textExtent = textExtent;
		this.textUtil = new TextUtil(textExtent);
		this.fontSettings = fontSettings;
		this.renderFactory = renderFactory;
		this.debugListener = debugListener;
	}

	public PageLayout<ELEMENT> layout(Document<ELEMENT> document, ViewPort viewPort, CSSContext<ELEMENT> cssContext, HTMLElementListener<ELEMENT, IElementRenderLayout> listener) {
		
		final LayoutState<ELEMENT> state = new LayoutState<>(textExtent, viewPort, cssContext, listener);
		
		document.iterate(this, state);
		
		return state.getPageLayout();
	}
	
    @Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {
    	
    	final HTMLElement elementType = document.getType(element);

    	if (!elementType.isLayoutElement()) {
    		return;
    	}
    	
    	if (debugListener != null) {
    		debugListener.onElementStart(
    				state.getDepth(),
    				elementType,
    				document.getId(element),
    				document.getTag(element),
    				document.getClasses(element));
    	}

    	final StackElement cur = state.getCur();

    	// Push new sub-element onto stack with remaining width and height from current element
    	final StackElement sub = state.push(cur.getRemainingWidth(), cur.getRemainingHeight());
    	
    	// Collect all layout styles from CSS
    	state.getCSSContext().getCSSLayoutStyles(
    			elementType.getDefaultDisplay(),
    			fontSettings.getFontForElement(elementType),
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				sub.layoutStyles);
    	
    	if (debugListener != null) {
    		debugListener.onElementCSS(state.getDepth(), sub.layoutStyles);
    	}

    	// Also apply style attribute if defined
		final ICSSDocumentStyles<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			// Get CSS document from style-tag of element
			state.getCSSContext().applyLayoutStyles(styleAttribute, element, sub.layoutStyles);

	    	if (debugListener != null) {
	    		debugListener.onElementStyleAttribute(state.getDepth(), sub.layoutStyles);
	    	}
		}
		
		// Adjust sub available width/height if is set
		
		final int width;
		final int cssWidth;

		// Cache values since may be updated further down
		final int curRemainingWidth = cur.getRemainingWidth();
		final int curRemainingHeight = cur.getRemainingHeight();
		
		if (sub.layoutStyles.hasWidth()) {
			// has width, compute and update
			width = cssWidth = computeWidthPx(sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), cur.getRemainingWidth());

	    	if (width <= 0) {
				throw new IllegalStateException("Computed width 0 from "  + sub.layoutStyles.getWidth() + " of unit " + sub.layoutStyles.getWidthUnit());
			}
	    	
			// Got width from CSS above
			sub.resultingLayout.setHasCSSWidth(true);
			
			// remaining - width may be negative below
			final int remaining = Math.max(0, cur.getRemainingWidth() - width);
			
			cur.setRemainingWidth(remaining);
		}
		else {
			// No CSS width so must set width to what is available in container
			if (cur.getRemainingWidth() < 0) {
				throw new IllegalStateException("Should always know available width");
			}
			
			// No CSS width so use all remaining width
			width = cur.getRemainingWidth();
			cur.setRemainingWidth(0);
			
			cssWidth = -1;
		}

		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
		
    	if (debugListener != null) {
    		debugListener.onComputedWidth(state.getDepth(), cur.getAvailableWidth(), sub.getAvailableWidth(), cssWidth, sub.layoutStyles.hasWidth());
    	}
		
		int height = -1;
		
		if (sub.layoutStyles.hasHeight()) {
			// has width, compute and update
			height = computeHeightPx(sub.layoutStyles.getHeight(), sub.layoutStyles.getHeightUnit(), cur.getAvailableHeight());

	    	// height is -1 if cur.getAvailableHeight() == -1 (scrolled webage with no specified height)
	    	if (height != -1) {
	    		sub.resultingLayout.setHasCSSHeight(true);
	    	}
		}
		
    	if (debugListener != null) {
    		debugListener.onComputedHeight(state.getDepth(), cur.getAvailableHeight(), sub.getAvailableHeight(), height, sub.layoutStyles.hasHeight());
    	}

		
		if (height == -1) {
			// No CSS height, height is computed from what is available in container, or from size of element, knowing width
			
			if (cur.getRemainingHeight() > 0) {
				// Set to rest of available height
				height = cur.getRemainingHeight();

				// Not set remaining height to 0 here, that only happens when we switch to new block
			}
			else if (cur.getRemainingHeight() == 0) {
				height = 0;
			}
			else {
				// We must compute element height, but element is nested so we do not know yet, we must figure out
				// after having recursed
				sub.delayedLayout |= StackElement.UNKNOWN_HEIGHT;
			}
		}

		// height might be -1
		sub.resultingLayout.getOuter().setHeight(height);
		sub.setAvailableHeight(height);
		
		// Compute inner-dimensions
		computeDimensionsFromOuter(
				sub.layoutStyles.getDisplay(),
				curRemainingWidth,
				width,
				sub.layoutStyles.hasWidth(),
				curRemainingHeight,
				height,
				sub.layoutStyles.hasHeight(),
				sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		if (debugListener != null) {
			debugListener.onResultingLayout(state.getDepth(), sub.resultingLayout);
		}

		// Set resulting font
		final FontSpec spec = sub.layoutStyles.getFont();

		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		
		sub.resultingLayout.setFont(font);

		// listener, eg renderer
		if (state.getListener() != null) {
			state.getListener().onElementStart(document, element, null);
		}
	}
    
    private static void computeDimensionsFromOuter(
    		CSSDisplay display,
    		int remainingWidth, int widthFromCSS, boolean hasWidthFromCSS,
    		int remainingHeight, int heightFromCSS, boolean hasHeightFromCSS,
    		CSSDimensions margins, CSSDimensions padding,
    		ElementLayout resultingLayout) {
    	
    	
    	final int topPadding 		= getPaddingSize(padding.getTop(), 		padding.getTopUnit(), 		padding.getTopType(),		remainingHeight);
    	final int rightPadding 	= getPaddingSize(padding.getRight(), 		padding.getRightUnit(), 		padding.getRightType(),		remainingWidth);
    	final int bottomPadding 	= getPaddingSize(padding.getBottom(), 	padding.getBottomUnit(), 	padding.getBottomType(),	remainingHeight);
    	final int leftPadding 		= getPaddingSize(padding.getLeft(),			padding.getLeftUnit(), 		padding.getLeftType(),		remainingWidth);

    	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);
    	
    	// margins can be auto so in that case must know how much space is required by element so that can supply the rest as margins
    	
    	final int innerWidth;
    	final int innerHeight;
    	
    	if (hasWidthFromCSS) {
    		// width is specified in CSS, we use that
    		innerWidth = widthFromCSS;
    	}
    	else {
    		// no width in CSS so we'll use what space is available. For height this is probably the value -1, which means not known
    		innerWidth = remainingWidth;
    	}
	
    	if (hasHeightFromCSS) {
    	   	innerHeight = heightFromCSS;
    	}
    	else {
    		innerHeight = remainingHeight;
    	}
 	    	
    	int innerLeft = leftPadding;
    	int innerTop = topPadding;
    	
    	final int leftMargin;
    	final int rightMargin;
    	
    	//I If margin is auto, we have to compute these.
    	// - if dusplay:block abd width was specified in CSS and rhere is room for margin, it will be set to remaining
    	// - otherwise set to 0
    	if (margins.getLeftType() == CSSJustify.AUTO || margins.getRightType() == CSSJustify.AUTO) {
    		
    		final int paddingWidth = leftPadding + rightPadding;
    		
    		if (display == CSSDisplay.BLOCK && hasWidthFromCSS) {
    			
    			// We should compute margin from leftover width, but only if had CSS width that was < specified
    			if (margins.getLeftType() == CSSJustify.AUTO && margins.getRightType() == CSSJustify.AUTO) {
    				// both left and right margins are auto so split margin size

    				final int remaining = remainingWidth - (widthFromCSS + paddingWidth);
    				
    				final int remainingHalf = remaining / 2;
    				
    				leftMargin = remainingHalf + (remaining % 2);
    				rightMargin = remainingHalf;
    			}
    			else if (margins.getLeftType() == CSSJustify.AUTO) {
    				rightMargin = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
    				// left is auto
    				leftMargin = Math.max(0, remainingWidth - rightMargin - paddingWidth - widthFromCSS);
    			}
    			else if (margins.getRightType() == CSSJustify.AUTO) {
    				leftMargin = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
    				// right is auto
    				rightMargin = Math.max(0, remainingWidth - leftMargin - paddingWidth - widthFromCSS);
    			}
    			else {
    				throw new IllegalStateException("Either left or right should be auto");
    			}
    		}
    		else {
    			// left and right margins are 0 since can only use auto for display:block it seems
    			leftMargin 	 = getSizeWithAutoAsZero(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
    			rightMargin = getSizeWithAutoAsZero(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
    		}
    	}
    	else {
    		// margins can be computed directly since not auto
    		leftMargin  = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
      		rightMargin  = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
     	}
    	
  		innerLeft += leftMargin;
 	
		// margins of auto is set to 0
		final int topMargin  = getNonAutoSize(margins.getTop(), margins.getTopUnit(), margins.getTopType(), remainingWidth);
  		final int bottomMargin  = getNonAutoSize(margins.getBottom(), margins.getBottomUnit(), margins.getBottomType(), remainingWidth);
  		
  		innerTop += topMargin;
    	
    	resultingLayout.getInner().init(innerLeft, innerTop, innerWidth, innerHeight);
    
    	resultingLayout.getMarginWrapping().init(topMargin, rightMargin, bottomMargin, leftMargin);
       	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);
   
     	// Outer width and outer height
    	resultingLayout.getOuter().setWidth(innerWidth + leftPadding + rightPadding + leftMargin + rightMargin);
    	
    	if (innerHeight != -1) {
    		resultingLayout.getOuter().setHeight(innerHeight + topPadding + bottomPadding + topMargin + bottomMargin);
    	}
    }
    
    
    
    private static int getPaddingSize(int size, CSSUnit unit, CSSJustify type, int curSize) {
    	return getNonAutoSize(size, unit, type, curSize);
    }
    
    private static int getSizeWithAutoAsZero(int size, CSSUnit unit, CSSJustify type, int curSize) {
    	return type == CSSJustify.AUTO ? 0 : getNonAutoSize(size, unit, type, curSize);
    }
    	    
    
    private static int getNonAutoSize(int size, CSSUnit unit, CSSJustify type, int curSize) {
        
    	final int ret;
    	
    	switch (type) {
    	case NONE:
    		ret = 0;
    		break;
    		
    	case SIZE:
    		ret = computeSizePx(size, unit, curSize);
    		break;
    		
    	default:
    		throw new UnsupportedOperationException("Unknown type " + type);
    	}
    	
    	return ret;
    }

    @Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {
	
    	final HTMLElement elementType = document.getType(element);
    	
    	if (!elementType.isLayoutElement()) {
    		return;
    	}
    	
    	if (debugListener != null) {
    		debugListener.onElementEnd(state.getDepth(), elementType);
    	}
    	
    	// End of element where wer're at
		final StackElement cur = state.getCur();
		
		state.pop();
		
		final StackElement parent  = state.getCur();

		
		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		computeLayout(elementType, cur.layoutStyles, parent, cur, document, element);
		
		// Got layout, add to layer
		final short zIndex = cur.layoutStyles.getZIndex();
		
		final PageLayer<ELEMENT>layer = state.addOrGetLayer(zIndex, renderFactory);

		// make copy since resulting layout is reused
		layer.add(element, cur.resultingLayout.makeCopy());

		// Has computed sub element size by now so can add
		if (!parent.resultingLayout.hasCSSWidth()) {
			// no width from CSS so must add this element to size of current element
			//parent.resultingLayout.getOuter().addToWidth(cur.resultingLayout.getOuter().getWidth());
		}
		
		// Add to height of current element if is taller than max for current element
		final int height = cur.resultingLayout.getOuter().getHeight();

		if (height > parent.getMaxBlockElementHeight()) {
			parent.setMaxBlockElementHeight(height);
		}
	
		/*
		if (!parent.resultingLayout.hasCSSHeight()) {
			// no width from CSS so must add this element to size of current element
			parent.resultingLayout.getDimensions().addToHeight(cur.resultingLayout.getDimensions().getHeight());
		}
		*/
		
		if (state.getListener() != null) {
			state.getListener().onElementEnd(document, element, cur.resultingLayout);
		}
	}


    @Override
	public void onText(Document<ELEMENT> document, String text, LayoutState<ELEMENT> state) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

		final StackElement cur = state.getCur();

		final IFont font = cur.resultingLayout.getFont();
		
		final int width = textUtil.getTextLengthOrAvailableWidth(text, cur.getAvailableWidth(), font);
		
		int height = 0;
		
		if (cur.getAvailableWidth() != -1) {
			// We have to compute number of lines for this text
			// TODO: floats
			
			String s = text;
			
			for (;;) {
			
				// For each line, find with of text
				int numChars = textUtil.findNumberOfChars(s, cur.getAvailableWidth(), font);
				
				if (numChars == 0 && !s.isEmpty()) {
					throw new IllegalStateException("No room for characters in element of width " + cur.getAvailableWidth());
				}
				
				// System.out.println("## numChars "+ numChars + " of \"" + s + "\"");
				
				height += textUtil.getTextLineHeight(cur, font);
				
				if (numChars == s.length()) {
					// was room for rest of string, exit
					break;
				}
				
				s = s.substring(numChars);
			}
		}
		else {
			// height is size of text line
			height = textUtil.getTextLineHeight(cur, font);
		}

		if (height > cur.getMaxBlockElementHeight()) {
			cur.setMaxBlockElementHeight(height);
		}

		// addWidthToCur(cur, width);
	}
    
    /*
    private void addWidthToCur(StackElement cur, int width) {
		switch (cur.layoutStyles.getDisplay()) {
		case BLOCK:
			// adds to width if is larger than current width
			// TODO margin and padding?
			break;

		case INLINE:
			// text adds to width of element
			cur.resultingLayout.getOuter().addToWidth(width);
			break;

		default:
			throw new UnsupportedOperationException("Unknown display " + cur.layoutStyles.getDisplay());
		}
    }
    */
	
	private void computeLayout(HTMLElement elementType, CSSLayoutStyles styles, StackElement cur, StackElement sub, Document<ELEMENT> document, ELEMENT element) {

		// Can we compute the dimensions of the element regardless of the position? Depends on overflow property etc, whether can scroll? Has to takes current layout into account
	
		if (styles.getFloat() != null) {
			throw new UnsupportedOperationException("TODO: support floats");
		}
		
		if (styles.getDisplay() == null) {
			throw new IllegalStateException("No CSS display computed for element of type " + elementType);
		}

		switch (styles.getDisplay()) {
		case BLOCK:
			
			// continue after next element on this index
			// computeBlockElementPosition(styles, dimensions);
			// Since this a block
			break;
			
		case INLINE:
			// computeInlineElementPosition(styles, dimensions);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown display style " + styles.getDisplay());
		}
		
	}
	
	private void computeBlockElementPosition(CSSLayoutStyles styles, Dimensions dimensions) {
		
	}

	private void computeInlineElementPosition(CSSLayoutStyles styles, Dimensions dimensions) {
		
	}

	private static int computeWidthPx(int width, CSSUnit widthUnit, int curWidth) {
		return computeSizePx(width, widthUnit, curWidth);
	}

	private static int computeHeightPx(int height, CSSUnit heightUnit, int curHeight) {
		return computeSizePx(height, heightUnit, curHeight);
	}

	private static int computeSizePx(int size, CSSUnit sizeUnit, int curSize) {
		final int ret;
		
		switch (sizeUnit) {
		case PX:
			ret = size;
			break;
			
		case EM:
			ret = pxFromEm(size);
			break;
			
		case PCT:
			ret = curSize == -1 ? -1 : percentOf(curSize, size);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + sizeUnit);
		}
		
		return ret;
	}
	
	private static int pxFromEm(int em) {
		// TODO
		return em;
	}
	
	private static int percentOf(int px, int pct) {
		return (px * pct) / 100; 
	}
}

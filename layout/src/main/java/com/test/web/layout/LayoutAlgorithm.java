package com.test.web.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.Tokenizer;
import com.test.web.render.common.IRenderFactory;


/*
/*
 * Main layout algorithm for document, implemented as a listener on start and end element events,
 * computes width for CSS speciefied elements on start element and tries to find dimensions
 * of other elements when processing end element, by adding up sizes.
 * 
 */

public class LayoutAlgorithm<ELEMENT, TOKENIZER extends Tokenizer>
	implements HTMLElementListener<ELEMENT, CSSContext<ELEMENT>> {

	// The input viewport, eg. size of browser window
	private final ViewPort viewPort;

	// For finding size of text strings when using a particular font for rendering
	private final TextUtil textUtil;

	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<StackElement> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensionsn are collected here
	private final PageLayout<ELEMENT> pageLayout;
	
	// For creating renderers, rendering occurs in the same pass (but renderer implenentation might just queue operations for later)
	private final IRenderFactory renderFactory;
	

	// Position of current display block
	private int curBlockYPos;
	
	
	
	public LayoutAlgorithm(ViewPort viewPort, ITextExtent textExtent, IRenderFactory renderFactory) {
		
		this.viewPort = viewPort;
		this.textUtil = new TextUtil(textExtent);

		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = new PageLayout<>();
		this.renderFactory = renderFactory;
		
		// TODO how does this work for other zIndex? Will they have their separate layout?
		this.curBlockYPos = 0;
		
		// Push intial element on stack
		push(viewPort.getViewPortWidth(), viewPort.getViewPortHeight());
	}

    @Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, CSSContext<ELEMENT> cssContext) {
   	
    	final StackElement cur = getCur();

    	// Push new sub-element onto stack
    	final StackElement sub = push(-1, -1);
    	
    	// Collect all layout styles from CSS
    	cssContext.getCSSLayoutStyles(
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				sub.layoutStyles);

    	// Also apply style attribute if defined
		final ICSSDocument<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			// Get CSS document from style-tag of element
			cssContext.applyLayoutStyles(styleAttribute, element, sub.layoutStyles);
		}
		
		// Adjust sub available width/height if is set
		// TODO: what if no width/height here and specified as percent? 
		
		if (sub.layoutStyles.hasWidth()) {
			// has width, compute and update
			final int width = computeWidthPx(sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), cur.getAvailableWidth());
			
			if (width != -1) {
				sub.resultingLayout.setHasCSSWidth(true);
				sub.resultingLayout.getDimensions().setWidth(width);

				sub.setAvailableWidth(width);
			}
		}
		
		if (sub.layoutStyles.hasHeight()) {
			// has width, compute and update
			final int height = computeHeightPx(sub.layoutStyles.getHeight(), sub.layoutStyles.getHeightUnit(), cur.getAvailableHeight());
			
			if (height != -1) {
				sub.resultingLayout.setHasCSSHeight(true);
				sub.resultingLayout.getDimensions().setHeight(height);

				sub.setAvailableHeight(height);
			}
		}
	}

    @Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, CSSContext<ELEMENT> cssContext) {
	
    	// End of element where wer're at
		final StackElement cur = getCur();
		
		pop();
		
		final StackElement parent  = getCur();

		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding?
		// does not know size of content yet so cannot for sure know height of element
		computeLayout(cur.layoutStyles, parent, cur, document, element);
		
		// Got layout, add to layer
		final short zIndex = cur.layoutStyles.getZIndex();
		
		final PageLayer<ELEMENT>layer = pageLayout.addOrGetLayer(zIndex, renderFactory);
		
		// Has computed sub element size by now so can add
		if (!parent.resultingLayout.hasCSSWidth()) {
			// no width from CSS so must add this element to size of curent element
			parent.resultingLayout.getDimensions().addToWidth(cur.resultingLayout.getDimensions().getWidth());
		}

		// Add to height of current element if is taller than max for current element
		final int height = cur.resultingLayout.getDimensions().getHeight();

		if (height > parent.getMaxBlockElementHeight()) {
			parent.setMaxBlockElementHeight(height);
		}
	
		/*
		if (!parent.resultingLayout.hasCSSHeight()) {
			// no width from CSS so must add this element to size of current element
			parent.resultingLayout.getDimensions().addToHeight(cur.resultingLayout.getDimensions().getHeight());
		}
		*/
	}


    @Override
	public void onText(Document<ELEMENT> document, String text, CSSContext<ELEMENT> cssContext) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

		final StackElement cur = getCur();

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

		addWidthToCur(cur, width);
	}
    
    private void addWidthToCur(StackElement cur, int width) {
		switch (cur.layoutStyles.getDisplay()) {
		case BLOCK:
			// adds to width if is larger than current width
			// TODO margin and padding?
			break;

		case INLINE:
			// text adds to width of element
			cur.resultingLayout.getDimensions().addToWidth(width);
			break;

		default:
			throw new UnsupportedOperationException("Unknown display " + cur.layoutStyles.getDisplay());
		}
    }
	
	private void computeLayout(CSSLayoutStyles styles, StackElement cur, StackElement sub, Document<ELEMENT> document, ELEMENT element) {

		// Can we compute the dimensions of the element regardless of the position? Depends on overflow property etc, whether can scroll? Has to takes current layout into account
	
		if (styles.getFloat() != null) {
			throw new UnsupportedOperationException("TODO: support floats");
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

	private StackElement getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	private StackElement push(int availableWidth, int availableHeight) {
		
		final StackElement ret;
		
		if (curDepth == stack.size()) {
			ret = new StackElement(availableWidth, availableHeight);
			
			stack.add(ret);
		}
		else {
			// reuse existing
			ret = stack.get(curDepth);
		}

		++ curDepth;
	
		return ret;
	}
	
	private void pop() {
		-- curDepth;
	}
	

	private static int computeWidthPx(int width, CSSUnit widthUnit, int curWidth) {

		final int ret;
		
		switch (widthUnit) {
		case PX:
			ret = width;
			break;
			
		case EM:
			ret = pxFromEm(width);
			break;
			
		case PCT:
			ret = curWidth == -1 ? -1 : percentOf(curWidth, width);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + widthUnit);
		}
		
		return ret;
	}

	private static int computeHeightPx(int height, CSSUnit heightUnit, int curHeight) {

		final int ret;
		
		switch (heightUnit) {
		case PX:
			ret = height;
			break;
			
		case EM:
			ret = pxFromEm(height);
			break;
			
		case PCT:
			ret = curHeight == -1 ? -1 : percentOf(curHeight, height);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + heightUnit);
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

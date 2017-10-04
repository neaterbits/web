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

	private final ViewPort viewPort;
	private final ITextExtent textExtent;
	
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	private final CSSLayoutStyles layoutStyles;
	
	// Compute element dimensions into a map? Or just in/order? Position?
	private final Dimensions dimensions;
	private final Wrapping margin;
	private final Wrapping padding;

	// We have to maintain a stack so that if width and height is computed
	private final List<ElementLayout> stack;
	private final Map<FontKey, IFont> fonts;
	
	private final PageLayout<ELEMENT> pageLayout;
	
	private final IRenderFactory renderFactory;
	
	private int curDepth;
	
	public LayoutAlgorithm(ViewPort viewPort, ITextExtent textExtent, IRenderFactory renderFactory) {
		
		this.viewPort = viewPort;
		this.textExtent = textExtent;

		this.layoutStyles = new CSSLayoutStyles();
		this.dimensions = new Dimensions();
		this.margin = new Wrapping();
		this.padding = new Wrapping();
		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = new PageLayout<>();
		this.renderFactory = renderFactory;
	}

    @Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, CSSContext<ELEMENT> cssContext) {
		
    	cssContext.getCSSLayoutStyles(
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				layoutStyles);

		final ICSSDocument<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			// Get CSS document from style-tag of element
			cssContext.applyLayoutStyles(styleAttribute, element, layoutStyles);
		}

		final ElementLayout cur = getCur();
		final ElementLayout sub = push();
		
		// Now should have collected relevant information to do layout and find the dimensions
		// of the element and also the margins and padding
		computeLayout(layoutStyles, cur, sub, document, element);
		
		// Got layout, add to layer
		final short zIndex = layoutStyles.getZIndex();
		
		pageLayout.addOrGetLayer(zIndex, renderFactory);
	}
	
    @Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, CSSContext<ELEMENT> cssContext) {
		
		final ElementLayout cur = getCur();
		
		pop();
		
		final ElementLayout parent  = getCur();
		
		// Has computed sub element size by now so can add
		if (!parent.hasCSSWidth()) {
			// no width from CSS so must add this element to size
			parent.getDimensions().addToWidth(cur.getDimensions().getWidth());
		}
		
		if (!parent.hasCSSHeight()) {
			// no width from CSS so must add this element to size
			parent.getDimensions().addToHeight(cur.getDimensions().getHeight());
		}
	}
	
    @Override
	public void onText(Document<ELEMENT> document, String text, CSSContext<ELEMENT> cssContext) {
		// We have a text element, compute text extents according to current mode
		// TODO: text-align, overflow

		final ElementLayout cur = getCur();
		
		final int width = textExtent.getTextExtent(cur.getFont(), text);
		
		switch (cur.getDisplay()) {
		case BLOCK:
			// adds to width if is larger than current width
			// TODO margin and padding?
			break;
			
		case INLINE:
			// text adds to width of element
			cur.getDimensions().addToWidth(width);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown display " + cur.getDisplay());
		}
	}
	
	
	private void computeLayout(CSSLayoutStyles styles, ElementLayout cur, ElementLayout sub, Document<ELEMENT> document, ELEMENT element) {
		// Can we compute the dimensions of the element regardless of the position? Depends on overflow property etc, whether can scroll? Has to takse current rlayout into account
		
		if (styles.getFloat() != null) {
			throw new UnsupportedOperationException("TODO: support floats");
		}

		computeElementDimensionsOnElementStart(styles, cur, sub);

		switch (styles.getDisplay()) {
		case BLOCK:
			// continue after next element on this index
			computeBlockElementPosition(styles, dimensions);
			break;
			
		case INLINE:
			computeInlineElementPosition(styles, dimensions);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown display style " + styles.getDisplay());
		}
		
	}
	
	private void computeBlockElementPosition(CSSLayoutStyles styles, Dimensions dimensions) {
		
	}

	private void computeInlineElementPosition(CSSLayoutStyles styles, Dimensions dimensions) {
		
	}

	private ElementLayout getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	private ElementLayout push() {
		
		final ElementLayout ret;
		
		if (curDepth == stack.size()) {
			ret = new ElementLayout();
			
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
	
	
	private void computeElementDimensionsOnElementStart(CSSLayoutStyles styles,  ElementLayout cur, ElementLayout sub) {
		// Does it have width set?
		if (styles.hasWidth()) {
			final int width = computeWidthPx(styles.getWidth(), styles.getWidthUnit(), cur.getDimensions(), viewPort);
			
			sub.setHasCSSWidth(true);
			sub.getDimensions().setWidth(width);
		}
		else {
			// No width set as part of CSS so we must compute element size from the element itself.
			// This depends on the type of element, the available space etc.
		}
		
		
		// Does it have height? If not, compute height of element
		if (styles.hasHeight()) {
			final int height = computeWidthPx(styles.getHeight(), styles.getHeightUnit(), cur.getDimensions(), viewPort);
			
			sub.setHasCSSHeight(true);
			sub.getDimensions().setHeight(height);
		}
		else {
			// No width set as part of CSS so we must compute element size from the element itself.
			// This depends on the type of element, the available space etc.
		}
	}
	
	private static int computeWidthPx(int width, CSSUnit widthUnit, Dimensions cur,  ViewPort viewPort) {

		final int ret;
		
		switch (widthUnit) {
		case PX:
			ret = width;
			break;
			
		case EM:
			ret = pxFromEm(width);
			break;
			
		case PCT:
			ret = percentOf(cur != null ? cur.getWidth() : viewPort.getViewPortWidth(), width);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + widthUnit);
		}
		
		return ret;
	}

	private static int computeHeightPx(int height, CSSUnit heightUnit, Dimensions cur,  ViewPort viewPort) {

		final int ret;
		
		switch (heightUnit) {
		case PX:
			ret = height;
			break;
			
		case EM:
			ret = pxFromEm(height);
			break;
			
		case PCT:
			ret = percentOf(cur != null ? cur.getHeight() : viewPort.getViewPortHeight(), height);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + heightUnit);
		}
		
		return ret;
	}
	
	private static class ElemenSums {
		private int width;
		private int height;
	}

	private static int pxFromEm(int em) {
		// TODO
		return em;
	}
	
	private static int percentOf(int px, int pct) {
		return (px * pct) / 100; 
	}
}

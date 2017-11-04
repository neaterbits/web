package com.test.web.layout;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.ICSSDocumentStyles;
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
	private final FontSettings fontSettings;
	
	private final TextUtil textUtil;
	
	public LayoutAlgorithm(ITextExtent textExtent, IRenderFactory renderFactory, FontSettings fontSettings) {
		this.textExtent = textExtent;
		this.textUtil = new TextUtil(textExtent);
		this.fontSettings = fontSettings;
		this.renderFactory = renderFactory;
	}

	public PageLayout<ELEMENT> layout(Document<ELEMENT> document, ViewPort viewPort, CSSContext<ELEMENT> cssContext, HTMLElementListener<ELEMENT, IElementRenderLayout> listener) {
		
		final LayoutState<ELEMENT> state = new LayoutState<>(textExtent, viewPort, cssContext, listener);
		
		document.iterate(this, state);
		
		return state.getPageLayout();
	}
	
    @Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {

    	System.out.println("## onElementStart " + document.getType(element));
    	
    	final HTMLElement elementType = document.getType(element);

    	if (!elementType.isLayoutElement()) {
    		return;
    	}
    	
    	final StackElement cur = state.getCur();

    	// Push new sub-element onto stack
    	final StackElement sub = state.push(-1, -1);
    	
    	// Collect all layout styles from CSS
    	state.getCSSContext().getCSSLayoutStyles(
    			elementType.getDefaultDisplay(),
    			fontSettings.getFontForElement(elementType),
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				sub.layoutStyles);

    	// Also apply style attribute if defined
		final ICSSDocumentStyles<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			System.out.println("## applying layout styles to element of type " + document.getType(element));
			// Get CSS document from style-tag of element
			state.getCSSContext().applyLayoutStyles(styleAttribute, element, sub.layoutStyles);

			System.out.println("## applied layout styles for " + document.getType(element));
		}
		
		// Adjust sub available width/height if is set
		// TODO: what if no width/height here and specified as percent? 
		
		if (sub.layoutStyles.hasWidth()) {
			// has width, compute and update
			final int width = computeWidthPx(sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), cur.getAvailableWidth());
			
			if (width != -1) {
				
				if (width == 0) {
					throw new IllegalStateException("Computed width 0 from "  + sub.layoutStyles.getWidth() + " of unit " + sub.layoutStyles.getWidthUnit());
				}
				
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
		
		// Set resulting font
		final FontSpec spec = sub.layoutStyles.getFont();

		final IFont font = state.getOrOpenFont(spec, FontStyle.NONE); // TODO: font styles
		
		sub.resultingLayout.setFont(font);
		
		if (state.getListener() != null) {
			state.getListener().onElementStart(document, element, null);
		}

		System.out.println("## onElementEnd" + document.getType(element));
	}

    @Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, LayoutState<ELEMENT> state) {
	
    	final HTMLElement elementType = document.getType(element);
    	
    	if (!elementType.isLayoutElement()) {
    		return;
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

		layer.add(element, cur.resultingLayout);

		// Has computed sub element size by now so can add
		if (!parent.resultingLayout.hasCSSWidth()) {
			// no width from CSS so must add this element to size of current element
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
				
				System.out.println("## numChars "+ numChars + " of \"" + s + "\"");
				
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

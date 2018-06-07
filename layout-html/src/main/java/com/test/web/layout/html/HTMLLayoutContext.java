package com.test.web.layout.html;

import java.lang.annotation.ElementType;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.IDocument;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.types.FontSpec;

public class HTMLLayoutContext<ELEMENT, ATTRIBUTE>
	implements ILayoutContext<ELEMENT, HTMLElement, IDocument<ELEMENT, ATTRIBUTE>>{

	private final CSSContext<ELEMENT> cssContext;

	public HTMLLayoutContext(CSSContext<ELEMENT> cssContext) {

		if (cssContext == null) {
			throw new IllegalArgumentException("cssContext == null");
		}

		this.cssContext = cssContext;
	}

	@Override
	public long getKnownWidthHeight(IDocument<ELEMENT, ATTRIBUTE> document, ELEMENT element) {

		final HTMLElement type = document.getType(element);
		
		final long result;
		
		if (type == HTMLElement.IMG) {
			final ATTRIBUTE widthAttribute = document.getAttributeWithName(element, "width");
			final ATTRIBUTE heightAttribute = document.getAttributeWithName(element, "height");

			if (widthAttribute != null && heightAttribute != null) {
				final long width = Integer.parseInt(document.getAttributeValue(element, widthAttribute));
				final long height = Integer.parseInt(document.getAttributeValue(element, heightAttribute));
				
				result = width << 32 | height;
			}
			else {
				result = -1;
			}
		}
		else {
			result = -1;
		}

		return result;
	}

	@Override
	public void computeLayoutStyles(
			IDocument<ELEMENT, ATTRIBUTE> document,
			ELEMENT element,
			IFontSettings<HTMLElement> fontSettings,
			LayoutStyles result,
			int debugDepth,
			ILayoutDebugListener<HTMLElement> debugListener) {
		
		final HTMLElement elementType = document.getType(element);
				
		final FontSpec defaultFont = fontSettings.getFontForElement(elementType);
    	
    	if (defaultFont == null) {
    		throw new IllegalStateException("No default font for element " + elementType);
    	}

    	// Collect all layout styles from CSS
    	cssContext.getCSSLayoutStyles(
    			elementType.getDefaultDisplay(),
    			defaultFont,
				document.getId(element),
				document.getTag(element),
				document.getClasses(element),
				result);
    	
    	if (debugListener != null) {
    		debugListener.onElementCSS(debugDepth, result);
    	}

    	// Also apply style attribute if defined
		final ICSSDocumentStyles<ELEMENT> styleAttribute = document.getStyles(element);

		if (styleAttribute != null) {
			// Get CSS document from style-tag of element
			cssContext.applyLayoutStyles(styleAttribute, element, result);

	    	if (debugListener != null) {
	    		debugListener.onElementStyleAttribute(debugDepth, result);
	    	}
		}
		
	}
}

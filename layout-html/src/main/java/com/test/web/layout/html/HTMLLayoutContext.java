package com.test.web.layout.html;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.IDocument;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.types.FontSpec;

public class HTMLLayoutContext<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>>
	implements ILayoutContext<ELEMENT, HTMLElement, DOCUMENT>{

	private final CSSContext<ELEMENT> cssContext;

	public HTMLLayoutContext(CSSContext<ELEMENT> cssContext) {

		if (cssContext == null) {
			throw new IllegalArgumentException("cssContext == null");
		}

		this.cssContext = cssContext;
	}

	@Override
	public void computeLayoutStyles(
			DOCUMENT document,
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

package com.test.web.document.html._long;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.test.web.css._long.BaseLongCSSDocument;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.html.IHTMLStyleParserListener;

// Keeps track of style-attribute contents for all HTML elements within the DOM
final class LongStyleDocument extends BaseLongCSSDocument
	implements IHTMLStyleParserListener<Integer, Void> {

	private final Map<Integer, int[]> htmlToStyleElements;
	
	public LongStyleDocument() {
		this.htmlToStyleElements = new HashMap<>();
	}
	
	@Override
	public Void onBlockStart(CSSRuleType ruleType) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onEntityMap(Void context, CSSTarget entity, String id) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onStylePropertyText(Void context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onBlockEnd(Void context, Tokenizer tokenizer, long blockStartPos, long blockEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}
	
	@Override
    public ICSSDocumentStyles<Integer> makeCSSDocumentStylesCopy(Integer ref) {
        throw new UnsupportedOperationException("TODO");
    }

    // Called before starting parsing of a CSS element, allows us to map the parsed element
	// to the HTML element that contains it
	@Override
	public void startParseStyleElement(Integer htmlElement, String styleText) {
		final int [] existing = htmlToStyleElements.get(htmlElement);
		
		final int cssElement = allocateCurParseElement();
		
		if (existing == null) {
			// Allocate new array
			final int [] array = new int [] { cssElement };
			
			htmlToStyleElements.put(htmlElement, array);
		}
		else {
			final int [] array = Arrays.copyOf(existing, existing.length + 1);
			
			array[existing.length] = cssElement;
		}
	}

	boolean hasElement(int element) {
		return htmlToStyleElements.containsKey(element);
	}
}

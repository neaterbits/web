package com.test.web.document.html.oo;

import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.oo.BaseOOCSSDocument;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.html.IHTMLStyleParserListener;

public class OOStyleDocument extends BaseOOCSSDocument
	implements IHTMLStyleParserListener<OOTagElement> {

	public OOStyleDocument() {
	}

	@Override
	public Void onBlockStart(CSSRuleType ruleType) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}
	
	@Override
	public void onStylePropertyText(Void context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onEntityMap(Void context, CSSTarget entity, String id) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onBlockEnd(Void context, Tokenizer tokenizer, long blockStartPos, long blockEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	// Called before starting parsing of a CSS element, allows us to map the
	// parsed element
	// to the HTML element that contains it
	@Override
	public void startParseStyleElement(OOTagElement htmlElement, String styleText) {

		OOCSSRule cssElement = htmlElement.getStyleElement();

		if (cssElement == null) {
			cssElement = allocateCurParseElement(CSSRuleType.STYLE);

			htmlElement.setStyle(cssElement, styleText);
		}
	}
}

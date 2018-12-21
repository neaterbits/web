package com.test.web.document.html.oo;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.oo.BaseOOCSSDocument;
import com.test.web.css.oo.OOCSSBase;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.html.IHTMLStyleParserListener;

public class OOStyleDocument extends BaseOOCSSDocument
	implements IHTMLStyleParserListener<OOTagElement, OOCSSBase> {

	public OOStyleDocument() {
	}

	public OOStyleDocument(OOCSSRule rule) {
	    setCurParseElement(rule);
    }

	@Override
	public OOCSSBase onBlockStart(CSSRuleType ruleType) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}
	
	@Override
	public void onStylePropertyText(OOCSSBase context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onEntityMap(OOCSSBase context, CSSTarget entity, String id) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onBlockEnd(OOCSSBase context, Tokenizer tokenizer, long blockStartPos, long blockEndPos) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	// Called before starting parsing of a HTML element style attribute, allows us to map the
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

    @Override
    public ICSSDocumentStyles<OOCSSRule> makeCSSDocumentStylesCopy(OOCSSRule ref) {
        return new OOStyleDocument(ref.makeCopy());
    }
}

package com.test.web.css.oo;


import java.util.Collections;
import java.util.List;

import com.test.web.css.common.CSSState;
import com.test.web.css.common.CSSValueType;
import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.css.ICSSDocumentParserListener;

/***
 * For storing and accessing a CSS document, encoding in arrays of long [] for less GC and memory churn
 * 
 * Must be singlethreaded during parsing
 * 
 * @author nhl
 *
 */

public final class OOCSSDocument
	extends BaseOOCSSDocument
	implements ICSSDocumentParserListener<OOCSSRule, Void> {

	private final CSSState<OOCSSRule> state;

	public OOCSSDocument() {
		this.state = new CSSState<>();
	}

	/***************************************************** Access interface *****************************************************/ 

	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final List<OOCSSRule> entries = state.get(target, targetName);

		final boolean isSet;

		if (entries == null) {
			isSet = false;
		}
		else {
			boolean has = false;
				
			for (OOCSSRule entry : entries) {
				if (styles(entry).hasStyle(style)) {
					has = true;
					break;
				}
			}
			
			isSet = has;
		}

		return isSet;
	}
	
	@Override
	public List<OOCSSRule> get(CSSTarget target, String targetName) {
		final List<OOCSSRule> elems = state.get(target, targetName);
		
		return elems != null ? elems : Collections.emptyList();
	}
	

	/***************************************************** Parse listener *****************************************************/ 


	@Override
	public Void onBlockStart(CSSRuleType ruleType) {
		allocateCurParseElement(ruleType);
		
		return null;
	}
	
	
	@Override
	public void onEntityMap(Void context, CSSTarget target, String targetName) {
		
		//System.out.println("## CSS target start " + target + "/" + targetName);
		
	   state.add(target, targetName, rule());
	}
	
	@Override
	public void onStylePropertyText(Void context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public void onBlockEnd(Void context, Tokenizer tokenizer, long blockStartPos, long blockEndPos) {
		stylesRef().setCSSText(tokenizer.asString(blockStartPos, blockEndPos));
	}

	/***************************************************** ICSSStyleSheet *****************************************************/ 


	@Override
	public CSSRuleType getRuleType(OOCSSRule rule) {
		return rule.getRuleType();
	}

	@Override
	public String getStyleSelectorText(OOCSSRule rule) {
		return ((OOCSSStylesRule)rule).getSelectorText();
	}

	@Override
	public void setStyleSelectorText(OOCSSRule rule, String selectorText) {
		// TODO parse
		((OOCSSStylesRule)rule).setSelectorText(selectorText);
	}

	@Override
	public int getStyleLength(OOCSSRule rule) {
		return styles(rule).getLength();
	}

	@Override
	public CSSValueType getStylePropertyCSSValueType(OOCSSRule rule, String propertyName) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public String getStyleCSSText(OOCSSRule rule) {
		return styles(rule).getCSSText();
	}

	@Override
	public void setStyleCSSText(OOCSSRule rule, String cssText) {
		// TODO parse
		styles(rule).setCSSText(cssText);
	}

	@Override
	public String getStylePropertyPriority(OOCSSRule rule, String propertyName) {
		return styles(rule).getPropertyPriority(propertyName);
	}

	@Override
	public String getStylePropertyValue(OOCSSRule rule, String propertyName) {
		return styles(rule).getPropertyValue(propertyName);
	}

	@Override
	public String getStyleItem(OOCSSRule rule, int index) {
		return styles(rule).getStyleItem(index);
	}

	@Override
	public String removeStyleProperty(OOCSSRule rule, String propertyName) {
		return styles(rule).removeProperty(propertyName);
	}

	@Override
	public void setStyleProperty(OOCSSRule rule, String propertyName, String value, String priority) {
		styles(rule).setProperty(propertyName, value, priority);
	}

	@Override
	public String getCharsetEncoding(OOCSSRule rule) {
		return ((OOCSSCharsetRule)rule).getEncoding();
	}

	@Override
	public void setCharsetEncoding(OOCSSRule rule, String encoding) {
		// TODO Auto-generated method stub
		((OOCSSCharsetRule)rule).setEncoding(encoding);
	}

	@Override
	public String getCounterStyleName(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleName(OOCSSRule rule, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSystem(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSystem(OOCSSRule rule, String system) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSymbols(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSymbols(OOCSSRule rule, String symbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleAdditiveSymbols(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleAdditiveSymbols(OOCSSRule rule, String additiveSymbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleNegative(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleNegative(OOCSSRule rule, String negative) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStylePrefix(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStylePrefix(OOCSSRule rule, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSuffix(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSuffix(OOCSSRule rule, String suffix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleRange(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleRange(OOCSSRule rule, String range) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStylePad(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStylePad(OOCSSRule rule, String pad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSpeakAs(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSpeakAs(OOCSSRule rule, String speakAs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleFallback(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleFallback(OOCSSRule rule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentUrl(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentUrl(OOCSSRule rule, String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentUrlPrefix(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentUrlPrefix(OOCSSRule rule, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentDomain(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentDomain(OOCSSRule rule, String domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentRegexp(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentRegexp(OOCSSRule rule, String regexp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getImportHref(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendImportMedium(OOCSSRule rule, String newMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteImportMedium(OOCSSRule rule, String oldMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getImportMediaLength(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getImportMediaText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImportMediaItem(OOCSSRule rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setImportMediaText(OOCSSRule rule, String mediaText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICSSStyleSheet<OOCSSRule> getImportStyleSheet(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getKeyframesCSSRulesLength(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OOCSSRule getKeyframesCSSRulesItem(OOCSSRule rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKeyframesName(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeyframesName(OOCSSRule rule, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendKeyframesRule(OOCSSRule rule, String keyframeRule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteKeyframesRule(OOCSSRule rule, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OOCSSRule findKeyframesRule(OOCSSRule rule, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertKeyframesRule(OOCSSRule rule, String keyframeRule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKeyframeKeyText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeyframeKeyText(OOCSSRule rule, String keyText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMediaCSSRulesLength(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OOCSSRule getMediaCSSRulesItem(OOCSSRule rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendMediaMedium(OOCSSRule rule, String newMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMediaMedium(OOCSSRule rule, String oldMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMediaLength(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMediaText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMediaItem(OOCSSRule rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMediaText(OOCSSRule rule, String mediaText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMediaCSSRule(OOCSSRule rule, long index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertMediaCSSRule(OOCSSRule rule, String mediaRule, long index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMediaConditionText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMediaConditionText(OOCSSRule rule, String conditionText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNamespaceURI(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespacePrefix(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageSelectorText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPageSelectorText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSupportsCSSRulesLength(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OOCSSRule getSupportsCSSRulesItem(OOCSSRule rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSupportsCSSRule(OOCSSRule rule, long index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertSupportsCSSRule(OOCSSRule rule, String supportsRule, long index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSupportsConditionText(OOCSSRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSupportsConditionText(OOCSSRule rule, String conditionText) {
		// TODO Auto-generated method stub
		
	}
}

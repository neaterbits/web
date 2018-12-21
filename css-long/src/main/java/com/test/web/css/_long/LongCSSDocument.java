package com.test.web.css._long;

import java.util.List;

import com.test.web.css.common.CSSState;
import com.test.web.css.common.CSSValueType;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.css.common.enums.CSSColorGamut;
import com.test.web.css.common.enums.CSSDisplayMode;
import com.test.web.css.common.enums.CSSHover;
import com.test.web.css.common.enums.CSSInvertedColors;
import com.test.web.css.common.enums.CSSLightLevel;
import com.test.web.css.common.enums.CSSMediaFeature;
import com.test.web.css.common.enums.CSSMediaType;
import com.test.web.css.common.enums.CSSOrientation;
import com.test.web.css.common.enums.CSSOverflowBlock;
import com.test.web.css.common.enums.CSSOverflowInline;
import com.test.web.css.common.enums.CSSPointer;
import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.common.enums.CSSScan;
import com.test.web.css.common.enums.CSSScripting;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUpdate;
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

public final class LongCSSDocument
	extends BaseLongCSSDocument
	implements ICSSDocumentParserListener<Integer, Void> {

	private final CSSState<Integer> state;

	public LongCSSDocument() {
		this.state = new CSSState<>();
	}
	
	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final List<Integer> entries = state.get(target, targetName);

		final boolean isSet;

		if (entries == null) {
			isSet = false;
		}
		else {
			boolean has = false;
			for (int entry : entries) {
				if (LongCSS.hasStyle(style, buf(entry), offset(entry))) {
					has = true;
					break;
				}
			}
			
			isSet = has;
		}

		return isSet;
	}
	
	@Override
	public List<Integer> get(CSSTarget target, String targetName) {
		return state.get(target, targetName);
	}
	

	/***************************************************** Parse listener *****************************************************/ 

	@Override
	public Void onBlockStart(CSSRuleType ruleType) {
		allocateCurParseElement();
		
		return null;
	}
	
	@Override
	public void onEntityMap(Void context, CSSTarget target, String targetName) {
		
		// Started a CSS entity, must allocate out of buffer
		// Start out allocating a compact element, we might re-allocate if turns out to have more than the standard compact elements
		
		state.add(target, targetName, ref());
	}

	@Override
	public void onStylePropertyText(Void context, Tokenizer tokenizer, long propertyStartPos, long propertyEndPos) {
		
	}

	@Override
	public void onBlockEnd(Void context, Tokenizer tokenizer, long blockStartPos, long blockEndPos) {
		// Nothing to do as write pos in buffer was already advanced
	}

	/***************************************************** ICSSStyleSheet *****************************************************/ 

	@Override
	public CSSRuleType getRuleType(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStyleSelectorText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyleSelectorText(Integer rule, String selectorText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStyleLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSStyle getStyleType(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStyleCustomPropertyName(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStyleCustomPropertyValue(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSValueType getStylePropertyCSSValueType(Integer rule, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStyleCSSText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyleCSSText(Integer rule, String cssText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStylePropertyPriority(Integer rule, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getStylePropertyPriority(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStylePropertyValue(Integer rule, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStyleItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeStyleProperty(Integer rule, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyleProperty(Integer rule, String propertyName, String value, String priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCharsetEncoding(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCharsetEncoding(Integer rule, String encoding) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleName(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleName(Integer rule, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSystem(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSystem(Integer rule, String system) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSymbols(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSymbols(Integer rule, String symbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleAdditiveSymbols(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleAdditiveSymbols(Integer rule, String additiveSymbols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleNegative(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleNegative(Integer rule, String negative) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStylePrefix(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStylePrefix(Integer rule, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSuffix(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSuffix(Integer rule, String suffix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleRange(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleRange(Integer rule, String range) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStylePad(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStylePad(Integer rule, String pad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleSpeakAs(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleSpeakAs(Integer rule, String speakAs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCounterStyleFallback(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterStyleFallback(Integer rule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentUrl(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentUrl(Integer rule, String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentUrlPrefix(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentUrlPrefix(Integer rule, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentDomain(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentDomain(Integer rule, String domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocumentRegexp(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentRegexp(Integer rule, String regexp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getImportHref(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendImportMedium(Integer rule, String newMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteImportMedium(Integer rule, String oldMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getImportMediaLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getImportMediaText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImportMediaItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setImportMediaText(Integer rule, String mediaText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICSSStyleSheet<Integer> getImportStyleSheet(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getKeyframesCSSRulesLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getKeyframesCSSRulesItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKeyframesName(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeyframesName(Integer rule, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendKeyframesRule(Integer rule, String keyframeRule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteKeyframesRule(Integer rule, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer findKeyframesRule(Integer rule, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertKeyframesRule(Integer rule, String keyframeRule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKeyframeKeyText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeyframeKeyText(Integer rule, String keyText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMediaCSSRulesLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getMediaCSSRulesItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendMediaMedium(Integer rule, String newMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMediaMedium(Integer rule, String oldMedium) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMediaLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMediaText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMediaItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMediaText(Integer rule, String mediaText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMediaCSSRule(Integer rule, long index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertMediaCSSRule(Integer rule, String mediaRule, long index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMediaConditionText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMediaConditionText(Integer rule, String conditionText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNamespaceURI(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespacePrefix(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageSelectorText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPageSelectorText(Integer rule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSupportsCSSRulesLength(Integer rule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getSupportsCSSRulesItem(Integer rule, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSupportsCSSRule(Integer rule, long index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertSupportsCSSRule(Integer rule, String supportsRule, long index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSupportsConditionText(Integer rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSupportsConditionText(Integer rule, String conditionText) {
		// TODO Auto-generated method stub
		
	}

	// Generic rule access
	@Override
	public int getNumRules() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSRuleType getRuleType(int ruleIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	// Iimports
	@Override
	public String getImportUrl(int ruleIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImportFile(int ruleIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumMediaQueries(int ruleIdx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMediaQueryNegated(int ruleIdx, int mediaQueryIdx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CSSMediaType getMediaType(int ruleIdx, int mediaQueryIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumMediaFeatures(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CSSMediaFeature getMediaFeature(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getWidth(int ruleIdx, int mediaQueryIdx, int[] nesting, GetDimension getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getHeight(int ruleIdx, int mediaQueryIdx, int[] nesting, GetDimension getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAspectRatio(int ruleIdx, int mediaQueryIdx, int[] nesting, GetAspectRatio getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CSSOrientation getOrientation(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getResolution(int ruleIdx, int mediaQueryIdx, int[] nesting, GetIntegerValue getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CSSScan getScan(int ruleIdx, int mediaQueryIdx, int[] nesting, GetIntegerValue getter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getGrid(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CSSUpdate getUpdate(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSOverflowBlock getOverflowBlock(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSOverflowInline getOverflowInline(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getColor(int ruleIdx, int mediaQueryIdx, int[] nesting, GetIntegerValue getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CSSColorGamut getColorGamut(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getColorIndex(int ruleIdx, int mediaQueryIdx, int[] nesting, GetIntegerValue getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CSSDisplayMode getDisplayMode(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getMonocrome(int ruleIdx, int mediaQueryIdx, int[] nesting, GetIntegerValue getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CSSInvertedColors getInvertedColors(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSPointer getPointer(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSHover getHover(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSPointer getAnyPointer(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSHover getAnyHover(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSLightLevel getLightLevel(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSScripting getScripting(int ruleIdx, int mediaQueryIdx, int[] nesting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getDeviceWidth(int ruleIdx, int mediaQueryIdx, int[] nesting, GetDimension getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDeviceHeight(int ruleIdx, int mediaQueryIdx, int[] nesting, GetDimension getter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDeviceAspectRatio(int ruleIdx, int mediaQueryIdx, int[] nesting, GetAspectRatio getter) {
		// TODO Auto-generated method stub
		
	}
	
	
    @Override
    public ICSSDocumentStyles<Integer> makeCSSDocumentStylesCopy(Integer ref) {
        throw new UnsupportedOperationException();
    }
}

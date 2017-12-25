package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.common.dom.IMediaList;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.ICSSConditionRule;
import com.test.web.jsapi.cssom.ICSSRuleList;

public class CSSMediaRule<RULE> extends CSSRule<RULE> implements ICSSConditionRule<RULE> {

	private final ICSSRuleList<RULE> cssRules;
	private final IMediaList media;
	
	public CSSMediaRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
		
		this.cssRules = new ICSSRuleList<RULE>() {

			@Override
			public int getLength() {
				return getAccessStyleSheet().getMediaCSSRulesLength(getRule());
			}

			@Override
			public CSSRule<RULE> item(int index) {
				
				final RULE mediaRule = getAccessStyleSheet().getMediaCSSRulesItem(getRule(), index);
				
				return mediaRule != null ? createRule(getAccessStyleSheet(), rule) : null;
			}
		};
		
		this.media = new IMediaList() {
			
			@Override
			public void setMediaText(String mediaText) {
				getAccessStyleSheet().setMediaText(getRule(), mediaText);
			}
			
			@Override
			public String item(int index) {
				return getAccessStyleSheet().getMediaItem(getRule(), index);
			}
			
			@Override
			public String getMediaText() {
				return getAccessStyleSheet().getMediaText(getRule());
			}
			
			@Override
			public int getLength() {
				return getAccessStyleSheet().getMediaCSSRulesLength(getRule());
			}
			
			@Override
			public void deleteMedium(String medium) {
				getAccessStyleSheet().deleteMediaMedium(getRule(), medium);
			}
			
			@Override
			public void appendMedium(String medium) {
				getAccessStyleSheet().appendMediaMedium(getRule(), medium);
			}
		};
	}
	
	public IMediaList getMedia() {
		return media;
	}

	@Override
	public ICSSRuleList<RULE> getCssRules() {
		return cssRules;
	}

	@Override
	public long insertRule(String rule, long index) {
		return getAccessStyleSheet().insertMediaCSSRule(getRule(), rule, index);
	}
	
	
	@Override
	public void deleteRule(long index) {
		getAccessStyleSheet().deleteMediaCSSRule(getRule(), index);
	}

	@Override
	public String getConditionText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConditionText(String conditionText) {
		// TODO Auto-generated method stub
		
	}


}

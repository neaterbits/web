package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.common.dom.IMediaList;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.CSSStyleSheet;

public final class CSSImportRule<RULE> extends CSSRule<RULE> {

	private final IMediaList media;
	
	public CSSImportRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
		
		this.media = new IMediaList() {
			
			@Override
			public void setMediaText(String mediaText) {
				getAccessStyleSheet().setImportMediaText(getRule(), mediaText);
			}
			
			@Override
			public String item(int index) {
				return getAccessStyleSheet().getImportMediaItem(getRule(), index);
			}
			
			@Override
			public String getMediaText() {
				return getAccessStyleSheet().getImportMediaText(getRule());
			}
			
			@Override
			public int getLength() {
				return getAccessStyleSheet().getImportMediaLength(getRule());
			}
			
			@Override
			public void deleteMedium(String medium) {
				getAccessStyleSheet().deleteImportMedium(getRule(), medium);
			}
			
			@Override
			public void appendMedium(String medium) {
				getAccessStyleSheet().appendImportMedium(getRule(), medium);
			}
		};
	}

	public String getHref() {
		return getAccessStyleSheet().getImportHref(getRule());
	}
	
	public IMediaList getMedia() {
		return media;
	}
	
	// TODO stylesheet
	public CSSStyleSheet<RULE> getStyleSheet() {
		final ICSSStyleSheet<RULE> styleSheet = getAccessStyleSheet().getImportStyleSheet(getRule());
		
		return styleSheet != null ? new CSSStyleSheet<>(styleSheet) : null;
	}
}

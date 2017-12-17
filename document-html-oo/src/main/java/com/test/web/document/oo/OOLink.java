package com.test.web.document.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.ICommonLinkAttributes;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.document.html.common.enums.LinkRevType;

final class OOLink extends OOReferenceElement implements ICommonLinkAttributes {

	private final OOCommonLinkAttributes commonLinkAttributes;
	
	OOLink() {
		this.commonLinkAttributes = new OOCommonLinkAttributes();
	}
	
	@Override
	HTMLElement getType() {
		return HTMLElement.LINK;
	}

	@Override
	public String getHRef() {
		return commonLinkAttributes.getHRef();
	}

	@Override
	public void setHRef(String href) {
		commonLinkAttributes.setHRef(setOrClearAttribute(HTMLAttribute.HREF, href));
	}

	@Override
	public String getHRefLang() {
		return commonLinkAttributes.getHRefLang();
	}

	@Override
	public void setHRefLang(String hrefLang) {
		commonLinkAttributes.setHRefLang(setOrClearAttribute(HTMLAttribute.HREFLANG, hrefLang));
	}

	@Override
	public String getMedia() {
		return commonLinkAttributes.getMedia();
	}

	@Override
	public void setMedia(String media) {
		commonLinkAttributes.setMedia(setOrClearAttribute(HTMLAttribute.MEDIA, media));
	}

	@Override
	public String getMediaType() {
		return commonLinkAttributes.getMediaType();
	}

	@Override
	public void setMediaType(String type) {
		commonLinkAttributes.setMediaType(setOrClearAttribute(HTMLAttribute.TYPE, type));
	}

	@Override
	public LinkRelType getRel() {
		return commonLinkAttributes.getRel();
	}

	@Override
	public void setRel(LinkRelType rel) {
		commonLinkAttributes.setRel(setOrClearAttribute(HTMLAttribute.REL, rel));
	}

	@Override
	public LinkRevType getRev() {
		return commonLinkAttributes.getRev();
	}

	@Override
	public void setRev(LinkRevType rev) {
		commonLinkAttributes.setRev(setOrClearAttribute(HTMLAttribute.REV, rev));
	}

	@Override
	String getStandardAttributeValue(HTMLAttribute attribute) {
		
		final String value;
		
		switch (attribute) {
		case HREF:
		case HREFLANG:
		case MEDIA:
		case TYPE:
		case REL:
		case REV:
			value = commonLinkAttributes.getAttributeValue(attribute);
			break;
			
		default:
			value = super.getStandardAttributeValue(attribute);
			break;
		}

		return value;
	}

	@Override
	void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state) {
		switch (attribute) {
		case HREF:
		case HREFLANG:
		case MEDIA:
		case TYPE:
		case REL:
		case REV:
			final boolean wasSet = commonLinkAttributes.setAttributeValue(attribute, value);
			setOrClearAttributeFlag(attribute, wasSet);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}
}

package com.test.web.document.oo;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.ICommonLinkAttributes;
import com.test.web.document.common.enums.LinkRelType;

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
}

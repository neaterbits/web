package com.test.web.document.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.ICommonDisplayableLinkAttributes;
import com.test.web.document.common.ICommonLinkAttributes;
import com.test.web.document.common.enums.LinkRelType;
import com.test.web.document.common.enums.LinkRevType;
import com.test.web.document.common.enums.HTMLTarget;

final class OOAElement extends OOInlineElement
	implements ICommonLinkAttributes, ICommonDisplayableLinkAttributes {

	// must delegate since no mixins
	private final OODisplayableLinkAttributes commonLinkAttributes;
	
	OOAElement() {
		this.commonLinkAttributes = new OODisplayableLinkAttributes();
	}

	@Override
	HTMLElement getType() {
		return HTMLElement.A;
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

	public String getMedia() {
		return commonLinkAttributes.getMedia();
	}

	public void setMedia(String media) {
		commonLinkAttributes.setMedia(setOrClearAttribute(HTMLAttribute.MEDIA, media));
	}

	public String getMediaType() {
		return commonLinkAttributes.getMediaType();
	}

	public void setMediaType(String type) {
		commonLinkAttributes.setMediaType(setOrClearAttribute(HTMLAttribute.TYPE, type));
	}

	public LinkRelType getRel() {
		return commonLinkAttributes.getRel();
	}

	public void setRel(LinkRelType rel) {
		commonLinkAttributes.setRel(setOrClearAttribute(HTMLAttribute.REL, rel));
	}

	@Override
	public String getDownload() {
		return commonLinkAttributes.getDownload();
	}

	@Override
	public void setDownload(String download) {
		commonLinkAttributes.setDownload(setOrClearAttribute(HTMLAttribute.DOWNLOAD, download));
	}

	@Override
	public HTMLTarget getTarget() {
		return commonLinkAttributes.getTarget();
	}

	@Override
	public void setTarget(HTMLTarget target, String targetFrame) {
		
		setOrClearAttributeFlag(HTMLAttribute.TARGET, target != null || targetFrame != null);
		
		commonLinkAttributes.setTarget(target, targetFrame);
	}

	@Override
	public String getTargetFrame() {
		return commonLinkAttributes.getTargetFrame();
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
		case DOWNLOAD:
		case TARGET:
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
		case DOWNLOAD:
		case TARGET:
			final boolean wasSet = commonLinkAttributes.setAttributeValue(attribute, value);
			setOrClearAttributeFlag(attribute, wasSet);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}

	@Override
	public LinkRevType getRev() {
		return commonLinkAttributes.getRev();
	}

	@Override
	public void setRev(LinkRevType rev) {
		commonLinkAttributes.setRev(setOrClearAttribute(HTMLAttribute.REV, rev));
	}
}

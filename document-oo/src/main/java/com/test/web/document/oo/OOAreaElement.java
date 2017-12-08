package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.ICommonDisplayableLinkAttributes;
import com.test.web.document.common.ICommonLinkAttributes;
import com.test.web.document.common.enums.LinkRelType;
import com.test.web.document.common.enums.Target;

final class OOAreaElement extends OOLeafElement
		implements ICommonLinkAttributes, ICommonDisplayableLinkAttributes {

	// must delegate since no mixins
	private final OODisplayableLinkAttributes commonLinkAttributes;
	
	OOAreaElement() {
		this.commonLinkAttributes = new OODisplayableLinkAttributes();
	}

	@Override
	HTMLElement getType() {
		return HTMLElement.AREA;
	}

	@Override
	public String getHRef() {
		return commonLinkAttributes.getHRef();
	}

	@Override
	public void setHRef(String href) {
		commonLinkAttributes.setHRef(href);
	}

	@Override
	public String getHRefLang() {
		return commonLinkAttributes.getHRefLang();
	}

	@Override
	public void setHRefLang(String hrefLang) {
		commonLinkAttributes.setHRefLang(hrefLang);
	}

	@Override
	public String getMedia() {
		return commonLinkAttributes.getMedia();
	}

	@Override
	public void setMedia(String media) {
		commonLinkAttributes.setMedia(media);
	}

	@Override
	public String getMediaType() {
		return commonLinkAttributes.getMediaType();
	}

	@Override
	public void setMediaType(String type) {
		commonLinkAttributes.setMediaType(type);
	}

	@Override
	public LinkRelType getRel() {
		return commonLinkAttributes.getRel();
	}

	@Override
	public void setRel(LinkRelType rel) {
		commonLinkAttributes.setRel(rel);
	}

	@Override
	public String getDownload() {
		return commonLinkAttributes.getDownload();
	}

	@Override
	public void setDownload(String download) {
		commonLinkAttributes.setDownload(download);
	}

	@Override
	public Target getTarget() {
		return commonLinkAttributes.getTarget();
	}

	@Override
	public void setTarget(Target target) {
		commonLinkAttributes.setTarget(target);
	}

	@Override
	public String getTargetFrame() {
		return commonLinkAttributes.getTargetFrame();
	}

	@Override
	public void setTargetFrame(String targetFrame) {
		commonLinkAttributes.setTargetFrame(targetFrame);
	}
}

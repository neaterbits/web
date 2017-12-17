package com.test.web.document.html.oo;

import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.ICommonLinkAttributes;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.document.html.common.enums.LinkRevType;
import com.test.web.types.IEnum;
import com.test.web.types.StringUtils;

class OOCommonLinkAttributes implements ICommonLinkAttributes {
	private String href;
	private String hrefLang;
	private String media;
	private String mediaType;
	private LinkRelType rel;
	private LinkRevType rev;
	
	
	@Override
	public final String getHRef() {
		return href;
	}

	@Override
	public final void setHRef(String href) {
		this.href = href;
	}

	@Override
	public final String getHRefLang() {
		return hrefLang;
	}

	@Override
	public final void setHRefLang(String hrefLang) {
		this.hrefLang = hrefLang;
	}

	@Override
	public final String getMedia() {
		return media;
	}

	@Override
	public final void setMedia(String media) {
		this.media = media;
	}
	
	@Override
	public final String getMediaType() {
		return mediaType;
	}

	@Override
	public final void setMediaType(String type) {
		this.mediaType = type;
	}

	@Override
	public final LinkRelType getRel() {
		return rel;
	}

	@Override
	public final void setRel(LinkRelType rel) {
		this.rel = rel;
	}

	@Override
	public LinkRevType getRev() {
		return rev;
	}

	@Override
	public void setRev(LinkRevType rev) {
		this.rev = rev;
	}

	String getAttributeValue(HTMLAttribute attribute) {
		
		final String value;
		
		switch (attribute) {
		case HREF:
			value = href;
			break;
			
		case HREFLANG:
			value = hrefLang;
			break;
			
		case MEDIA:
			value = media;
			break;
			
		case TYPE:
			value = mediaType;
			break;
			
		case REL:
			value = rel.getName();
			break;
			
		case REV:
			value = rev.getName();
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute);
		}

		return value;
	}

	boolean setAttributeValue(HTMLAttribute attribute, String value) {
		final String trimmed = StringUtils.trimToNull(value);
		
		boolean wasSet = trimmed != null;

		switch (attribute) {
		case HREF:
			this.href = trimmed;
			break;
			
		case HREFLANG:
			this.hrefLang = trimmed;
			break;
			
		case MEDIA:
			this.media = trimmed;
			break;
			
		case TYPE:
			this.mediaType = trimmed;
			break;
			
		case REL:
			this.rel = IEnum.asEnum(LinkRelType.class, trimmed, true);
			break;
			
		case REV:
			this.rev = IEnum.asEnum(LinkRevType.class, trimmed, true);
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute);
		}
		
		return wasSet;
	}
}

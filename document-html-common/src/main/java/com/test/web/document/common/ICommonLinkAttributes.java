package com.test.web.document.common;

import com.test.web.document.common.enums.LinkRelType;
import com.test.web.document.common.enums.LinkRevType;

public interface ICommonLinkAttributes {
	String getHRef();
	void setHRef(String href);

	String getHRefLang();
	void setHRefLang(String hrefLang);

	String getMedia();
	void setMedia(String media);

	String getMediaType();
	void setMediaType(String type);

	LinkRelType getRel();
	void setRel(LinkRelType rel);

	LinkRevType getRev();
	void setRev(LinkRevType rev);
}

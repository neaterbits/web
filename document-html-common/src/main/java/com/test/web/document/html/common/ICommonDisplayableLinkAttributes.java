package com.test.web.document.html.common;

import com.test.web.document.html.common.enums.HTMLTarget;

// attributes common to <a> and <area>
public interface ICommonDisplayableLinkAttributes {

	String getDownload();
	void setDownload(String download);

	HTMLTarget getTarget();
	void setTarget(HTMLTarget target, String targetFrame);

	String getTargetFrame();
}

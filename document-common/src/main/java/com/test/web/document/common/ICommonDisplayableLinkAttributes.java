package com.test.web.document.common;

import com.test.web.document.common.enums.Target;

// attributes common to <a> and <area>
public interface ICommonDisplayableLinkAttributes {

	String getDownload();
	void setDownload(String download);

	Target getTarget();
	void setTarget(Target target);

	String getTargetFrame();
	void setTargetFrame(String targetFrame);
}

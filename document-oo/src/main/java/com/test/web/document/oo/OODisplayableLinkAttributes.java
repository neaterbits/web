package com.test.web.document.oo;

import com.test.web.document.common.ICommonDisplayableLinkAttributes;
import com.test.web.document.common.enums.Target;

// <a> and <area> are displayable links that can delegate to this class
// cannot inherit since <a> is container while <area> is leaf element
final class OODisplayableLinkAttributes extends OOCommonLinkAttributes
	implements ICommonDisplayableLinkAttributes {

	private String download; // download filename
	
	private Target target; // either this
	private String targetFrame; // ... or a target frame
	
	@Override
	public final String getDownload() {
		return download;
	}

	@Override
	public final void setDownload(String download) {
		this.download = download;
	}

	@Override
	public final Target getTarget() {
		return target;
	}

	@Override
	public final void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public final String getTargetFrame() {
		return targetFrame;
	}

	@Override
	public final void setTargetFrame(String targetFrame) {
		this.targetFrame = targetFrame;
	}
}

package com.test.web.document.oo;

import com.test.web.document.common.enums.Target;

// <a> and <area> are displayable links
abstract class OODisplayableHRefElement extends OOHRefElement {

	private String download; // download filename
	
	private Target target; // either this
	private String targetFrame; // ... or a target frame
	
	final String getDownload() {
		return download;
	}

	final void setDownload(String download) {
		this.download = download;
	}

	final Target getTarget() {
		return target;
	}

	final void setTarget(Target target) {
		this.target = target;
	}

	final String getTargetFrame() {
		return targetFrame;
	}

	final void setTargetFrame(String targetFrame) {
		this.targetFrame = targetFrame;
	}
}

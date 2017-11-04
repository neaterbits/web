package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;

final class OOScript extends OOContainerElement {

	private String scriptType;
	
	String getScriptType() {
		return scriptType;
	}

	void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	@Override
	HTMLElement getType() {
		return HTMLElement.SCRIPT;
	}
}

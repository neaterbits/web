package com.test.web.document.html.oo;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;

final class OOScript extends OOContainerElement {

	private String scriptType;
	
	String getScriptType() {
		return scriptType;
	}

	void setScriptType(String scriptType) {
		this.scriptType = setOrClearAttribute(HTMLAttribute.TYPE, scriptType);
	}

	@Override
	HTMLElement getType() {
		return HTMLElement.SCRIPT;
	}

	@Override
	String getStandardAttributeValue(HTMLAttribute attribute) {
		final String value;
		
		switch (attribute) {
		case TYPE:
			value = scriptType;
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
		case TYPE:
			setScriptType(value);
			break;
			
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}

}

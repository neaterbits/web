package com.test.web.document.oo;

import com.test.web.document.common.HTMLElement;
import com.test.web.types.DecimalSize;

class OOProgressElement extends OOLeafElement {
	private int max;
	private int value;
	
	OOProgressElement() {
		this.max = DecimalSize.NONE;
		this.value = DecimalSize.NONE;
	}
	
	@Override
	HTMLElement getType() {
		return HTMLElement.PROGRESS;
	}

	int getMax() {
		return max;
	}

	void setMax(int max) {
		this.max = max;
	}

	int getValue() {
		return value;
	}

	void setValue(int value) {
		this.value = value;
	}
}

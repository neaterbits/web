package com.test.web.document.html.oo;

import java.math.BigDecimal;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLStringConversion;
import com.test.web.types.BigDecimalConversion;
import com.test.web.types.DecimalSize;

class OOProgressElement extends OOLeafElement {
	private BigDecimal max;
	private BigDecimal value;
	
	OOProgressElement() {
		this.max = null;
		this.value = null;
	}
	
	@Override
	HTMLElement getType() {
		return HTMLElement.PROGRESS;
	}

	BigDecimal getMax() {
		return max;
	}

	void setMax(BigDecimal max) {
		this.max = setOrClearAttribute(HTMLAttribute.MAX, max);
	}

	BigDecimal getValue() {
		return value;
	}

	void setValue(BigDecimal value) {
		this.value = setOrClearAttribute(HTMLAttribute.VALUE, value);
	}

	@Override
	String getStandardAttributeValue(HTMLAttribute attribute) {
		final String value;
		
		switch (attribute) {
		case MAX:
			value = max != null ? HTMLStringConversion.fromBigDecimal(max) : null;
			break;
			
		case VALUE:
			value = this.value != null ? HTMLStringConversion.fromBigDecimal(this.value) : null;
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
		case MAX:
			setMax(value != null ? BigDecimalConversion.fromString(value) : null);
			break;
			
		case VALUE:
			setValue(value != null ? BigDecimalConversion.fromString(value) : null);
			break;
		
		default:
			super.setStandardAttributeValue(attribute, value, state);
			break;
		}
	}
}

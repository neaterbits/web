package com.test.web.jsapi.dom;

import java.util.function.Function;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLAttributeValueType;

final class AttributeTestValues {
	private final HTMLAttributeValueType valueType;
	private final Function<HTMLAttribute, String> initial;
	private final Function<HTMLAttribute, String> update;
	private final Function<HTMLAttribute, String> erroneous;

	AttributeTestValues(HTMLAttributeValueType valueType, String initial, String update, String erroneous) {
		this(valueType, a -> initial, a -> update, a-> erroneous);
	}
	
	AttributeTestValues(
			HTMLAttributeValueType valueType,
			Function<HTMLAttribute, String> initial,
			Function<HTMLAttribute, String> update,
			Function<HTMLAttribute, String> erroneous) {
		this.valueType = valueType;
		this.initial = initial;
		this.update = update;
		this.erroneous = erroneous;
	}
	
	HTMLAttributeValueType getValueType() {
		return valueType;
	}

	String getInitial(HTMLAttribute attribute) {
		return initial.apply(attribute);
	}

	String getUpdate(HTMLAttribute attribute) {
		return update.apply(attribute);
	}

	String getErroneous(HTMLAttribute attribute) {
		return erroneous.apply(attribute);
	}
}

package com.test.web.jsapi.dom.attributes;

import java.util.function.Consumer;

import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLAttributeValueType;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.testdata.TestData;

class AttributeTestHelper {

	private final AttributeTestValues [] testValues;

	AttributeTestHelper(AttributeTestValues[] testValues) {
		this.testValues = testValues;
	}

	void checkAttribute(HTMLElement element, HTMLAttribute attribute, AttributeTest attributeTest, Consumer<AttributeTestCase> testCaseRunner) {

		// Generate some simple HTML for a DOM
		final String defaultElementId = "element_to_test";
		final String attributeTagValue;
		final String elementId;

		final AttributeTestValues testValues = getAttributeTestValues(attribute);
		final String attributeValue = getAttributeTestValue(attribute, testValues, attributeTest);

		if (attribute == HTMLAttribute.ID) {
			attributeTagValue = makeAttributeTag(element, attribute, null, attributeValue);
			elementId = attributeValue; // if testing the id attribute, then we have to use that value for lookup
		}
		else {
			attributeTagValue = makeAttributeTag(element, attribute, defaultElementId, attributeValue);
			elementId = defaultElementId;
		}

		final boolean runTest;

		String html = TestData.DOCTYPE
				+ "<html>";

		switch (element.getPlacement()) {
		case ROOT:
		case HTML:
			//skip these for now
			runTest = false;
			break;
			
		case HEAD:
		case BOTH:
			// place tag in<head> element
			html += "<head>";
			
			html += attributeTagValue;
			
			html += "</head>";
			runTest = true;
			break;
			
		case BODY:
			// place tag in<body> element
			html += "<body>";
			
			html += attributeTagValue;
			
			html += "</body>";
			runTest = true;
			break;
			
		case MAP:
			// TODO attributes for elements under <map>
			runTest = false;
			break;

		default:
			throw new IllegalStateException("Unknown placement: " + element.getPlacement());
		}
		
		html += "</html>";
		
		if (runTest) {
			testCaseRunner.accept(new AttributeTestCase(element, attribute, html, elementId, attributeValue, testValues));
		}
	}
	
	static String makeAttributeTag(HTMLElement element, HTMLAttribute attribute, String elementId, String attributeValue) {

		String html = "";
		
		html += "<" + element.getName() + " ";

		if (elementId != null) {
			html += "id=" + "\"" + elementId + "\" ";
		}

		html += attribute.getAttributeName() + "=\"" + attributeValue + "\"";
		html += ">";
		html += "</" + element.getName() + ">";

		return html;
	}
	
	// TODO also run with invalid values
	private AttributeTestValues getAttributeTestValues(HTMLAttribute attribute) {

		final AttributeTestValues testValues;
		
		if (attribute == HTMLAttribute.STYLE) {
			testValues = getAttributeTestValue(HTMLAttributeValueType.CSS);
		}
		else {
			testValues = getAttributeTestValue(attribute.getValueType());
		}

		return testValues;
	}
	private AttributeTestValues getAttributeTestValue(HTMLAttributeValueType valueType) {
		AttributeTestValues found = null;

		for (AttributeTestValues value : testValues) {
			if (value.getValueType() == valueType) {
				found = value;
				break;
			}
		}

		if (found == null) {
			throw new IllegalStateException("No test values for type " + valueType);
		}

		return found;
	}
	
	private static String getAttributeTestValue(HTMLAttribute attribute, AttributeTestValues testValues, AttributeTest test) {
		final String value;
		
		switch (test) {
		case INITIAL:
			value = testValues.getInitial(attribute);
			break;

		case UPDATE:
			value = testValues.getUpdate(attribute);
			break;
			
		case ERRONEOUS:
			value = testValues.getErroneous(attribute);
			break;

		default:
			throw new IllegalArgumentException("Unknown test " + test);
		}
		
		return value;
	}
	
}

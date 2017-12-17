package com.test.web.jsapi.dom;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLAttributeValueType;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.oo.OOHTMLDocument;
import com.test.web.document.oo.OOTagElement;
import com.test.web.document.test.DocumentParser;
import com.test.web.jsengine.common.IJSEngine;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.parse.common.ParserException;
import com.test.web.types.IEnum;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

public abstract class BaseDOMTest extends TestCase {

	abstract IJSEngine getJSEngine();

	private static final AttributeTestValues [] testValues = new AttributeTestValues [] {
			new AttributeTestValues(HTMLAttributeValueType.STRING, 							"a_string", 		 	"another_string", 	null),
			new AttributeTestValues(HTMLAttributeValueType.STRING_ARRAY,	 			"value1 value2", 	"value3 value4", 	null),
			new AttributeTestValues(HTMLAttributeValueType.BOOLEAN_TRUE_FALSE,	 "true", 				 	"false", 					null),
			new AttributeTestValues(HTMLAttributeValueType.BOOLEAN_MINIMIZABLE,	 a -> a.getName(),  a -> "false", 			a -> null),
			new AttributeTestValues(HTMLAttributeValueType.BIGDECIMAL, 					"2732839.34984", 	"32987245.23442", "abcde"),
			new AttributeTestValues(HTMLAttributeValueType.DECIMAL, 						"239.4", 		 		"4352.43", 			"fghij"),
			new AttributeTestValues(HTMLAttributeValueType.YES_NO, 							"yes", 		 			"no", 					"someothertext"),
			new AttributeTestValues(HTMLAttributeValueType.INTEGER, 						"123", 		 			"456", 					"notaninteger"),
			new AttributeTestValues(HTMLAttributeValueType.ENUM, 								a -> getEnumName(a, 0), a -> getEnumName(a, 1), a -> "notanenumconstant"),
			new AttributeTestValues(HTMLAttributeValueType.ENUM_OR_STRING, 			a -> getEnumName(a, 0), a -> "a_string_value", a -> null),
			new AttributeTestValues(HTMLAttributeValueType.CSS, 								"width:100px;height:50px",  "float:left;text-align:center",  "notvalidcss"),
	};

	private static String getEnumName(HTMLAttribute attribute, int ordinal) {
		if (attribute.getEnumType() == null) {
			throw new IllegalStateException("No enum type for " + attribute);
		}

		final Enum<?> [] enumConstants = attribute.getEnumType().getEnumConstants();
		
		if (enumConstants == null) {
			throw new IllegalStateException("No enum constants for " + attribute.getEnumType().getSimpleName());
		}
	
		return ((IEnum)enumConstants[ordinal]).getName();
	}

	public void testAttributeSetting() throws ParserException, JSCompileException, JSExecutionException {

		final AttributeTestHelper testHelper = new AttributeTestHelper(testValues);
		final IJSEngine jsEngine = getJSEngine();
	
		// TODO init from JS
		iterateAllAttributesOfAllElements(testHelper, AttributeTest.INITIAL, atc -> checkAttributeInitial(jsEngine, atc));
	}
	
	private void iterateAllAttributesOfAllElements(AttributeTestHelper testHelper, AttributeTest attributeTest, Consumer<AttributeTestCase> testCaseRunner) {
		// Loop through all elements and their attributes
		for (HTMLElement element : HTMLElement.values()) {
			
			// Global attributes
			for (HTMLAttribute attribute : HTMLAttribute.getGlobalAttributes()) {
				testHelper.checkAttribute(element, attribute, attributeTest, testCaseRunner);
			}
			
			// Element specific attributes
			for (HTMLAttribute attribute : element.getAttributes()) {
				testHelper.checkAttribute(element, attribute, attributeTest, testCaseRunner);
			}
		}
	}

	private void checkAttributeInitial(IJSEngine jsEngine, AttributeTestCase atc) {
		try {
			checkAttributeValueEx(jsEngine, atc);
		} catch (ParserException | JSCompileException | JSExecutionException ex) {
			throw new IllegalStateException("Exception while executing", ex);
		}
	}

	private void checkAttributeValueEx(IJSEngine jsEngine, AttributeTestCase atc) throws ParserException, JSCompileException, JSExecutionException {

		System.out.println("checkAttribute:\n" + atc.getHtml());
		
		final JSVariableMap varMap = prepareVarMap(atc.getHtml());

		final int numAttributes;
		final int attrIdx;
		
		final HTMLAttribute attribute = atc.getAttribute();
		final String elementId = atc.getElementId();
		
		if (attribute == HTMLAttribute.ID) {
			numAttributes = 1;
			attrIdx = 0;
		}
		else {
			numAttributes = 2;
			attrIdx = 1;
		}
		
		checkAttributesLength(jsEngine, varMap, elementId, numAttributes);
		checkGetAttributeByIdx(jsEngine, varMap, elementId, attrIdx, atc, atc.getAttributeValue());
		checkGetAttributeByName(jsEngine, varMap, elementId, atc, atc.getAttributeValue());
		checkGetAttributeByNSAndLocalName(jsEngine, varMap, elementId, atc, atc.getAttributeValue());
		
		checkUpdateByAssignment(jsEngine, varMap, elementId, atc, attrIdx, numAttributes);
	}
	
	private void checkUpdateByAssignment(IJSEngine jsEngine, JSVariableMap varMap, String elementId, AttributeTestCase atc, int attrIdx, int numAttributes) throws JSCompileException, JSExecutionException {
		final String updatedValue = atc.getAttributeTestValues().getUpdate(atc.getAttribute());
		final String updatedElementId;

		final HTMLAttribute attribute = atc.getAttribute();

		if (attribute == HTMLAttribute.ID) {

			jsEngine.evalJS("document.getElementById(\"" + elementId + "\").attributes.item(" + attrIdx + ").value = \"" + updatedValue + "\"", varMap);

			// should have removed for the old ID
			final Object elementForInitialId = jsEngine.evalJS("document.getElementById(\"" + elementId + "\")", varMap);
			assertThat(elementForInitialId).isNull();
			
			updatedElementId = updatedValue;
		}
		else {
			updatedElementId = elementId;
		}

		jsEngine.evalJS("document.getElementById(\"" + updatedElementId + "\").attributes.item(" + attrIdx + ").value = \"" + updatedValue + "\"", varMap);

		checkAttributesLength(jsEngine, varMap, updatedElementId, numAttributes);
		checkGetAttributeByIdx(jsEngine, varMap, updatedElementId, attrIdx, atc, updatedValue);
	}
	
	private void checkAttributesLength(IJSEngine jsEngine, JSVariableMap varMap, String elementId, int numAttributes) throws JSCompileException, JSExecutionException {

		final Integer attributesLength = (Integer)jsEngine.evalJS("document.getElementById(\"" + elementId + "\").attributes.length", varMap);

		assertThat(attributesLength).isNotNull();
		assertThat(attributesLength).isEqualTo(numAttributes);
	}

	private void checkGetAttributeByIdx(IJSEngine jsEngine, JSVariableMap varMap, String elementId, int attrIdx, AttributeTestCase atc, String attributeValue) throws JSCompileException, JSExecutionException {
		checkGetAttributeByJS(jsEngine, varMap, "document.getElementById(\"" + elementId + "\").attributes.item(" + attrIdx + ")", atc, attributeValue);
	}

	private void checkGetAttributeByName(IJSEngine jsEngine, JSVariableMap varMap, String elementId, AttributeTestCase atc, String attributeValue) throws JSCompileException, JSExecutionException {
		checkGetAttributeByJS(jsEngine, varMap, "document.getElementById(\"" + elementId + "\").attributes.getNamedItem(\"" + atc.getAttribute().getName() + "\")", atc, attributeValue);
	}

	private void checkGetAttributeByNSAndLocalName(IJSEngine jsEngine, JSVariableMap varMap, String elementId, AttributeTestCase atc, String attributeValue) throws JSCompileException, JSExecutionException {
		checkGetAttributeByJS(jsEngine, varMap, "document.getElementById(\"" + elementId + "\").attributes.getNamedItemNS(null, \"" + atc.getAttribute().getName() + "\")", atc, attributeValue);
	}

	private void checkGetAttributeByJS(IJSEngine jsEngine, JSVariableMap varMap, String js, AttributeTestCase atc, String attributeValue) throws JSCompileException, JSExecutionException {
		
		final String attrName 					= (String)jsEngine.evalJS(js + ".name", varMap);
		final String attrNamespaceURI 	= (String)jsEngine.evalJS(js + ".namespaceURI", varMap);
		final String attrPrefix 					= (String)jsEngine.evalJS(js + ".prefix", varMap);
		final String attrLocalName 			= (String)jsEngine.evalJS(js + ".localName", varMap);
		final String attrValue 					= (String)jsEngine.evalJS(js + ".value", varMap);

		final HTMLAttribute attribute = atc.getAttribute();

		assertThat(attrName).isEqualTo(attribute.getAttributeName());
		assertThat(attrNamespaceURI).isEqualTo(attribute.getAttributeNamespaceURI());
		assertThat(attrPrefix).isEqualTo(attribute.getAttributePrefix());
		assertThat(attrLocalName).isEqualTo(attribute.getAttributeLocalName());
		assertThat(attrValue).isEqualTo(attributeValue);
	}
	
	private JSVariableMap prepareVarMap(String html) throws ParserException {
		final CSSContext<OOCSSElement> cssContext = new CSSContext<>();
		
		final OOHTMLDocument document = OOHTMLDocument.parseHTMLDocument(
				html,
				charInput -> DocumentParser.parseCSS(charInput, cssContext, new OOCSSDocument()));

		// Now run some JS tests
		final JSVariableMap varMap = new JSVariableMap();

		final DocumentContext<OOTagElement> documentContext = new DocumentContext<>(document);

		varMap.addReflected("document", new DOMDocument<OOTagElement, DocumentContext<OOTagElement>>(documentContext));

		return varMap;
	}
}


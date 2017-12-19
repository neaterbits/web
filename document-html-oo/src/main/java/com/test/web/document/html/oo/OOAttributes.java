package com.test.web.document.html.oo;

import java.math.BigDecimal;
import java.util.Arrays;

import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLAttributeValueType;
import com.test.web.document.html.common.HTMLStringConversion;

// Attribute handling in separate file from OOTagElement for clarity
abstract class OOAttributes extends OODocumentElement {

	/*
	private HTMLAttribute [] standardAttributes;
	private int numStandardAttributes;
	private List<OOCustomAttribute> customAttributes;
	*/

	// All added attributes
	private OOAttribute [] attributes;
	private int numAttributes; // So can reuse elements and not reallocate all the time

	abstract String getStandardAttributeValue(HTMLAttribute attribute);

	abstract void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state);

	abstract void removeStandardAttribute(HTMLAttribute attribute, DocumentState<OOTagElement> state);
	
	OOAttributes() {
		/*
		this.standardAttributes = new HTMLAttribute[10]; // sufficient in most cases
		this.numStandardAttributes = 0;
		this.customAttributes = null;
		*/
	}
	

	final boolean isAttributeSet(HTMLAttribute attribute) {
		return getAttributeIdx(attribute) >= 0;
	}

	final void clearAttribute(HTMLAttribute attribute) {

		final int attrIdx = getAttributeIdx(attribute);

		if (attrIdx < 0) {
			throw new IllegalStateException("Attribute not set");
		}

		clearAttribute(attrIdx, attribute);
	}

	private void removeAttributeAt(int attrIdx) {
		for (int i = attrIdx + 1; i < numAttributes; ++ i) {
			attributes[i - 1] = attributes[i];
		}

		-- this.numAttributes;
	}
	
	final void clearAttribute(int attrIdx, HTMLAttribute attribute) {
		if (attributes[attrIdx].getStandard() != attribute) {
			throw new IllegalStateException("Not set: " + attribute);
		}

		// Clear the attribute value by removing from list of non-standard attrs
		removeAttributeAt(attrIdx);

		/*
		if (standardAttributes[attrIdx] != attribute) {
			throw new IllegalStateException("Not set: " + attribute);
		}

		// Clear the attribute value by removing from list of non-standard attrs
		for (int i = attrIdx + 1; i < numStandardAttributes; ++ i) {
			standardAttributes[i - 1] = standardAttributes[i];
		}

		-- this.numStandardAttributes;
		*/
	}
	
	final String setOrClearAttribute(HTMLAttribute attribute, String value) {
		
		if (attribute.getValueType() != HTMLAttributeValueType.STRING) {
			throw new IllegalArgumentException("Not a string attribute: " + attribute);
		}
		
		return genericSetOrClearAttribute(attribute, value);
	}

	final boolean setOrClearAttribute(HTMLAttribute attribute, Boolean value) {
		if (     attribute.getValueType() != HTMLAttributeValueType.BOOLEAN_TRUE_FALSE
			 && attribute.getValueType() != HTMLAttributeValueType.YES_NO) {
			throw new IllegalArgumentException("Not a boolean attribute: " + attribute);
		}

		final Boolean ret = genericSetOrClearAttribute(attribute, value);
		
		return ret != null ? ret : HTMLStringConversion.booleanValue(attribute.getDefaultValue());
	}

	final <T extends Enum<T>>T setOrClearAttribute(HTMLAttribute attribute, T value) {
		if (attribute.getValueType() != HTMLAttributeValueType.ENUM) {
			throw new IllegalArgumentException("Not an enum attribute: " + attribute);
		}

		return genericSetOrClearAttribute(attribute, value);
	}

	final Integer setOrClearAttribute(HTMLAttribute attribute, Integer value) {
		if (attribute.getValueType() != HTMLAttributeValueType.INTEGER) {
			throw new IllegalArgumentException("Not an integer attribute: " + attribute);
		}

		return genericSetOrClearAttribute(attribute, value);
	}

	final BigDecimal setOrClearAttribute(HTMLAttribute attribute, BigDecimal value) {
		if (attribute.getValueType() != HTMLAttributeValueType.BIGDECIMAL) {
			throw new IllegalArgumentException("Not a bigdecimal attribute: " + attribute);
		}

		return genericSetOrClearAttribute(attribute, value);
	}

	final <T>T genericSetOrClearAttribute(HTMLAttribute attribute, T value) {
		
		setOrClearAttributeFlag(attribute, value != null);
		
		return value;
	}
	
	final void setOrClearMinimizableAttribute(HTMLAttribute attribute, boolean value) {
		if (attribute.getValueType() != HTMLAttributeValueType.BOOLEAN_MINIMIZABLE) {
			throw new IllegalArgumentException("Not a string attribute: " + attribute);
		}
		
		setOrClearAttributeFlag(attribute, value);
	}
	
	final void setOrClearAttributeFlag(HTMLAttribute attribute, boolean setAttribute)  {
		
		final int attrIdx = getAttributeIdx(attribute);

		if (setAttribute) {
			if (attrIdx < 0) {
				setAttributeFlag(attribute);
			}
		}
		else {
			if (attrIdx >= 0) {
				clearAttribute(attrIdx, attribute);
			}
		}
	}

	final void setAttributeFlagIfNotSet(HTMLAttribute attribute) {
		if (!isAttributeSet(attribute)) {
			setAttributeFlag(attribute);
		}
	}
	
	final void setAttributeFlag(HTMLAttribute attribute) {
		
		if (attribute == null) {
			throw new IllegalArgumentException("attribute == null");
		}

		if (isAttributeSet(attribute)) {
			throw new IllegalStateException("attribute " + attribute + " already set");
		}
		
		/*

		if (numStandardAttributes == standardAttributes.length) {
			this.standardAttributes = Arrays.copyOf(standardAttributes, standardAttributes.length * 2);
		}
		
		standardAttributes[numStandardAttributes ++] = attribute;
		*/
		
		addAttribute(new OOAttribute(attribute));
	}
	
	private void addAttribute(OOAttribute attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("attribute == null");
		}

		if (attributes == null) {
			attributes = new OOAttribute[10];
		}
		else if (numAttributes == attributes.length) {
			attributes = Arrays.copyOf(attributes, attributes.length * 2);
		}

		attributes[numAttributes ++] = attribute;
	}
	
	
	final int getAttributeIdx(HTMLAttribute attribute) {
		
		int idx = -1;
		
		if (attributes != null) {
			for (int i = 0; i < numAttributes; ++ i) {
				final OOAttribute ooAttribute = attributes[i];
				
				if (ooAttribute.getStandard() != null && ooAttribute.getStandard() == attribute) {
					idx = i;
					break;
				}
			}
		}
		
		/*
		for (int i = 0; i < numStandardAttributes; ++ i) {
			if (standardAttributes[i] == attribute) {
				idx = i;
				break;
			}
		}
		*/
		
		return idx;
	}
	
	String getAttributeValue(OOAttribute attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("attribute == null");
		}

		return attribute.getStandard() != null
				? getStandardAttributeValue(attribute.getStandard())
				: attribute.getCustom().getValue();
	}

	final OOAttribute removeAttribute(String name, DocumentState<OOTagElement> state) {
		final int idx = getIdxOfAttributeWithName(name);
		
		return removeAttribute(idx, state);
	}

	final OOAttribute removeAttribute(String namespaceURI, String localName, DocumentState<OOTagElement> state) {
		final int idx = getIdxOfAttributeWithNameNS(namespaceURI, localName);
		
		return removeAttribute(idx, state);
	}
	
	private OOAttribute removeAttribute(int idx, DocumentState<OOTagElement> state) {
		
		final OOAttribute ooAttribute;

		if (idx >= 0) {
			 ooAttribute = attributes[idx];
			
			if (ooAttribute.getStandard() != null) {
				removeStandardAttribute(ooAttribute.getStandard(), state);
			}
			else {
				removeAttributeAt(idx);
			}
			/*
			if (idx < numStandardAttributes) {
				removed = standardAttributes[idx];

				removeStandardAttribute(removed, state);
			}
			else {
				removed = null; // not a standard attribute but remove nonetheless
				customAttributes.remove(idx - numStandardAttributes);
			}
			*/
		}
		else {
			ooAttribute = null;
		}
		
		return ooAttribute;
	}

	
	final int getNumAttributes() {

		return attributes == null ? 0 : numAttributes;

		/*
		int num = numStandardAttributes;
		
		if (customAttributes != null) {
			num += customAttributes.size();
		}
		
		return num;
		*/
	}
	
	final OOAttribute getAttributeWithName(String name) {
		final int idx = getIdxOfAttributeWithName(name);
		
		return idx < 0 ? null : attributes[idx];
	}

	final OOAttribute getAttribute(int idx) {
		return attributes[idx];
	}

	final OOAttribute getAttributeWithNameNS(String namespaceURI, String localName) {
		final int idx = getIdxOfAttributeWithNameNS(namespaceURI, localName);
		
		return idx < 0 ? null : attributes[idx];
	}

	private final int getIdxOfAttributeWithName(String name) {
		int found = -1;
		
		if (attributes != null) {
			for (int i = 0; i < numAttributes; ++ i) {
				if (attributes[i].matches(name)) {
					found = i;
					break;
				}
			}
		}
	
		/*
		int idx;
		
		for (idx = 0; idx < numStandardAttributes; ++ idx) {
			final HTMLAttribute attribute = standardAttributes[idx];
			
			if (name.equals(attribute.getAttributeName())) {
				found = idx;
				break;
			}
		}
		
		if (found == -1 && this.customAttributes != null) {

			final String [] parts = StringUtils.split(name, ':');

			for (OOCustomAttribute custom : customAttributes) {
				if (parts.length == 1 && parts[0].equals(custom.getLocalName())) {
					found = idx;
					break;
				}
				else if (parts.length == 2 && parts[0].equals(custom.getPrefix()) && parts[1].equals(custom.getLocalName())) {
					found = idx;
					break;
				}
				else if (parts.length == 2 && parts[0].equals(custom.getNamespaceURI()) && parts[1].equals(custom.getLocalName())) {
					found = idx;
					break;
				}

				++ idx;
			}
		}
		*/

		return found;
	}

	private int getIdxOfAttributeWithNameNS(String namespaceURI, String localName) {

		int found = -1;
		
		if (attributes != null) {
			for (int i = 0; i < numAttributes; ++ i) {
				if (attributes[i].matches(namespaceURI, localName)) {
					found = i;
					break;
				}
			}
		}
		
		/*
		int idx;

		for (idx = 0; idx < numStandardAttributes; ++ idx) {
			final HTMLAttribute attribute = standardAttributes[idx];
			
			if (    (namespaceURI == null || namespaceURI.equals(attribute.getAttributeNamespaceURI()))
				&& localName.equals(attribute.getAttributeLocalName())) {
				found = idx;
				break;
			}
		}
		
		if (found == -1 && this.customAttributes != null) {
			for (OOCustomAttribute custom : customAttributes) {
				
				if (StringUtils.equals(namespaceURI, custom.getNamespaceURI()) && localName.equals(custom.getLocalName())) {
					found = idx;
					break;
				}
				
				++ idx;
			}
		}
		*/

		return found;
	}

	private final String getAttributeName(int idx) {
		return attributes[idx].getName();
		/*
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getName();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeName();
		}
		
		return name;
		*/
	}

	final String getAttributeLocalName(int idx) {
		return attributes[idx].getLocalName();
		/*
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getLocalName();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeLocalName();
		}
		
		return name;
		*/
	}

	final String getAttributeNamespaceURI(int idx) {
		return attributes[idx].getNamespaceURI();
		/*
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getNamespaceURI();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeNamespaceURI();
		}
		
		return name;
		*/
	}
	

	final String getAttributePrefix(int idx) {
		return attributes[idx].getPrefix();
		/*
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getPrefix();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributePrefix();
		}
		
		return name;
		*/
	}
	
	final String getAttributeValue(int idx) {
		
		final OOAttribute ooAttribute = attributes[idx];
		
		return ooAttribute.getStandard() != null
				? getStandardAttributeValue(ooAttribute.getStandard())
				: ooAttribute.getCustom().getValue();
		
		/*
		final String value;
		
		if (idx >= numStandardAttributes) {
			value = customAttributes.get(idx - numStandardAttributes).getValue();
		}
		else {
			// standard attribute
			value = getStandardAttributeValue(standardAttributes[idx]);
		}
		
		return value;
		*/
	}
	final OOAttribute setAttributeValue(int idx, String value, DocumentState<OOTagElement> state) {

		final OOAttribute ooAttribute = attributes[idx];
		
		setAttributeValue(ooAttribute, value, state);
		
		return ooAttribute;
	}
	
	final OOAttribute setAttributeValue(OOAttribute ooAttribute, String value, DocumentState<OOTagElement> state) {

		if (ooAttribute.getCustom() != null) {
			ooAttribute.getCustom().setValue(value);
		}
		else {
			setStandardAttributeValue(ooAttribute.getStandard(), value, state);
		}
		
		/*
		if (idx >= numStandardAttributes) {
			// custom one
			customAttributes.get(idx).setValue(value);
			
			changed = null;
		}
		else {
			changed = standardAttributes[idx];
			
			setStandardAttributeValue(changed, value, state);
		}
		*/
	
		return ooAttribute;
	}
	
	private OOAttribute addAttribute(String name, String value, DocumentState<OOTagElement> state) {
		//	Standard or custom?
		
		if (getIdxOfAttributeWithName(name) >= 0) {
			throw new IllegalStateException("already added");
		}
		
		HTMLAttribute standard = HTMLAttribute.find(name);

		final OOAttribute attribute;

		if (standard != null) {
			attribute = new OOAttribute(standard);
			
			setStandardAttributeValue(standard, value, state);
		}
		else {
			final OOCustomAttribute custom = OOCustomAttribute.parse(name);
			
			attribute = custom != null ? new OOAttribute(custom) : null;
		}
		
		if (attribute != null) {
			addAttribute(attribute);
		}
		
		return attribute;
	}

	final OOAttribute setAttributeValue(String name, String value, DocumentState<OOTagElement> state) {

		final OOAttribute updated;
		final int idx = getIdxOfAttributeWithName(name);

		if (idx >= 0) {
			updated = setAttributeValue(idx, value, state);
		}
		else {
			updated = addAttribute(name, value, state);
		}
		
		return updated;
	}
	
	final OOAttribute setAttributeValue(String namespaceURI, String localName, String value, DocumentState<OOTagElement> state) {

		final OOAttribute updated;
		final int idx = getIdxOfAttributeWithNameNS(namespaceURI, localName);

		if (idx >= 0) {
			updated = setAttributeValue(idx, value, state);
		}
		else {
			updated = null;
		}
		
		return updated;
	}

}

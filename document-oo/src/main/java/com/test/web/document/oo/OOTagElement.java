package com.test.web.document.oo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.common.DocumentState;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLAttributeValueType;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLStringConversion;
import com.test.web.parse.html.enums.HTMLDirection;
import com.test.web.types.IEnum;
import com.test.web.types.StringUtils;

public abstract class OOTagElement extends OODocumentElement {

	private HTMLAttribute [] standardAttributes;
	private int numStandardAttributes;
	private List<OOCustomAttribute> customAttributes;
	

	// The standard attributes
	private String id;
	private String [] classes;
	
	private boolean contentEditable;
	private String accessKey;
	private String contextMenu;
	private HTMLDirection direction;
	private String lang;
	private String title;
	
	private OOCSSElement styleElement;
	
	abstract HTMLElement getType();
	
	public OOTagElement() {
		this.standardAttributes = new HTMLAttribute[10]; // sufficient in most cases
		this.numStandardAttributes = 0;
		this.customAttributes = null;
	}

	final boolean isAttributeSet(HTMLAttribute attribute) {
		return getAttributeIdx(attribute) >= 0;
	}

	private void clearAttribute(HTMLAttribute attribute) {
		
		final int attrIdx = getAttributeIdx(attribute);
		
		clearAttribute(attrIdx);
	}

	private void clearAttribute(int attrIdx) {
		// Clear the attribute value by removing from list of non-standard attrs
		for (int i = attrIdx + 1; i < numStandardAttributes; ++ i) {
			standardAttributes[i - 1] = standardAttributes[i];
		}
		
		-- this.numStandardAttributes;
	}
	
	final String setOrClearAttribute(HTMLAttribute attribute, String value) {
		
		if (attribute.getValueType() != HTMLAttributeValueType.STRING) {
			throw new IllegalArgumentException("Not a string attribute: " + attribute);
		}
		
		return genericSetOrClearAttribute(attribute, value);
	}

	final boolean setOrClearAttribute(HTMLAttribute attribute, Boolean value) {
		if (attribute.getValueType() != HTMLAttributeValueType.BOOLEAN_TRUE_FALSE) {
			throw new IllegalArgumentException("Not a string attribute: " + attribute);
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

	final BigDecimal setOrClearAttribute(HTMLAttribute attribute, BigDecimal value) {
		if (attribute.getValueType() != HTMLAttributeValueType.BIGDECIMAL) {
			throw new IllegalArgumentException("Not a bigdecimal attribute: " + attribute);
		}

		return genericSetOrClearAttribute(attribute, value);
	}

	private <T>T genericSetOrClearAttribute(HTMLAttribute attribute, T value) {
		
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
				clearAttribute(attrIdx);
			}
		}
	}

	final void setAttributeFlag(HTMLAttribute attribute) {
		
		if (attribute == null) {
			throw new IllegalArgumentException("attribute == null");
		}
		
		if (numStandardAttributes == standardAttributes.length) {
			this.standardAttributes = Arrays.copyOf(standardAttributes, standardAttributes.length * 2);
		}
		
		standardAttributes[numStandardAttributes ++] = attribute;
	}
	
	final String getId() {
		return id;
	}

	final void setId(String id, DocumentState<OOTagElement> state) {
		
		final boolean attributeAlreadySet = isAttributeSet(HTMLAttribute.ID);
		
		final String trimmed = id != null ? id.trim() : null;
		
		if (attributeAlreadySet) {
			// remove current mapping
			state.removeElementById(this.id);
		}
		
		if (id == null || trimmed.isEmpty()) {
			if (attributeAlreadySet) {
				clearAttribute(HTMLAttribute.ID);
			}
		}
		else {
			setAttributeFlag(HTMLAttribute.ID);
			
			this.id = trimmed;
			state.addElement(trimmed, this);
		}
	}

	final String[] getClasses() {
		return classes;
	}

	final void setClasses(String[] classes, DocumentState<OOTagElement> state) {
		
		final boolean attributeAlreadySet = isAttributeSet(HTMLAttribute.CLASS);

		if (attributeAlreadySet) {
			state.removeElementClasses(classes, this);
		}
		
		if (classes == null || classes.length == 0) {
			if (attributeAlreadySet) {
				clearAttribute(HTMLAttribute.CLASS);
			}
		}
		else {
			setAttributeFlag(HTMLAttribute.CLASS);
			
			this.classes = classes;

			for (String cl : classes) {
				state.addElementClass(cl, this);
			}
		}
	}
	
	final void addClass(String classString, DocumentState<OOTagElement> state) {
		setAttributeFlag(HTMLAttribute.CLASS);

		if (classes == null) {
			this.classes = new String [] { classString };
		}
		else {
			this.classes = Arrays.copyOf(classes, classes.length + 1);
			
			this.classes[classes.length] = classString;
		}
		
		state.addElementClass(classString, this);
	}

	final boolean isContentEditable() {
		return contentEditable;
	}

	final void setContentEditable(Boolean contentEditable) {
		this.contentEditable = setOrClearAttribute(HTMLAttribute.CONTENTEDITABLE, contentEditable);
	}

	final String getAccessKey() {
		return accessKey;
	}

	final void setAccessKey(String accessKey) {
		this.accessKey = setOrClearAttribute(HTMLAttribute.CONTENTEDITABLE, accessKey);
	}

	final String getContextMenu() {
		return contextMenu;
	}

	final void setContextMenu(String contextMenu) {
		this.contextMenu = setOrClearAttribute(HTMLAttribute.CONTEXTMENU, contextMenu);
	}

	final HTMLDirection getDirection() {
		return direction;
	}

	final void setDirection(HTMLDirection direction) {
		this.direction = setOrClearAttribute(HTMLAttribute.DIRECTION, direction);
	}

	final String getLang() {
		return lang;
	}

	final void setLang(String lang) {
		this.lang = setOrClearAttribute(HTMLAttribute.LANG, lang);
	}
	
	final String getTitleAttribute() {
		return title;
	}

	final void setTitleAttribute(String title) {
		this.title = setOrClearAttribute(HTMLAttribute.TITLE, title);
	}

	final OOCSSElement getStyleElement() {
		return styleElement;
	}

	final void setStyleElement(OOCSSElement styleElement) {
		this.styleElement = genericSetOrClearAttribute(HTMLAttribute.STYLE, styleElement);
	}
	
	final int getNumAttributes() {
		int num = numStandardAttributes;
		
		if (customAttributes != null) {
			num += customAttributes.size();
		}
		
		return num;
	}
	
	final int getIdxOfAttributeWithName(String namespaceURI, String name) {

		// Match against standard attributes first
		int idx;
		
		int found = -1;
		
		for (idx = 0; idx < numStandardAttributes; ++ idx) {
			final HTMLAttribute attribute = standardAttributes[idx];
			
			if (    (namespaceURI == null || namespaceURI.equals(attribute.getAttributeNamespaceURI()))
				&& name.equals(attribute.getAttributeLocalName())) {
				found = idx;
				break;
			}
		}
		
		if (found == -1 && this.customAttributes != null) {
			for (OOCustomAttribute custom : customAttributes) {
				
				if (StringUtils.equals(namespaceURI, custom.getNamespaceURI()) && name.equals(custom.getLocalName())) {
					found = idx;
					break;
				}
				
				++ idx;
			}
		}

		return idx;
	}

	final String getAttributeName(int idx) {
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getName();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeName();
		}
		
		return name;
	}

	final String getAttributeLocalName(int idx) {
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getLocalName();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeLocalName();
		}
		
		return name;
	}

	final String getAttributeNamespaceURI(int idx) {
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getNamespaceURI();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributeNamespaceURI();
		}
		
		return name;
	}
	

	final String getAttributePrefix(int idx) {
		final String name;
		
		if (idx >= numStandardAttributes) {
			name = customAttributes.get(idx - numStandardAttributes).getPrefix();
		}
		else {
			// standard attribute
			name = standardAttributes[idx].getAttributePrefix();
		}
		
		return name;
	}
	
	final String getAttributeValue(int idx) {
		final String value;
		
		if (idx >= numStandardAttributes) {
			value = customAttributes.get(idx - numStandardAttributes).getValue();
		}
		else {
			// standard attribute
			value = getStandardAttributeValue(standardAttributes[idx]);
		}
		
		return value;
	}
	
	String getStandardAttributeValue(HTMLAttribute attribute) {
		final String value;
		
		switch (attribute) {
		case ID:
			value = id;
			break;
			
		case CLASS:
			if (classes == null || classes.length == 0) {
				value = "";
			}
			else if (classes.length == 1) {
				value = classes[0];
			}
			else {
				final StringBuilder sb = new StringBuilder(100);
				
				for (int i = 0; i < classes.length; ++ i) {
					if (i > 0) {
						sb.append(' ');
					}
					
					sb.append(classes[i]);
				}
				
				value = sb.toString();
			}
			break;
			
		case CONTENTEDITABLE:
			value = HTMLStringConversion.booleanString(contentEditable);
			break;
			
		case ACCESSKEY:
			value = accessKey;
			break;
			
		case CONTEXTMENU:
			value = contextMenu;
			break;
			
		case DIRECTION:
			value = direction.getName();
			break;
			
		case LANG:
			value = lang;
			break;
			
		case TITLE:
			value = title;
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute);
		}
		
		return value;
	}

	final HTMLAttribute setAttributeValue(int idx, String value, DocumentState<OOTagElement> state) {
		
		final HTMLAttribute changed;
		
		if (idx >= numStandardAttributes) {
			// custom one
			customAttributes.get(idx).setValue(value);
			
			changed = null;
		}
		else {
			changed = standardAttributes[idx];
			
			setStandardAttributeValue(changed, value, state);
		}
	
		return changed;
	}
	
	final HTMLAttribute setAttributeValue(String namespaceURI, String name, String value, DocumentState<OOTagElement> state) {

		final HTMLAttribute updated;
		final int idx = getIdxOfAttributeWithName(namespaceURI, name);

		if (idx >= 0) {
			updated = setAttributeValue(idx, value, state);
		}
		else {
			updated = null;
		}
		
		return updated;
	}

	
	void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state) {
		switch (attribute) {
		case ID:
			this.id = value;
			break;
			
		case CLASS:
			removeStandardAttribute(HTMLAttribute.CLASS, state);
			
			// Parse WS separated classes
			ClassAttributeParser.parseClassAttribute(value, classString -> addClass(classString, state));
			break;

		case CONTENTEDITABLE:
			final Boolean b = HTMLStringConversion.booleanValue(value);
			
			if (b != null) {
				setContentEditable(b);
			}
			break;

		case ACCESSKEY:
			setAccessKey(value);
			break;

		case CONTEXTMENU:
			setContextMenu(value);
			break;
			
		case DIRECTION:
			final HTMLDirection directon = IEnum.asEnum(HTMLDirection.class, value, true);
			if (direction != null) {
				setDirection(directon);
			}
			break;
			
		case LANG:
			setLang(value);
			break;
			
		case TITLE:
			setTitleAttribute(title);
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute);
		}
	}
	
	private int getAttributeIdx(HTMLAttribute attribute) {
		
		int idx = -1;
		
		for (int i = 0; i < numStandardAttributes; ++ i) {
			if (standardAttributes[i] == attribute) {
				idx = i;
				break;
			}
		}
		
		return idx;
	}

	final HTMLAttribute removeAttribute(String namespaceURI, String name, DocumentState<OOTagElement> state) {
		final int idx = getIdxOfAttributeWithName(namespaceURI, name);
		
		final HTMLAttribute removed;
		
		if (idx >= 0) {
			if (idx < numStandardAttributes) {
				removed = standardAttributes[idx];

				removeStandardAttribute(removed, state);
			}
			else {
				removed = null; // not a standard attribute but remove nonetheless
				customAttributes.remove(idx - numStandardAttributes);
			}
		}
		else {
			removed = null;
		}
		
		return removed;
	}

	private void removeStandardAttribute(HTMLAttribute attribute, DocumentState<OOTagElement> state) {
		
		final int attrIdx = getAttributeIdx(attribute);

		if (attrIdx < 0) {
			throw new IllegalStateException("removing attribute that was not set");
		}

		if (attribute == HTMLAttribute.ID) {
			final OOTagElement removed = state.removeElementById(this.id);
			
			if (removed != this) {
				throw new IllegalStateException("ID was not in lookup state");
			}
			
			this.id = null;
		}
		else if (attribute== HTMLAttribute.CLASS) {
			if (classes != null) {
				state.removeElementClasses(classes, this);
			}
			
			this.classes = null;
		}
		else {

			// set to null-value, which should clear it
			setAttributeValue(attrIdx, null, state);
		}

		if (isAttributeSet(attribute)) {
			throw new IllegalStateException("attribute " + attribute + " ought to have been cleared");
		}
	}


	@Override
	public String toString() {
		return "OOTagElement [type=" + getType() + ", id=" + id + ", classes=" + Arrays.toString(classes) + ", contentEditable="
				+ contentEditable + ", accessKey=" + accessKey + ", contextMenu=" + contextMenu + ", direction="
				+ direction + ", styleElement=" + styleElement + "]";
	}
}

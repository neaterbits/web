package com.test.web.document.html.oo;

import java.io.IOException;
import java.util.Arrays;

import com.test.web.css.common.enums.CSSRuleType;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.document.common.DocumentState;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLAttributeValueType;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLStringConversion;
import com.test.web.document.html.common.enums.HTMLDirection;
import com.test.web.document.html.common.enums.HTMLDropzone;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.StringCharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.css.CSSToken;
import com.test.web.types.IEnum;

public abstract class OOTagElement extends OOAttributes {

	// The standard attributes
	private String id;
	private String [] classes;
	
	private boolean contentEditable;
	private String accessKey;
	private String contextMenu;
	private HTMLDirection direction;
	private String lang;
	private String title;
	private boolean translate;
	private boolean spellcheck;
	private boolean draggable;
	private HTMLDropzone dropzone;
	private int tabindex;
	private String hidden;
	
	private OOCSSRule styleElement;
	private String style; // store style as plain text as well as parsed CSS
	
	abstract HTMLElement getType();
	
	public OOTagElement() {
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
			if ( ! attributeAlreadySet ) {
				setAttributeFlag(HTMLAttribute.ID);
			}
			
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
		setAttributeFlagIfNotSet(HTMLAttribute.CLASS);

		if (classes == null) {
			this.classes = new String [] { classString };
		}
		else {
			this.classes = Arrays.copyOf(classes, classes.length + 1);
			
			this.classes[classes.length - 1] = classString;
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
		this.accessKey = setOrClearAttribute(HTMLAttribute.ACCESSKEY, accessKey);
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

	final boolean getTranslate() {
		return translate;
	}

	final void setTranslate(Boolean translate) {
		this.translate = setOrClearAttribute(HTMLAttribute.TRANSLATE, translate);
	}

	final boolean getSpellcheck() {
		return spellcheck;
	}

	final void setSpellcheck(Boolean spellcheck) {
		this.spellcheck = setOrClearAttribute(HTMLAttribute.SPELLCHECK, spellcheck);
	}

	final String getHidden() {
		return hidden;
	}

	void setHidden(String value) {
		setAttributeFlagIfNotSet(HTMLAttribute.HIDDEN);
		
		this.hidden = value;
	}

	final boolean getDraggable() {
		return draggable;
	}

	final void setDraggable(Boolean draggable) {
		this.draggable = setOrClearAttribute(HTMLAttribute.DRAGGABLE, draggable);
	}

	final HTMLDropzone getDropzone() {
		return dropzone;
	}

	final void setDropzone(HTMLDropzone dropzone) {
		this.dropzone = setOrClearAttribute(HTMLAttribute.DROPZONE, dropzone);
	}

	final int getTabindex() {
		return tabindex;
	}

	final void setTabindex(Integer tabindex) {
		this.tabindex = setOrClearAttribute(HTMLAttribute.TABINDEX, tabindex);
	}

	final OOCSSRule getStyleElement() {
		return styleElement;
	}
	
	final String getStyle() {
		return style;
	}

	final void setStyle(OOCSSRule styleElement, String style) {
		
		if (styleElement == null && style != null) {
			throw new IllegalArgumentException("styleElement == null && style != null");
		}

		if (styleElement != null && style == null) {
			throw new IllegalArgumentException("styleElement != null && style == null");
		}
		
		this.styleElement = genericSetOrClearAttribute(HTMLAttribute.STYLE, styleElement);
		this.style = style;
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
			
		case TRANSLATE:
			value = HTMLStringConversion.yesNoString(translate);
			break;

		case SPELLCHECK:
			value = HTMLStringConversion.booleanString(spellcheck);
			break;

		case DRAGGABLE:
			value = HTMLStringConversion.booleanString(draggable);
			break;
			
		case DROPZONE:
			value = dropzone.getName();
			break;

		case TABINDEX:
			value = String.valueOf(tabindex);
			break;
			
		case HIDDEN:
			value= minimizableValue(attribute, hidden);
			break;
			
		case STYLE:
			value = style;
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute + " in " + getType());
		}
		
		return value;
	}
	
	final String minimizableValue(HTMLAttribute attribute, String textualValue) {
		if (attribute.getValueType() != HTMLAttributeValueType.BOOLEAN_MINIMIZABLE) {
			throw new IllegalArgumentException("Not minimizable: " + attribute);
		}
		
		// return isAttributeSet(attribute) ? textualValue : null; // attribute.getName() : null;
		return textualValue;
	}

	
	void setStandardAttributeValue(HTMLAttribute attribute, String value, DocumentState<OOTagElement> state) {
		switch (attribute) {
		case ID:
			setId(value, state);
			break;
			
		case CLASS:
			if (isAttributeSet(HTMLAttribute.CLASS)) {
				removeStandardAttribute(HTMLAttribute.CLASS, state);
			}
			
			this.classes = null;
			
			// Parse WS separated classes
			ClassAttributeParser.parseClassAttribute(value, classString -> addClass(classString, state));
			break;

		case CONTENTEDITABLE: {
			final Boolean b = HTMLStringConversion.booleanValue(value);
			
			if (b != null) {
				setContentEditable(b);
			}
			break;
		}

		case ACCESSKEY:
			setAccessKey(value);
			break;

		case CONTEXTMENU:
			setContextMenu(value);
			break;

		case DIRECTION:
			final HTMLDirection direction = IEnum.asEnum(HTMLDirection.class, value, true);
			if (direction != null) {
				setDirection(direction);
			}
			break;
			
		case LANG:
			setLang(value);
			break;
			
		case TITLE:
			setTitleAttribute(value);
			break;
			
		case TRANSLATE: {
			final Boolean b = HTMLStringConversion.yesNoValue(value);
			
			if (b != null) {
				setTranslate(b);
			}
			break;
		}

		case SPELLCHECK: {
			final Boolean b = HTMLStringConversion.booleanValue(value);
			
			if (b != null) {
				setSpellcheck(b);
			}
			break;
		}

		case DRAGGABLE: {
			final Boolean b = HTMLStringConversion.booleanValue(value);
			
			if (b != null) {
				setDraggable(b);
			}
			break;
		}

		case DROPZONE:
			final HTMLDropzone dropzone = IEnum.asEnum(HTMLDropzone.class, value, true);
			if (dropzone != null) {
				setDropzone(dropzone);
			}
			break;

		case TABINDEX: {
			final Integer i = HTMLStringConversion.integerValue(value);
			
			if (i != null) {
				setTabindex(i);
			}
			break;
		}
		
		case HIDDEN:
			setHidden(value);
			break;

		case STYLE:
			// TODO perhaps cache parser
			final String trimmed = value.trim();
			try {
				parseStyle(trimmed);
			} catch (IOException | ParserException ex) {
				throw new IllegalStateException("Failed to parse style", ex);
			}
			break;
			
		default:
			throw new IllegalStateException("Unknown attribute " + attribute);
		}
	}

	private void parseStyle(String value) throws IOException, ParserException {
		final CharInput charInput = new StringCharInput(value);
		
		final OOStyleDocument listener = new OOStyleDocument();
		
		OOCSSRule cssElement = getStyleElement();
		
		if (cssElement == null) {
			cssElement = listener.allocateCurParseElement(CSSRuleType.STYLE);
		}
		else{
			listener.setCurParseElement(cssElement);
		}

		final Lexer<CSSToken, CharInput> lexer = CSSParser.createLexer(charInput);
		final CSSParser<Void> cssParser = new CSSParser<>(charInput, null, listener);
		
		boolean done = false;

		do {
		
			cssParser.parseElement(null);
			
			// this was the last element?
			boolean semiColonRead = false;
			
			do {

				final CSSToken token = lexer.lex(CSSToken.WS, CSSToken.SEMICOLON, CSSToken.EOF);
				
				switch (token) {
				
				case WS:
					// skip
					break;
					
				case SEMICOLON:
					semiColonRead = true;
					break;
					
				case EOF: // end of string probably
					done = true;
					break;
					
				default:
					throw lexer.unexpectedToken();
				}
			} while (!done && !semiColonRead);
		} while (!done);
	
		setStyle(cssElement, value);
	}
	

	final void removeStandardAttribute(HTMLAttribute attribute, DocumentState<OOTagElement> state) {
		
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
			clearAttribute(attrIdx, attribute);
		}
		else if (attribute== HTMLAttribute.CLASS) {
			if (classes != null) {
				state.removeElementClasses(classes, this);
			}
			
			// Keep value since Attr objects may be pointing to it
			//this.classes = null;
			
			clearAttribute(attrIdx, attribute);
		}
		else {

			// set to null-value, which should clear it
			//setAttributeValue(attrIdx, null, state);
			
			// just clear flag since we need to retain value pointed to by Attr
			clearAttribute(attrIdx, attribute);
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

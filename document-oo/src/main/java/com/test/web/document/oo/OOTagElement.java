package com.test.web.document.oo;

import java.util.Arrays;

import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.common.HTMLElement;
import com.test.web.parse.html.enums.HTMLDirection;

public abstract class OOTagElement extends OODocumentElement {

	private String id;
	private String [] classes;
	
	private boolean contentEditable;
	private String accessKey;
	private String contextMenu;
	private HTMLDirection direction;
	
	private OOCSSElement styleElement;
	
	abstract HTMLElement getType();
	
	final String getId() {
		return id;
	}

	final void setId(String id) {
		this.id = id;
	}

	final String[] getClasses() {
		return classes;
	}

	final void setClasses(String[] classes) {
		this.classes = classes;
	}

	final boolean isContentEditable() {
		return contentEditable;
	}

	final void setContentEditable(boolean contentEditable) {
		this.contentEditable = contentEditable;
	}

	final String getAccessKey() {
		return accessKey;
	}

	final void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	final String getContextMenu() {
		return contextMenu;
	}

	final void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}

	final HTMLDirection getDirection() {
		return direction;
	}

	final void setDirection(HTMLDirection direction) {
		this.direction = direction;
	}

	OOCSSElement getStyleElement() {
		return styleElement;
	}

	void setStyleElement(OOCSSElement styleElement) {

		if (styleElement == null) {
			throw new IllegalArgumentException("styleElement == null");
		}
		
		this.styleElement = styleElement;
	}

	@Override
	public String toString() {
		return "OOTagElement [type=" + getType() + ", id=" + id + ", classes=" + Arrays.toString(classes) + ", contentEditable="
				+ contentEditable + ", accessKey=" + accessKey + ", contextMenu=" + contextMenu + ", direction="
				+ direction + ", styleElement=" + styleElement + "]";
	}
	
	
}

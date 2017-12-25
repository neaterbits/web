package com.test.web.jsapi.cssom;

import com.test.web.jsapi.common.dom.INode;
import com.test.web.jsapi.common.dom.IMediaList;

public class StyleSheet {

	private final boolean disabled;
	private final String href;
	private final IMediaList media;
	private final INode ownerNode;
	private final StyleSheet parentStyleSheet;
	
	private final String title;
	private final String type;

	public StyleSheet(boolean disabled, String href, IMediaList media, INode ownerNode, StyleSheet parentStyleSheet,
			String title, String type) {
		this.disabled = disabled;
		this.href = href;
		this.media = media;
		this.ownerNode = ownerNode;
		this.parentStyleSheet = parentStyleSheet;
		this.title = title;
		this.type = type;
	}

	public final boolean isDisabled() {
		return disabled;
	}

	public final String getHref() {
		return href;
	}

	public final IMediaList getMedia() {
		return media;
	}

	public final INode getOwnerNode() {
		return ownerNode;
	}

	public final StyleSheet getParentStyleSheet() {
		return parentStyleSheet;
	}

	public final String getTitle() {
		return title;
	}

	public final String getType() {
		return type;
	}
}

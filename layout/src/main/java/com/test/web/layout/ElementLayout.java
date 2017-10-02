package com.test.web.layout;

import com.test.web.css.common.enums.CSSDisplay;

final class ElementLayout {
	
	private CSSDisplay display;
	private IFont font;
	
	private boolean hasCSSWidth;
	private boolean hasCSSHeight;
	
	private final Dimensions dimensions;
	private final Wrapping margin;
	private final Wrapping padding;
	
	ElementLayout() {
		this.dimensions = new Dimensions();
		this.margin = new Wrapping();
		this.padding = new Wrapping();
	}

	CSSDisplay getDisplay() {
		return display;
	}

	void setDisplay(CSSDisplay display) {
		this.display = display;
	}

	IFont getFont() {
		return font;
	}

	void setFont(IFont font) {
		this.font = font;
	}

	void setHasCSSWidth(boolean hasCSSWidth) {
		this.hasCSSWidth = hasCSSWidth;
	}

	void setHasCSSHeight(boolean hasCSSHeight) {
		this.hasCSSHeight = hasCSSHeight;
	}

	public boolean hasCSSWidth() {
		return hasCSSWidth;
	}

	public boolean hasCSSHeight() {
		return hasCSSHeight;
	}

	public Dimensions getDimensions() {
		return dimensions;
	}

	public Wrapping getMargin() {
		return margin;
	}

	public Wrapping getPadding() {
		return padding;
	}
}

package com.test.web.layout;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.render.common.IFont;

final class ElementLayout {
	
	// display class for this element
	private CSSDisplay display;
	
	// font used to render this element
	private IFont font;
	
	// whether width/height was specified in CSS
	private boolean hasCSSWidth;
	private boolean hasCSSHeight;

	// sum width and height of sub elements
	private int sumWidth;
	private int sumHeight;

	// dimensions/position of the resulting html element, always including margins and padding
	// so might not be same as width/height from CSS since by default padding and margin comes in addition to these
	private final Dimensions dimensions;
	
	// margin in pixels
	private final Wrapping margin;
	
	// padding in pixels
	private final Wrapping padding;
	
	ElementLayout() {
		this.dimensions = new Dimensions();
		this.margin = new Wrapping();
		this.padding = new Wrapping();
		
		this.sumWidth = 0;
		this.sumHeight = 0;
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

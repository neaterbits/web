package com.test.web.css.common;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.types.FontSpec;

/*
 * For collecting all styles that apply to layout.
 *  
 */

public final class CSSLayoutStyles {

	private CSSDisplay display;
	private CSSPosition position;
	private CSSFloat cssFloat;
	
	private FontSpec font;
	
	private int positionLeft;
	private CSSUnit positionLeftUnit;
	private int positionTop;
	private CSSUnit positionTopUnit;
	
	private int width;
	private CSSUnit widthUnit;
	private int height;
	private CSSUnit heightUnit;
	
	private final CSSDimensions margins;
	private final CSSDimensions padding;
	
	private short zIndex;
	
	public CSSLayoutStyles() {
		this.margins = new CSSDimensions();
		this.padding = new CSSDimensions();
	}
	
	void setDisplay(CSSDisplay display) {
		if (display == null) {
			throw new IllegalArgumentException("display == null");
		}
		
		this.display = display;
	}

	void setFont(FontSpec font) {
		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}
		
		this.font = font;
	}
	
	void merge(CSSDisplay display, CSSPosition position, CSSFloat cssFloat,

			FontSpec font,
	
			int positionLeft, CSSUnit positionLeftUnit, int positionTop, CSSUnit positionTopUnit,
			int width, CSSUnit widthUnit, int height , CSSUnit heightUnit,
			short zIndex) {

		if (display != null) {
			this.display = display;
		}
		
		if (position != null) {
			this.position = position;
		}
		
		if (cssFloat != null) {
			this.cssFloat = cssFloat;
		}
		
		if (font != null) {
			this.font = font;
		}
		
		if (positionLeftUnit != null) {
			this.positionLeft = positionLeft;
			this.positionLeftUnit = positionLeftUnit;
		}
		
		if (positionTopUnit != null) {
			this.positionTop = positionTop;
			this.positionTopUnit = positionTopUnit;
		}

		if (widthUnit != null) {
			this.width = width;
			this.widthUnit = widthUnit;
		}

		if (heightUnit != null) {
			this.height = height;
			this.heightUnit = heightUnit;
		}
		
		this.zIndex = zIndex;
	}
	
	public CSSDisplay getDisplay() {
		return display;
	}

	public CSSPosition getPosition() {
		return position;
	}

	public CSSFloat getFloat() {
		return cssFloat;
	}
	
	public int getPositionLeft() {
		return positionLeft;
	}
	
	public CSSUnit getPositionLeftUnit() {
		return positionLeftUnit;
	}

	public int getPositionTop() {
		return positionTop;
	}

	public CSSUnit getPositionTopUnit() {
		return positionTopUnit;
	}

	public int getWidth() {
		return width;
	}

	public CSSUnit getWidthUnit() {
		return widthUnit;
	}
	
	public boolean hasWidth() {
		return getWidthUnit() != null;
	}

	public int getHeight() {
		return height;
	}

	public CSSUnit getHeightUnit() {
		return heightUnit;
	}
	
	public boolean hasHeight() {
		return getHeightUnit() != null;
	}

	public CSSDimensions getPadding() {
		return padding;
	}
	
	public CSSDimensions getMargins() {
		return margins;
	}
	
	public FontSpec getFont() {
		return font;
	}

	public short getZIndex() {
		return zIndex;
	}
}

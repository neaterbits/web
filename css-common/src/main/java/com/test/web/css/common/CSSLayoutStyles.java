package com.test.web.css.common;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSUnit;

/*
 * For collecting all styles that apply to layout.
 *  
 */

public final class CSSLayoutStyles {

	private CSSDisplay display;
	private CSSPosition position;
	private CSSFloat cssFloat;
	
	private int positionLeft;
	private CSSUnit positionLeftUnit;
	private int positionTop;
	private CSSUnit positionTopUnit;
	
	private int width;
	private CSSUnit widthUnit;
	private int height;
	private CSSUnit heightUnit;
	
	private CSSDimensions padding;
	private CSSDimensions margins;
	
	private short zIndex;
	
	void setDisplay(CSSDisplay display) {
		if (display == null) {
			throw new IllegalArgumentException("display == null");
		}
		
		this.display = display;
	}

	void merge(CSSDisplay display, CSSPosition position, CSSFloat cssFloat,
			
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

	public short getZIndex() {
		return zIndex;
	}
}

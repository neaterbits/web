package com.test.web.layout.common;

import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.LayoutFloat;
import com.test.web.layout.common.enums.Position;
import com.test.web.types.DecimalSize;
import com.test.web.types.FontSpec;
import com.test.web.types.layout.Unit;

/*
 * For collecting all styles that apply to layout.
 *  
 */

public final class LayoutStyles implements ILayoutStylesSetters, ILayoutStylesGetters {

	private Display display;
	private Position position;
	private LayoutFloat _float;
	
	private int width;
	private Unit widthUnit;
	private int height;
	private Unit heightUnit;
	
	private final StyleDimensions margins;
	private final StyleDimensions padding;
	
	private short zIndex;

	private int positionLeft;
	private Unit positionLeftUnit;
	private int positionTop;
	private Unit positionTopUnit;

	private FontSpec font;
	
	public LayoutStyles() {
		this.margins = new StyleDimensions();
		this.padding = new StyleDimensions();
	}
	
	public void clear() {
		this.display = null;
		this.position = null;
		this._float = null;
		
		this.width = 0;
		this.widthUnit = null;
		this.height = 0;
		this.heightUnit = null;
		
		margins.clear();
		padding.clear();
		
		
		this.zIndex = 0;
		this.positionLeft = 0;
		this.positionLeftUnit = null;
		this.positionTop = 0;
		this.positionTopUnit = null;
		
		this.font = null;
	}
	
	@Override
	public void setDisplay(Display display) {
		if (display == null) {
			throw new IllegalArgumentException("display == null");
		}
		
		this.display = display;
	}

	@Override
	public void setFont(FontSpec font) {
		if (font == null) {
			throw new IllegalArgumentException("font == null");
		}
		
		this.font = font;
	}
	
	@Override
	public void merge(Display display, Position position, LayoutFloat _float,

			FontSpec font,
	
			int positionLeft, Unit positionLeftUnit, int positionTop, Unit positionTopUnit,
			int width, Unit widthUnit, int height , Unit heightUnit,
			short zIndex) {

		if (display != null) {
			this.display = display;
		}
		
		if (position != null) {
			this.position = position;
		}
		
		if (_float != null) {
			this._float = _float;
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
	
	public Display getDisplay() {
		return display;
	}

	public Position getPosition() {
		return position;
	}

	public LayoutFloat getFloat() {
		return _float;
	}
	
	public int getPositionLeft() {
		return positionLeft;
	}
	
	public Unit getPositionLeftUnit() {
		return positionLeftUnit;
	}

	public int getPositionTop() {
		return positionTop;
	}

	public Unit getPositionTopUnit() {
		return positionTopUnit;
	}

	public int getWidth() {
		return width;
	}

	public Unit getWidthUnit() {
		return widthUnit;
	}
	
	public boolean hasWidth() {
		return getWidthUnit() != null;
	}

	public int getHeight() {
		return height;
	}

	public Unit getHeightUnit() {
		return heightUnit;
	}
	
	public boolean hasHeight() {
		return getHeightUnit() != null;
	}

	@Override
	public StyleDimensions getPadding() {
		return padding;
	}
	
	@Override
	public StyleDimensions getMargins() {
		return margins;
	}
	
	@Override
	public FontSpec getFont() {
		return font;
	}

	@Override
	public short getZIndex() {
		return zIndex;
	}

	@Override
	public String toString() {
		return "[d=" + display + ", p=" + position + ", f=" + _float + ", w="
				+ DecimalSize.decodeToString(width) + ", wu=" + widthUnit + ", h=" + DecimalSize.decodeToString(height) + ", hu=" + heightUnit
				+ ", m=" + margins + ", p=" + padding + ", z=" + zIndex + ", posL="
				+ positionLeft + ", poSLU=" + positionLeftUnit + ", posT=" + positionTop
				+ ", posTU=" + positionTopUnit + ", font=" + font + "]";
	}

}

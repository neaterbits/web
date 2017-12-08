package com.test.web.css.common;

import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.types.ColorAlpha;
import com.test.web.types.ColorRGB;
import com.test.web.types.DecimalSize;

public class CSSGradientColorStop {

	private final int r, g, b, a;
	private CSSColor color;
	
	
	private final int value;
	private final CSSUnit lengthUnit;
	
	public CSSGradientColorStop(int r, int g, int b, int a, int length, CSSUnit unit) {
		this(r, g, b, a, null, length, unit);
	}
	
	public CSSGradientColorStop(CSSColor color, int length, CSSUnit unit) {
		this(ColorRGB.NONE, ColorRGB.NONE, ColorRGB.NONE, ColorAlpha.NONE, color, length, unit);
	}

	private CSSGradientColorStop(int r, int g, int b, int a, CSSColor color, int length, CSSUnit unit) {
		
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.color = color;
		
		this.value = length;
		this.lengthUnit = unit;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getA() {
		return a;
	}

	public CSSColor getColor() {
		return color;
	}
	
	public boolean hasDistance() {
		return lengthUnit != null;
	}

	public boolean distanceIsPercent() {
		return lengthUnit == CSSUnit.PCT;
	}
	
	// as encoded DecimalSize
	public int getDistancePercent() {
		return distanceIsPercent() ? value : DecimalSize.NONE;
	}
	
	public boolean distanceIsLength() {
		return lengthUnit != null && lengthUnit != CSSUnit.PCT;
	}
	
	// as encoded DecimalSize
	public int getDistanceLength() {
		return distanceIsLength() ? value : DecimalSize.NONE;
	}
	
	public CSSUnit getUnit()  {
		return lengthUnit;
	}
}

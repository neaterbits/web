package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSFilter;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.types.ColorAlpha;
import com.test.web.types.ColorRGB;
import com.test.web.types.DecimalSize;

// Separate class since not always in use
final class OOCSSFilter {
	private CSSFilter filter; // NONE, INITIAL, INHERIT
	
	// set to DecimalSize.NONE etc if not in use
	private int blur;
	private int brightness;
	private int contrast;
	private int grayscale;
	
	private int dropShadowH;
	private CSSUnit dropShadowHUnit;
	private int dropShadowV;
	private CSSUnit dropShadowVUnit;
	private int dropShadowBlur; // always in pixels
	private int dropShadowSpread; // always in pixels

	private int dropShadowR;
	private int dropShadowG;
	private int dropShadowB;
	private int dropShadowA;
	private CSSColor dropShadowColor;
	
	private int hueRotate;
	private int invert;
	private int opacity;
	private int saturate;
	private int sepia;
	private String url;
	
	OOCSSFilter() {
		this.blur = -1;
		this.brightness = DecimalSize.NONE;
		this.contrast = DecimalSize.NONE;
		this.grayscale = DecimalSize.NONE;
		
		this.dropShadowH = DecimalSize.NONE;
		this.dropShadowV = DecimalSize.NONE;
		this.dropShadowBlur = -1;
		this.dropShadowSpread = -1;
		
		this.dropShadowR = ColorRGB.NONE;
		this.dropShadowG = ColorRGB.NONE;
		this.dropShadowB = ColorRGB.NONE;
		this.dropShadowA = ColorAlpha.NONE;
		this.dropShadowColor = null;
		
		this.hueRotate = DecimalSize.NONE;
		this.invert = DecimalSize.NONE;
		this.opacity = DecimalSize.NONE;
		this.saturate = DecimalSize.NONE;
		this.sepia = DecimalSize.NONE;
		this.url = null;
	}
	
	private OOCSSFilter(OOCSSFilter toCopy) {
	    this.blur = toCopy.blur;
        this.brightness = toCopy.brightness;
        this.contrast = toCopy.contrast;
        this.grayscale = toCopy.grayscale;
        
        this.dropShadowH = toCopy.dropShadowH;
        this.dropShadowV = toCopy.dropShadowV;
        this.dropShadowBlur = toCopy.dropShadowBlur;
        this.dropShadowSpread = toCopy.dropShadowSpread;
        
        this.dropShadowR = toCopy.dropShadowR;
        this.dropShadowG = toCopy.dropShadowG;
        this.dropShadowB = toCopy.dropShadowB;
        this.dropShadowA = toCopy.dropShadowA;
        this.dropShadowColor = toCopy.dropShadowColor;
        
        this.hueRotate = toCopy.hueRotate;
        this.invert = toCopy.invert;
        this.opacity = toCopy.opacity;
        this.saturate = toCopy.saturate;
        this.sepia = toCopy.sepia;
        this.url = toCopy.url;
	}
	
	
	OOCSSFilter makeCopy() {
	    return new OOCSSFilter(this);
	}

	CSSFilter getFilter() {
		return filter;
	}

	void setFilter(CSSFilter filter) {
		this.filter = filter;
	}

	int getBlur() {
		return blur;
	}

	void setBlur(int blur) {
		this.blur = blur;
	}

	int getBrightness() {
		return brightness;
	}

	void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	int getContrast() {
		return contrast;
	}

	void setContrast(int contrast) {
		this.contrast = contrast;
	}

	int getGrayscale() {
		return grayscale;
	}

	void setGrayscale(int grayscale) {
		this.grayscale = grayscale;
	}
	
	boolean hasDropShadow() {
		return   dropShadowH != DecimalSize.NONE && dropShadowHUnit != null
				&& dropShadowV != DecimalSize.NONE && dropShadowVUnit != null;
	}

	int getDropShadowH() {
		return dropShadowH;
	}

	CSSUnit getDropShadowHUnit() {
		return dropShadowHUnit;
	}

	int getDropShadowV() {
		return dropShadowV;
	}

	CSSUnit getDropShadowVUnit() {
		return dropShadowVUnit;
	}

	int getDropShadowBlur() {
		return dropShadowBlur;
	}

	int getDropShadowSpread() {
		return dropShadowSpread;
	}

	int getDropShadowR() {
		return dropShadowR;
	}

	int getDropShadowG() {
		return dropShadowG;
	}

	int getDropShadowB() {
		return dropShadowB;
	}

	int getDropShadowA() {
		return dropShadowA;
	}

	CSSColor getDropShadowColor() {
		return dropShadowColor;
	}
	
	void setDropShadow(
			int dropShadowH,
			CSSUnit dropShadowHUnit,
			int dropShadowV,
			CSSUnit dropShadowVUnit,
			int dropShadowBlur,
			int dropShadowSpread,

			int dropShadowR,
			int dropShadowG,
			int dropShadowB,
			int dropShadowA
			) {
		
		this.dropShadowH = dropShadowH;
		this.dropShadowHUnit = dropShadowHUnit;
		this.dropShadowV = dropShadowV;
		this.dropShadowVUnit = dropShadowVUnit;
		this.dropShadowBlur = dropShadowBlur;
		this.dropShadowSpread = dropShadowSpread;
		
		this.dropShadowR = dropShadowR;
		this.dropShadowG = dropShadowG;
		this.dropShadowB = dropShadowB;
		this.dropShadowA = dropShadowA;
		
		this.dropShadowColor = null;
	}

	void setDropShadow(
			int dropShadowH,
			CSSUnit dropShadowHUnit,
			int dropShadowV,
			CSSUnit dropShadowVUnit,
			int dropShadowBlur,
			int dropShadowSpread,

			CSSColor dropShadowColor
			) {
		
		this.dropShadowH = dropShadowH;
		this.dropShadowHUnit = dropShadowHUnit;
		this.dropShadowV = dropShadowV;
		this.dropShadowVUnit = dropShadowVUnit;
		this.dropShadowBlur = dropShadowBlur;
		this.dropShadowSpread = dropShadowSpread;
		
		this.dropShadowColor = dropShadowColor;
		
		this.dropShadowR = ColorRGB.NONE;
		this.dropShadowG = ColorRGB.NONE;
		this.dropShadowB = ColorRGB.NONE;
		this.dropShadowA = ColorAlpha.NONE;
	}

	int getHueRotate() {
		return hueRotate;
	}

	void setHueRotate(int hueRotate) {
		this.hueRotate = hueRotate;
	}

	int getInvert() {
		return invert;
	}

	void setInvert(int invert) {
		this.invert = invert;
	}

	int getOpacity() {
		return opacity;
	}

	void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	int getSaturate() {
		return saturate;
	}

	void setSaturate(int saturate) {
		this.saturate = saturate;
	}

	int getSepia() {
		return sepia;
	}

	void setSepia(int sepia) {
		this.sepia = sepia;
	}

	String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}
}

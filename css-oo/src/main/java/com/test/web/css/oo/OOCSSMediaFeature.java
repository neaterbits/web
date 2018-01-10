package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSColorGamut;
import com.test.web.css.common.enums.CSSDisplayMode;
import com.test.web.css.common.enums.CSSHover;
import com.test.web.css.common.enums.CSSInvertedColors;
import com.test.web.css.common.enums.CSSLightLevel;
import com.test.web.css.common.enums.CSSMediaFeature;
import com.test.web.css.common.enums.CSSOrientation;
import com.test.web.css.common.enums.CSSOverflowBlock;
import com.test.web.css.common.enums.CSSOverflowInline;
import com.test.web.css.common.enums.CSSPointer;
import com.test.web.css.common.enums.CSSRange;
import com.test.web.css.common.enums.CSSResolutionUnit;
import com.test.web.css.common.enums.CSSScan;
import com.test.web.css.common.enums.CSSScripting;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSSUpdate;
import com.test.web.types.Ratio;

final class OOCSSMediaFeature extends OOCSSMediaQueryOption {

	// Only one feature per object, but Java does not have a union type
	private CSSMediaFeature setFeature;
	
	private int width;
	private CSSUnit widthUnit;
	private CSSRange widthRange;

	private int height;
	private CSSUnit heightUnit;
	private CSSRange heightRange;
	
	private Ratio aspectRatio;
	private CSSRange aspectRatioRange;
	
	private CSSOrientation orientation;
	
	private int resolution;
	private CSSResolutionUnit resolutionUnit;
	private CSSRange resolutionRange;
	
	private CSSScan scan;
	
	private boolean grid;
	
	private CSSUpdate update;
	
	private CSSOverflowBlock overflowBlock;
	
	private CSSOverflowInline overflowInline;
	
	private int color;
	private CSSRange colorRange;
	
	private CSSColorGamut colorGamut;

	private int colorIndex;
	private CSSRange colorIndexRange;
	
	private CSSDisplayMode displayMode;
	
	private int monochrome;
	private CSSRange monochromeRange;
	
	private CSSInvertedColors invertedColors;
	
	private CSSPointer pointer;
	
	private CSSHover hover;
	
	private CSSPointer anyPointer;
	
	private CSSHover anyHover;
	
	private CSSLightLevel lightLevel;
	
	private CSSScripting scripting;
	
	private int deviceWidth;
	private CSSUnit deviceWidthUnit;
	private CSSRange deviceWidthRange;

	private int deviceHeight;
	private CSSUnit deviceHeightUnit;
	private CSSRange deviceHeightRange;
	
	private Ratio deviceAspectRatio;
	private CSSRange deviceAspectRatioRange;
	
	private void set(CSSMediaFeature mediaFeature) {
		
		if (mediaFeature == null) {
			throw new IllegalArgumentException("mediaFeature == null");
		}
		
		if (setFeature != null) {
			throw new IllegalStateException("Feature already set");
		}
		
		this.setFeature = mediaFeature;
	}

	int getWidth() {
		return width;
	}

	CSSUnit getWidthUnit() {
		return widthUnit;
	}

	CSSRange getWidthRange() {
		return widthRange;
	}

	void setWidth(int width, CSSUnit widthUnit, CSSRange widthRange) {

		set(CSSMediaFeature.WIDTH);

		this.width = width;
		this.widthUnit = widthUnit;
		this.widthRange = widthRange;
	}

	CSSMediaFeature getMediaFeature() {
		return setFeature;
	}
	
	int getHeight() {
		return height;
	}

	CSSUnit getHeightUnit() {
		return heightUnit;
	}

	CSSRange getHeightRange() {
		return heightRange;
	}

	void setHeight(int height, CSSUnit heightUnit, CSSRange heightRange) {

		set(CSSMediaFeature.HEIGHT);

		this.height = height;
		this.heightUnit = heightUnit;
		this.heightRange = heightRange;
	}

	Ratio getAspectRatio() {
		return aspectRatio;
	}

	CSSRange getAspectRatioRange() {
		return aspectRatioRange;
	}

	void setAspectRatio(Ratio aspectRatio, CSSRange aspectRatioRange) {
		
		set(CSSMediaFeature.ASPECT_RATIO);

		this.aspectRatio = aspectRatio;
		this.aspectRatioRange = aspectRatioRange;
	}

	CSSOrientation getOrientation() {
		return orientation;
	}
	
	void setOrientation(CSSOrientation orientation) {

		set(CSSMediaFeature.ORIENTATION);

		this.orientation = orientation;
	}

	int getResolution() {
		return resolution;
	}

	CSSResolutionUnit getResolutionUnit() {
		return resolutionUnit;
	}

	CSSRange getResolutionRange() {
		return resolutionRange;
	}
	
	void setResolution(int resolution, CSSResolutionUnit resolutionUnit, CSSRange resolutionRange) {
		
		set(CSSMediaFeature.RESOLUTION);
		
		this.resolution = resolution;
		this.resolutionUnit = resolutionUnit;
		this.resolutionRange = resolutionRange;
	}

	CSSScan getScan() {
		return scan;
	}

	void setScan(CSSScan scan) {
		
		set(CSSMediaFeature.SCAN);
		
		this.scan = scan;
	}
	
	boolean isGrid() {
		return grid;
	}
	
	void setGrid(boolean grid) {
		
		set(CSSMediaFeature.GRID);
		
		this.grid = grid;
	}
	
	CSSUpdate getUpdate() {
		return update;
	}

	void setUpdate(CSSUpdate update) {
		
		set(CSSMediaFeature.UPDATE);
		
		this.update = update;
	}

	CSSOverflowBlock getOverflowBlock() {
		return overflowBlock;
	}
	
	void setOverflowBlock(CSSOverflowBlock overflowBlock) {
		
		set(CSSMediaFeature.OVERFLOW_BLOCK);
		
		this.overflowBlock = overflowBlock;
	}

	CSSOverflowInline getOverflowInline() {
		return overflowInline;
	}
	
	void setOverflowInline(CSSOverflowInline overflowInline) {
		
		set(CSSMediaFeature.OVERFLOW_INLINE);
		
		this.overflowInline = overflowInline;
	}

	int getColor() {
		return color;
	}

	CSSRange getColorRange() {
		return colorRange;
	}
	
	void setColor(int color, CSSRange colorRange) {
		
		set(CSSMediaFeature.COLOR);
		
		this.color = color;
		this.colorRange = colorRange;
	}

	CSSColorGamut getColorGamut() {
		return colorGamut;
	}
	
	void setColorGamut(CSSColorGamut colorGamut) {
		set(CSSMediaFeature.COLOR_GAMUT);
		
		this.colorGamut = colorGamut;
	}

	int getColorIndex() {
		return colorIndex;
	}

	CSSRange getColorIndexRange() {
		return colorIndexRange;
	}

	void setColorIndex(int colorIndex, CSSRange colorIndexRange) {
		
		set(CSSMediaFeature.COLOR_INDEX);
		
		this.colorIndex = colorIndex;
		this.colorIndexRange = colorIndexRange;
	}
	
	CSSDisplayMode getDisplayMode() {
		return displayMode;
	}
	
	void setDisplayMode(CSSDisplayMode displayMode) {
		
		set(CSSMediaFeature.DISPLAY_MODE);
		
		this.displayMode = displayMode;
	}

	int getMonochrome() {
		return monochrome;
	}

	CSSRange getMonochromeRange() {
		return monochromeRange;
	}
	
	void setMonochrome(int monochrome, CSSRange monochromeRange) {
		
		set(CSSMediaFeature.MONOCHROME);
		
		this.monochrome = monochrome;
		this.monochromeRange = monochromeRange;
	}

	CSSInvertedColors getInvertedColors() {
		return invertedColors;
	}
	
	void setInvertedColors(CSSInvertedColors invertedColors) {
		
		set(CSSMediaFeature.INVERTED_COLORS);
		
		this.invertedColors = invertedColors;
	}

	CSSPointer getPointer() {
		return pointer;
	}
	
	void setPointer(CSSPointer pointer) {

		set(CSSMediaFeature.POINTER);

		this.pointer = pointer;
	}

	CSSHover getHover() {
		return hover;
	}
	
	void setHover(CSSHover hover) {

		set(CSSMediaFeature.HOVER);
		
		this.hover = hover;
	}

	CSSPointer getAnyPointer() {
		return anyPointer;
	}
	
	void setAnyPointer(CSSPointer anyPointer) {

		set(CSSMediaFeature.ANY_POINTER);

		this.anyPointer = anyPointer;
	}

	CSSHover getAnyHover() {
		return anyHover;
	}
	
	void setAnyHover(CSSHover anyHover) {
		
		set(CSSMediaFeature.ANY_HOVER);
		
		this.anyHover = anyHover;
	}

	CSSLightLevel getLightLevel() {
		return lightLevel;
	}
	
	void setLightLevel(CSSLightLevel lightLevel) {
		
		set(CSSMediaFeature.LIGHT_LEVEL);
		
		this.lightLevel = lightLevel;
	}

	CSSScripting getScripting() {
		return scripting;
	}
	
	void setScripting(CSSScripting scripting) {
		
		set(CSSMediaFeature.SCRIPTING);
		
		this.scripting = scripting;
	}

	int getDeviceWidth() {
		return deviceWidth;
	}
	
	CSSUnit getDeviceWidthUnit() {
		return deviceWidthUnit;
	}

	CSSRange getDeviceWidthRange() {
		return deviceWidthRange;
	}

	void setDeviceWidth(int deviceWidth, CSSUnit deviceWidthUnit, CSSRange deviceWidthRange) {
		
		set(CSSMediaFeature.DEVICE_WIDTH);
		
		this.deviceWidth = deviceWidth;
		this.deviceWidthUnit = deviceWidthUnit;
		this.deviceWidthRange = deviceWidthRange;
	}

	int getDeviceHeight() {
		return deviceHeight;
	}

	CSSUnit getDeviceHeightUnit() {
		return deviceHeightUnit;
	}

	CSSRange getDeviceHeightRange() {
		return deviceHeightRange;
	}
	
	void setDeviceHeight(int deviceHeight, CSSUnit deviceHeightUnit, CSSRange deviceHeightRange) {
		
		set(CSSMediaFeature.DEVICE_HEIGHT);
		
		this.deviceHeight = deviceHeight;
		this.deviceHeightUnit = deviceHeightUnit;
		this.deviceHeightRange = deviceHeightRange;
	}

	Ratio getDevieAspectRatio() {
		return deviceAspectRatio;
	}

	CSSRange getDeviceAspectRatioRange() {
		return deviceAspectRatioRange;
	}
	
	void setDeviceAspectRatio(Ratio deviceAspectRatio, CSSRange deviceAspectRatioRange) {

		set(CSSMediaFeature.DEVICE_ASPECT_RATIO);

		this.deviceAspectRatio = deviceAspectRatio;
		this.deviceAspectRatioRange = deviceAspectRatioRange;
	}
}

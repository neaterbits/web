package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.CSSGradientColorStop;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSGradientDirectionType;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.types.Angle;
import com.test.web.types.ColorRGB;
import com.test.web.types.DecimalSize;

// CSS 3 supports multiple background layers, so we must support that here as well
public class OOBackroundLayer extends OOStylesBase {

	// see https://css-tricks.com/almanac/properties/b/background/
	
	// Image
	private String imageURL; // image
	private CSSBackgroundImage image;
	
	// ... or gradient
	private OOCSSGradient gradient;

	// Position
	private int left;
	private CSSUnit leftUnit;
	private int top;
	private CSSUnit topUnit;
	private CSSBackgroundPosition position;
	
	// Size
	private int width;
	private CSSUnit widthUnit;
	private int height;
	private CSSUnit heightUnit;
	private CSSBackgroundSize size;
	
	// Repeat
	private CSSBackgroundRepeat repeat;
	
	// Attachment
	private CSSBackgroundAttachment attachment;
	
	// Origin
	private CSSBackgroundOrigin origin;

	// Clip
	private CSSBackgroundOrigin clip;

	// Color
	private int bgColorRGB; // bit-shifted, ColorRGB.NONE is not set
	private int bgColorAlpha; // decimal encoded, DecimalSize.NONE if not set
	private CSSColor bgColorCSS; // enumerated color
	private CSSBackgroundColor bgColorType;

	String getImageUrl() {
		return imageURL;
	}
	
	void setImageURL(String imageUrl) {
		if (imageUrl == null) {
			throw new IllegalArgumentException("imageUrl == null");
		}
		
		this.imageURL = imageUrl;
		
		set(CSStyle.BACKGROUND_IMAGE);
	}
	
	CSSBackgroundImage getImage() {
		return image;
	}

	void setImage(CSSBackgroundImage image) {
		if (image == null) {
			throw new IllegalArgumentException("mage == null");
		}
		
		this.image = image;
	}
	
	CSSGradientDirectionType getGradientDirectionType() {
		return gradient == null ? null : gradient.getDirectionType();
	}

	int getGradientAngle() {
		return gradient == null ? Angle.NONE : gradient.getAngle();
	}
	
	CSSPositionComponent getGradientPos1() {
		return gradient == null ? null : gradient.getPos1();
	}
	
	CSSPositionComponent getGradientPos2() {
		return gradient == null ? null : gradient.getPos2();
	}

	CSSGradientColorStop [] getGradientColorStops() {
		return gradient == null ? null : gradient.getColorStops();
	}
	
	void setGradient(int angle, CSSGradientColorStop [] colorStops) {
		setGradient(new OOCSSGradient(angle, colorStops));
	}
	
	void setGradient(CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop [] colorStops) {
		setGradient(new OOCSSGradient(pos1, pos2, colorStops));
	}

	void setGradient(CSSGradientColorStop [] colorStops) {
		setGradient(new OOCSSGradient(colorStops));
	}

	private void setGradient(OOCSSGradient gradient) {
		if (this.gradient != null) {
			throw new IllegalStateException("gradient already set for this layer");
		}
		
		this.gradient = gradient;
	}
	int getPositionLeft() {
		return left;
	}

	CSSUnit getPositionLeftUnit() {
		return leftUnit;
	}
	
	int getPositionTop() {
		return top;
	}

	CSSUnit getPositionTopUnit() {
		return topUnit;
	}
	
	CSSBackgroundPosition getPosition() {
		return position;
	}

	void setPosition(int left, CSSUnit leftUnit, int top, CSSUnit topUnit) {
		if (leftUnit == null) {
			throw new IllegalArgumentException("leftUnit == null");
		}
		
		if (topUnit == null) {
			throw new IllegalArgumentException("topUnit == null");
		}
		
		this.left = left;
		this.leftUnit = leftUnit;
		this.top = top;
		this.topUnit = topUnit;
		this.position = null;
		
		set(CSStyle.BACKGROUND_POSITION);
	}
	
	void setPosition(CSSBackgroundPosition position) {
		
		if (position == null) {
			throw new IllegalArgumentException("position == null");
		}
		
		this.left = DecimalSize.NONE;
		this.leftUnit = null;
		this.top = DecimalSize.NONE;
		this.topUnit = null;
		
		this.position = position;
		
		set(CSStyle.BACKGROUND_POSITION);
	}
	
	int getSizeWidth() {
		return width;
	}

	CSSUnit getSizeWidthUnit() {
		return widthUnit;
	}
	
	int getSizeHeight() {
		return height;
	}

	CSSUnit getSizeHeightUnit() {
		return heightUnit;
	}
	
	CSSBackgroundSize getSize() {
		return size;
	}

	void setSize(int width, CSSUnit widthUnit, int height, CSSUnit heightUnit) {
		if (widthUnit == null) {
			throw new IllegalArgumentException("widthUnit == null");
		}
		
		if (heightUnit == null) {
			throw new IllegalArgumentException("heightUnit == null");
		}
		
		this.width = width;
		this.widthUnit = widthUnit;
		this.height = height;
		this.heightUnit = heightUnit;
		this.size = null;
		
		set(CSStyle.BACKGROUND_SIZE);
	}
	
	void setSize(CSSBackgroundSize size) {
		
		if (size == null) {
			throw new IllegalArgumentException("size == null");
		}
		
		this.width = DecimalSize.NONE;
		this.widthUnit = null;
		this.height = DecimalSize.NONE;
		this.heightUnit = null;
		
		this.size = size;
		
		set(CSStyle.BACKGROUND_SIZE);
	}
	
	CSSBackgroundRepeat getRepeat() {
		return repeat;
	}

	void setRepeat(CSSBackgroundRepeat repeat) {
		if (repeat == null) {
			throw new IllegalArgumentException("repeat == null");
		}
		
		this.repeat = repeat;
		
		set(CSStyle.BACKGROUND_REPEAT);
	}
	
	CSSBackgroundAttachment getAttachment() {
		return attachment;
	}


	void setAttachment(CSSBackgroundAttachment attachment) {

		if (attachment == null) {
			throw new IllegalArgumentException("attachment == null");
		}
		
		this.attachment = attachment;
		
		set(CSStyle.BACKGROUND_ATTACHMENT);
	}

	CSSBackgroundOrigin getOrigin() {
		return origin;
	}

	void setOrigin(CSSBackgroundOrigin origin) {
		if (origin == null) {
			throw new IllegalArgumentException("origin == null");
		}
		
		this.origin = origin;
		
		set(CSStyle.BACKGROUND_ORIGIN);
	}
	
	CSSBackgroundOrigin getClip() {
		return clip;
	}

	void setClip(CSSBackgroundOrigin clip) {
		if (clip == null) {
			throw new IllegalArgumentException("clip == null");
		}
		
		this.clip = clip;
		
		set(CSStyle.BACKGROUND_CLIP);
	}

	void setBgColorRGB(int r, int g, int b, int a) {
		this.bgColorRGB = r << 16 | g << 8 | b;
		this.bgColorAlpha = a;
		this.bgColorCSS = null;
		this.bgColorType = null;

		set(CSStyle.BACKGROUND_COLOR);
	}

	CSSColor getBgColorCSS() {
		return bgColorCSS;
	}

	void setBgColorCSS(CSSColor cssColor) {
		this.bgColorCSS = cssColor;
		this.bgColorRGB = ColorRGB.NONE;
		this.bgColorAlpha = DecimalSize.NONE;
		this.bgColorType = null;

		set(CSStyle.BACKGROUND_COLOR);
	}
	
	void setBgColorType(CSSBackgroundColor colorType) {
		this.bgColorCSS = null;
		this.bgColorRGB = ColorRGB.NONE;
		this.bgColorAlpha = DecimalSize.NONE;
		this.bgColorType = colorType;

		set(CSStyle.BACKGROUND_COLOR);
	}
	
	int getBgColorR() {
		return getColorR(bgColorRGB, bgColorCSS);
	}
	
	int getBgColorG() {
		return getColorG(bgColorRGB, bgColorCSS);
	}
	
	int getBgColorB() {
		return getColorB(bgColorRGB, bgColorCSS);
	}

	int getBgColorA() {
		return getColorA(bgColorAlpha);
	}
	
	CSSBackgroundColor getBgColorType() {
		return bgColorType;
	}

}

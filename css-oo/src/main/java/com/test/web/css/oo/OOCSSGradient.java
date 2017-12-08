package com.test.web.css.oo;

import com.test.web.css.common.CSSGradientColorStop;
import com.test.web.css.common.enums.CSSGradientDirectionType;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.types.Angle;

class OOCSSGradient {

	// angle in degrees
	private final int angle;
	
	// ... or to a side or corner, if side only pos1 is set
	private final CSSPositionComponent pos1;
	private final CSSPositionComponent pos2;
	
	// a number of color stops, at least 1
	private final CSSGradientColorStop [] colorStops;

	OOCSSGradient(CSSGradientColorStop[] colorStops) {
		this(Angle.NONE, null, null, colorStops);
	}

	OOCSSGradient(CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop[] colorStops) {
		this(Angle.NONE, pos1, pos2, colorStops);
		
		if (pos1 == null) {
			throw new IllegalArgumentException("pos1 == null");
		}
		
		if (pos1 == CSSPositionComponent.CENTER) {
			throw new IllegalArgumentException("pos1 is center");
		}

		if (pos2 != null) {
			if (pos2 == CSSPositionComponent.CENTER) {
				throw new IllegalArgumentException("pos2 is center");
			}
			
			if (pos1 == pos2) {
				throw new IllegalArgumentException("pos1 == pos2");
			}
		}
	}

	OOCSSGradient(int angle, CSSGradientColorStop[] colorStops) {
		this(angle, null, null, colorStops);
	}

	private OOCSSGradient(int angle, CSSPositionComponent pos1, CSSPositionComponent pos2, CSSGradientColorStop[] colorStops) {

		if (colorStops == null) {
			throw new IllegalArgumentException("colorStops == null");
		}
		
		if (colorStops.length == 0) {
			throw new IllegalArgumentException("no colorstops");
		}
		
		this.angle = angle;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.colorStops = colorStops;
	}

	CSSGradientDirectionType getDirectionType() {
		
		final CSSGradientDirectionType ret;
		
		if (pos1 != null) {
			ret = pos2 == null ? CSSGradientDirectionType.SIDE : CSSGradientDirectionType.CORNER;
		}
		else if (angle != Angle.NONE) {
			ret = CSSGradientDirectionType.ANGLE;
		}
		else {
			ret = CSSGradientDirectionType.NONE;
		}
		
		return ret;
	}

	int getAngle() {
		return angle;
	}

	CSSPositionComponent getPos1() {
		return pos1;
	}

	CSSPositionComponent getPos2() {
		return pos2;
	}

	CSSGradientColorStop[] getColorStops() {
		return colorStops;
	}
}

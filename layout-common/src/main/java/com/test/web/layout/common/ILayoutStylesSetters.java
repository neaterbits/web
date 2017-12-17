package com.test.web.layout.common;

import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.LayoutFloat;
import com.test.web.layout.common.enums.Position;
import com.test.web.layout.common.enums.Unit;
import com.test.web.types.FontSpec;

public interface ILayoutStylesSetters {

	void merge(Display display, Position position, LayoutFloat _float,

			FontSpec font,
	
			int positionLeft, Unit positionLeftUnit, int positionTop, Unit positionTopUnit,
			int width, Unit widthUnit, int height , Unit heightUnit,
			short zIndex);
	
	StyleDimensions getPadding();
	
	StyleDimensions getMargins();

	void setDisplay(Display display);
	
	void setFont(FontSpec font);
}

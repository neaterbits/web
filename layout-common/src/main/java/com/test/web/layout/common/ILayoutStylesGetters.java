package com.test.web.layout.common;

import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.LayoutFloat;
import com.test.web.layout.common.enums.Position;
import com.test.web.types.FontSpec;
import com.test.web.types.layout.Unit;

public interface ILayoutStylesGetters {

	Display getDisplay();

	Position getPosition();

	LayoutFloat getFloat();
	
	int getPositionLeft();
	
	Unit getPositionLeftUnit();

	int getPositionTop();

	Unit getPositionTopUnit();

	int getWidth();

	Unit getWidthUnit();
	
	boolean hasWidth();

	int getHeight();

	Unit getHeightUnit();
	
	boolean hasHeight();

	IStyleDimensions getPadding();
	
	IStyleDimensions getMargins();
	
	FontSpec getFont();

	short getZIndex();
}

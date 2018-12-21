package com.test.web.layout.common;

import com.test.web.layout.common.enums.Justify;
import com.test.web.types.layout.Unit;

public interface IStyleDimensions {

	int getTop();

	Unit getTopUnit();

	Justify getTopType();

	int getRight();

	Unit getRightUnit();

	Justify getRightType();

	int getBottom();

	Unit getBottomUnit();

	Justify getBottomType();

	int getLeft();

	Unit getLeftUnit();

	Justify getLeftType();

}

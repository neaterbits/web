package com.test.web.layout.common;

import com.test.web.layout.common.enums.Justify;
import com.test.web.types.layout.Unit;

public final class StyleDimensions implements IStyleDimensions {

	private int top;
	private Unit topUnit;
	private Justify topType;

	private int right;
	private Unit rightUnit;
	private Justify rightType;
	
	private int bottom;
	private Unit bottomUnit;
	private Justify bottomType;
	
	private int left;
	private Unit leftUnit;
	private Justify leftType;
	
	public StyleDimensions() {
		this.leftType = Justify.NONE;
		this.topType = Justify.NONE;
		this.rightType = Justify.NONE;
		this.bottomType = Justify.NONE;
	}
	
	void clear() {
		this.top = 0;
		this.topUnit = null;
		this.topType = Justify.NONE;
		
		this.right = 0;
		this.rightUnit = null;
		this.rightType = Justify.NONE;

		this.bottom = 0;
		this.bottomUnit = null;
		this.bottomType = Justify.NONE;

		this.right = 0;
		this.rightUnit = null;
		this.rightType = Justify.NONE;
	}
	
	public void merge(
			int top, Unit topUnit, Justify topType,
			int right, Unit rightUnit, Justify rightType,
			int bottom, Unit bottomUnit, Justify bottomType,
			int left, Unit leftUnit, Justify leftType) {
		
		if (isSet(topType)) {
			this.top = top;
			this.topUnit = topUnit;
			this.topType = topType;
		}
		
		if (isSet(rightType)) {
			this.right = right;
			this.rightUnit = rightUnit;
			this.rightType = rightType;
		}
		
		if (isSet(bottomType)) {
			this.bottom = bottom;
			this.bottomUnit = bottomUnit;
			this.bottomType = bottomType;
		}
		
		if (isSet(leftType)) {
			this.left = left;
			this.leftUnit = leftUnit;
			this.leftType = leftType;
		}
	}
	
	@Override
	public int getTop() {
		return top;
	}

	@Override
	public Unit getTopUnit() {
		return topUnit;
	}

	@Override
	public Justify getTopType() {
		return topType;
	}

	@Override
	public int getRight() {
		return right;
	}

	@Override
	public Unit getRightUnit() {
		return rightUnit;
	}

	@Override
	public Justify getRightType() {
		return rightType;
	}

	@Override
	public int getBottom() {
		return bottom;
	}

	@Override
	public Unit getBottomUnit() {
		return bottomUnit;
	}

	@Override
	public Justify getBottomType() {
		return bottomType;
	}

	@Override
	public int getLeft() {
		return left;
	}

	@Override
	public Unit getLeftUnit() {
		return leftUnit;
	}

	@Override
	public Justify getLeftType() {
		return leftType;
	}

	private static boolean isSet(Justify type) {
		return type != null && type != Justify.NONE; 
	}

	@Override
	public String toString() {
		return "[" + top + ":" + topUnit + ":" + topType +
				        ", " + right + ":" + rightUnit + ":" + rightType +
				        ", " + bottom + ":" + bottomUnit + ":" + bottomType +
				        ", l" + left + ":" + leftUnit + ":" + leftType + "]";
	}
}

package com.test.web.css.common;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

public class CSSDimensions {

	private int top;
	private CSSUnit topUnit;
	private CSSJustify topType;

	private int right;
	private CSSUnit rightUnit;
	private CSSJustify rightType;
	
	private int bottom;
	private CSSUnit bottomUnit;
	private CSSJustify bottomType;
	
	private int left;
	private CSSUnit leftUnit;
	private CSSJustify leftType;
	
	public CSSDimensions() {
		this.leftType = CSSJustify.NONE;
		this.topType = CSSJustify.NONE;
		this.rightType = CSSJustify.NONE;
		this.bottomType = CSSJustify.NONE;
	}
	
	void merge(
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
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
	
	
	
	public int getTop() {
		return top;
	}

	public CSSUnit getTopUnit() {
		return topUnit;
	}

	public CSSJustify getTopType() {
		return topType;
	}

	public int getRight() {
		return right;
	}

	public CSSUnit getRightUnit() {
		return rightUnit;
	}

	public CSSJustify getRightType() {
		return rightType;
	}

	public int getBottom() {
		return bottom;
	}

	public CSSUnit getBottomUnit() {
		return bottomUnit;
	}

	public CSSJustify getBottomType() {
		return bottomType;
	}

	public int getLeft() {
		return left;
	}

	public CSSUnit getLeftUnit() {
		return leftUnit;
	}

	public CSSJustify getLeftType() {
		return leftType;
	}

	private static boolean isSet(CSSJustify type) {
		return type != null && type != CSSJustify.NONE; 
	}

	@Override
	public String toString() {
		return "CSSDimensions [top=" + top + ", topUnit=" + topUnit + ", topType=" + topType + ", right=" + right
				+ ", rightUnit=" + rightUnit + ", rightType=" + rightType + ", bottom=" + bottom + ", bottomUnit="
				+ bottomUnit + ", bottomType=" + bottomType + ", left=" + left + ", leftUnit=" + leftUnit
				+ ", leftType=" + leftType + "]";
	}
}

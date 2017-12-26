package com.test.web.css.common;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

public class WrappingHolder implements ICSSJustify<Void> {

	int top;
	CSSUnit topUnit;
	CSSJustify topType;
	
	int right;
	CSSUnit rightUnit;
	CSSJustify rightType;
	
	int bottom;
	CSSUnit bottomUnit;
	CSSJustify bottomType;
	
	int left;
	CSSUnit leftUnit;
	CSSJustify leftType;
	
	public final int getTop() {
		return top;
	}

	public final CSSUnit getTopUnit() {
		return topUnit;
	}

	public final CSSJustify getTopType() {
		return topType;
	}

	public final int getRight() {
		return right;
	}

	public final CSSUnit getRightUnit() {
		return rightUnit;
	}

	public final CSSJustify getRightType() {
		return rightType;
	}

	public final int getBottom() {
		return bottom;
	}

	public final CSSUnit getBottomUnit() {
		return bottomUnit;
	}

	public final CSSJustify getBottomType() {
		return bottomType;
	}

	public final int getLeft() {
		return left;
	}

	public final CSSUnit getLeftUnit() {
		return leftUnit;
	}

	public final CSSJustify getLeftType() {
		return leftType;
	}

	@Override
	public void set(Void param,
			int top, CSSUnit topUnit, CSSJustify topType,
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType) {
		
		this.top = top;
		this.topUnit = topUnit;
		this.topType = topType;
		
		this.right = right;
		this.rightUnit = rightUnit;
		this.rightType = rightType;
		
		this.bottom = bottom;
		this.bottomUnit = bottomUnit;
		this.bottomType = bottomType;

		this.left = left;
		this.leftUnit = leftUnit;
		this.leftType = leftType;
	}

	public final boolean hasTypes(CSSJustify topType, CSSJustify rightType, CSSJustify bottomType, CSSJustify leftType) {
		return this.topType == topType && this.rightType == rightType && this.bottomType == bottomType && this.leftType == leftType;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [top=" + top + ", topUnit=" + topUnit + ", topType=" + topType + ", right=" + right
				+ ", rightUnit=" + rightUnit + ", rightType=" + rightType + ", bottom=" + bottom + ", bottomUnit="
				+ bottomUnit + ", bottomType=" + bottomType + ", left=" + left + ", leftUnit=" + leftUnit
				+ ", leftType=" + leftType + "]";
	}

}

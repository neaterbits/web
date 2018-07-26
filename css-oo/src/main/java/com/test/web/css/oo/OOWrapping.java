package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

final class OOWrapping {

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

	public OOWrapping() {
		topType = rightType = bottomType = leftType = CSSJustify.NONE;
	}
	
	OOWrapping(OOWrapping toCopy) {
	    this.top = toCopy.top;
	    this.topUnit = toCopy.topUnit;
	    this.topType = toCopy.topType;
	    
	    this.right = toCopy.right;
	    this.rightUnit = toCopy.rightUnit;
	    this.rightType = toCopy.rightType;
	    
	    this.bottom = toCopy.bottom;
	    this.bottomUnit = toCopy.bottomUnit;
	    this.bottomType = toCopy.bottomType;
	    
	    this.left = toCopy.left;
	    this.leftUnit = toCopy.leftUnit;
	    this.leftType = toCopy.leftType;
	}
	
	OOWrapping makeCopy() {
	    return new OOWrapping(this);
	}
	
	int getTop() {
		return top;
	}

	void setTop(int top) {
		this.top = top;
	}

	CSSUnit getTopUnit() {
		return topUnit;
	}

	void setTopUnit(CSSUnit topUnit) {
		this.topUnit = topUnit;
	}

	CSSJustify getTopType() {
		return topType;
	}

	void setTopType(CSSJustify topType) {
		this.topType = topType;
	}

	int getRight() {
		return right;
	}

	void setRight(int right) {
		this.right = right;
	}

	CSSUnit getRightUnit() {
		return rightUnit;
	}

	void setRightUnit(CSSUnit rightUnit) {
		this.rightUnit = rightUnit;
	}

	CSSJustify getRightType() {
		return rightType;
	}

	void setRightType(CSSJustify rightType) {
		this.rightType = rightType;
	}

	int getBottom() {
		return bottom;
	}

	void setBottom(int bottom) {
		this.bottom = bottom;
	}

	CSSUnit getBottomUnit() {
		return bottomUnit;
	}

	void setBottomUnit(CSSUnit bottomUnit) {
		this.bottomUnit = bottomUnit;
	}

	CSSJustify getBottomType() {
		return bottomType;
	}

	void setBottomType(CSSJustify bottomType) {
		this.bottomType = bottomType;
	}

	int getLeft() {
		return left;
	}

	void setLeft(int left) {
		this.left = left;
	}

	CSSUnit getLeftUnit() {
		return leftUnit;
	}

	void setLeftUnit(CSSUnit leftUnit) {
		this.leftUnit = leftUnit;
	}

	CSSJustify getLeftType() {
		return leftType;
	}

	void setLeftType(CSSJustify leftType) {
		this.leftType = leftType;
	}

	@Override
	public String toString() {
		return "OOWrapping [top=" + top + ", topUnit=" + topUnit + ", topType=" + topType + ", right=" + right
				+ ", rightUnit=" + rightUnit + ", rightType=" + rightType + ", bottom=" + bottom + ", bottomUnit="
				+ bottomUnit + ", bottomType=" + bottomType + ", left=" + left + ", leftUnit=" + leftUnit
				+ ", leftType=" + leftType + "]";
	}
}

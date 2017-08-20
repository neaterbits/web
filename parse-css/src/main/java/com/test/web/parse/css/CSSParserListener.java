package com.test.web.parse.css;

import com.test.web.css.common.CSSDisplay;
import com.test.web.css.common.CSSFloat;
import com.test.web.css.common.CSSTarget;
import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.Justify;
import com.test.web.io.common.Tokenizer;

public interface CSSParserListener<TOKENIZER extends Tokenizer, CONTEXT> {

	CONTEXT onEntityStart(CSSTarget entity, String id);
	
	void onEntityEnd(CONTEXT context);
	
	void onWidth(CONTEXT context, int width, CSSUnit unit);
	
	void onHeight(CONTEXT context, int width, CSSUnit unit);
	
	void onBackgroundColor(CONTEXT context, int r, int g, int b);

	void onTextAlign(CONTEXT context, CSSTextAlign textAlign);

	void onDisplay(CONTEXT context, CSSDisplay display);

	void onPosition(CONTEXT context, CSSPosition position);

	void onFloat(CONTEXT context, CSSFloat _float);
	
	void onOverflow(CONTEXT context, CSSOverflow overflow);

	void onMargin(CONTEXT context,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType,
			int left, CSSUnit leftUnit, Justify leftType);

	default void onMarginRight(CONTEXT context, int right, CSSUnit rightUnit, Justify rightType) {
		onMargin(context, 
				0, null, Justify.NONE,
				right, rightUnit, rightType,
				0, null, Justify.NONE,
				0, null, Justify.NONE);
	}

	default void onMarginTop(CONTEXT context, int top, CSSUnit topUnit, Justify topType) {
		onMargin(context, 
				top, topUnit, topType,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				0, null, Justify.NONE);
	}

	default void onMarginBottom(CONTEXT context, int bottom, CSSUnit bottomUnit, Justify bottomType) {
		onMargin(context, 
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				bottom, bottomUnit, bottomType,
				0, null, Justify.NONE);
	}

	default void onMarginLeft(CONTEXT context, int left, CSSUnit leftUnit, Justify leftType) {
		onMargin(context,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				left, leftUnit, leftType);
	}

	void onPadding(CONTEXT context,
			int top, CSSUnit topUnit, Justify topType, 
			int right, CSSUnit rightUnit, Justify rightType,
			int bottom, CSSUnit bottomUnit, Justify bottomType,
			int left, CSSUnit leftUnit, Justify leftType);

	default void onPaddingTop(CONTEXT context, int top, CSSUnit topUnit, Justify topType) {
		onPadding(context, 
				top, topUnit, topType,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				0, null, Justify.NONE);
	}

	default void onPaddingRight(CONTEXT context, int right, CSSUnit rightUnit, Justify rightType) {
		onPadding(context, 
				0, null, Justify.NONE,
				right, rightUnit, rightType,
				0, null, Justify.NONE,
				0, null, Justify.NONE);
	}

	default void onPaddingBottom(CONTEXT context, int bottom, CSSUnit bottomUnit, Justify bottomType) {
		onPadding(context, 
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				bottom, bottomUnit, bottomType,
				0, null, Justify.NONE);
	}

	default void onPaddingLeft(CONTEXT context, int left, CSSUnit leftUnit, Justify leftType) {
		onPadding(context,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				0, null, Justify.NONE,
				left, leftUnit, leftType);
	}
}


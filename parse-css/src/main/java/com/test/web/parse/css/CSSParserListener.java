package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.io.common.Tokenizer;

public interface CSSParserListener<TOKENIZER extends Tokenizer, CONTEXT> {

	CONTEXT onEntityStart(CSSTarget entity, String id);
	
	void onEntityEnd(CONTEXT context);
	
	void onLeft(CONTEXT context, int left, CSSUnit unit);
	
	void onTop(CONTEXT context, int top, CSSUnit unit);
	
	void onWidth(CONTEXT context, int width, CSSUnit unit);
	
	void onHeight(CONTEXT context, int width, CSSUnit unit);
	
	void onBackgroundColor(CONTEXT context, int r, int g, int b);

	void onTextAlign(CONTEXT context, CSSTextAlign textAlign);

	void onDisplay(CONTEXT context, CSSDisplay display);

	void onPosition(CONTEXT context, CSSPosition position);

	void onFloat(CONTEXT context, CSSFloat _float);
	
	void onOverflow(CONTEXT context, CSSOverflow overflow);

	void onMargin(CONTEXT context,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType);

	default void onMarginRight(CONTEXT context, int right, CSSUnit rightUnit, CSSJustify rightType) {
		onMargin(context, 
				0, null, CSSJustify.NONE,
				right, rightUnit, rightType,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE);
	}

	default void onMarginTop(CONTEXT context, int top, CSSUnit topUnit, CSSJustify topType) {
		onMargin(context, 
				top, topUnit, topType,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE);
	}

	default void onMarginBottom(CONTEXT context, int bottom, CSSUnit bottomUnit, CSSJustify bottomType) {
		onMargin(context, 
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				bottom, bottomUnit, bottomType,
				0, null, CSSJustify.NONE);
	}

	default void onMarginLeft(CONTEXT context, int left, CSSUnit leftUnit, CSSJustify leftType) {
		onMargin(context,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				left, leftUnit, leftType);
	}

	void onPadding(CONTEXT context,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType);

	default void onPaddingTop(CONTEXT context, int top, CSSUnit topUnit, CSSJustify topType) {
		onPadding(context, 
				top, topUnit, topType,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE);
	}

	default void onPaddingRight(CONTEXT context, int right, CSSUnit rightUnit, CSSJustify rightType) {
		onPadding(context, 
				0, null, CSSJustify.NONE,
				right, rightUnit, rightType,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE);
	}

	default void onPaddingBottom(CONTEXT context, int bottom, CSSUnit bottomUnit, CSSJustify bottomType) {
		onPadding(context, 
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				bottom, bottomUnit, bottomType,
				0, null, CSSJustify.NONE);
	}

	default void onPaddingLeft(CONTEXT context, int left, CSSUnit leftUnit, CSSJustify leftType) {
		onPadding(context,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				0, null, CSSJustify.NONE,
				left, leftUnit, leftType);
	}
}


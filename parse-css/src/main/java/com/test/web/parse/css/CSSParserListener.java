package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.io.common.Tokenizer;

public interface CSSParserListener<TOKENIZER extends Tokenizer, CONTEXT> {

	CONTEXT onBlockStart();
	
	void onEntityMap(CONTEXT context, CSSTarget entity, String id);
	
	void onBlockEnd(CONTEXT context);
	
	void onLeft(CONTEXT context, int left, CSSUnit unit);
	
	void onTop(CONTEXT context, int top, CSSUnit unit);
	
	void onWidth(CONTEXT context, int width, CSSUnit unit);
	
	void onHeight(CONTEXT context, int width, CSSUnit unit);

	void onColor(CONTEXT context, int r, int g, int b, int a); // -1 for a if no value, otherwise decimal encoded

	void onColor(CONTEXT context, CSSColor color);

	void onColor(CONTEXT context, CSSForeground foreground);
	
	void onBgImageURL(CONTEXT context, int bgLayer, String url);

	void onBgImage(CONTEXT context, int bgLayer, CSSBackgroundImage image);

	void onBgPosition(CONTEXT context, int bgLayer, int left, CSSUnit leftUnit, int top, CSSUnit topUnit);

	void onBgPosition(CONTEXT context, int bgLayer, CSSBackgroundPosition position);

	void onBgSize(CONTEXT context, int bgLayer, int width, CSSUnit widthUnit, int height, CSSUnit heightUnit);

	void onBgSize(CONTEXT context, int bgLayer, CSSBackgroundSize size);
	
	void onBgRepeat(CONTEXT context, int bgLayer, CSSBackgroundRepeat repeat);

	void onBgAttachment(CONTEXT context, int bgLayer, CSSBackgroundAttachment attachment);

	void onBgOrigin(CONTEXT context, int bgLayer, CSSBackgroundOrigin origin);
	
	void onBgClip(CONTEXT context, int bgLayer, CSSBackgroundOrigin clip);

	void onBgColor(CONTEXT context, int r, int g, int b, int a); // -1 for a if no value, otherwise decimal encoded

	void onBgColor(CONTEXT context, CSSColor color);

	void onBgColor(CONTEXT context, CSSBackgroundColor background);

	void onTextAlign(CONTEXT context, CSSTextAlign textAlign);

	void onDisplay(CONTEXT context, CSSDisplay display);

	void onPosition(CONTEXT context, CSSPosition position);

	void onFloat(CONTEXT context, CSSFloat _float);
	
	void onOverflow(CONTEXT context, CSSOverflow overflow);

	void onTextDecoration(CONTEXT context, CSSTextDecoration textDecoration);
	
	void onMaxWidth(CONTEXT context, int width, CSSUnit unit, CSSMax type);
	
	void onMaxHeight(CONTEXT context, int height, CSSUnit unit, CSSMax type);
	
	void onMinWidth(CONTEXT context, int width, CSSUnit unit, CSSMin type);
	
	void onMinHeight(CONTEXT context, int height, CSSUnit unit, CSSMin type);
	
	void onFontSize(CONTEXT context, int fontSize, CSSUnit fontSizeUnit, CSSFontSize fontSizeEnum);
	
	void onFontWeight(CONTEXT context, int fontWeightNumber, CSSFontWeight fontWeightEnum);

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


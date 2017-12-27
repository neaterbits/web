package com.test.web.css.common;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSPriority;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.types.ColorAlpha;
import com.test.web.types.DecimalSize;

public class CSSStyleBuilder {

	public static <RULE> String getCSSText(ICSSDocument<RULE> styles, RULE rule) {
		
		final StringBuilder sb = new StringBuilder();
		
		final int numStyles = styles.getStyleLength(rule);
		
		WrappingHolder wrappingHolder = null;
		
		for (int i = 0; i < numStyles; ++ i) {
			
			if (i > 0) {
				sb.append(' ');
			}
			
			final CSStyle style = styles.getStyleType(rule, i);

			if (style != null) {
				// known style
				sb.append(style.getName());
				sb.append(": ");
				wrappingHolder = getPropertyValue(styles, rule, style, sb, wrappingHolder);
			}
			else {
				// custom style
				sb.append(styles.getStyleCustomPropertyName(rule, i));
				sb.append(": ");
				sb.append(styles.getStyleCustomPropertyValue(rule, i));
			}
			
			final String priority = styles.getStylePropertyPriority(rule, i);
			if (priority != null) {
				sb.append(" !").append(priority);
			}

			sb.append(';');
		}

		return sb.toString();
	}

	public static <RULE> String getCSSPropertyValue(ICSSDocument<RULE> styles, RULE rule, CSStyle style) {

		final StringBuilder sb = new StringBuilder();
		
		getPropertyValue(styles, rule, style, sb, null);
		
		return sb.toString();
	}


	// format output
	private static <RULE> WrappingHolder getPropertyValue(ICSSDocumentStyles<RULE> styles, RULE rule, CSStyle style, StringBuilder sb, WrappingHolder wrappingHolder) {
		
		switch (style) {
		case LEFT:
			appendDim(sb, styles.getLeft(rule), styles.getLeftUnit(rule));
			break;
			
		case TOP:
			appendDim(sb, styles.getTop(rule), styles.getTopUnit(rule));
			break;
			
		case CLEAR:
			sb.append(styles.getClear(rule).getName());
			break;
			
		case FLOAT:
			sb.append(styles.getFloat(rule).getName());
			break;
			
		case COLOR:
			appendColor(sb, styles.getColorR(rule), styles.getColorG(rule), styles.getColorB(rule), styles.getColorA(rule));
			break;
			
		case DISPLAY:
			sb.append(styles.getDisplay(null).getName());
			break;
			
		case FONT_FAMILY:
			sb.append(styles.getFontFamily(rule));
			break;
			
		case FONT_NAME:
			sb.append(styles.getFontName(rule));
			break;
			
		case FONT_SIZE:
			sb.append(styles.getFontSize(rule));
			break;
			
		case FONT_WEIGHT:
			if (styles.getFontWeightEnum(rule) != null) {
				sb.append(styles.getFontSizeEnum(rule).getName());
			}
			else {
				sb.append(styles.getFontWeightNumber(rule));
			}
			break;
			
		case WIDTH:
			appendDim(sb, styles.getWidth(rule), styles.getWidthUnit(rule));
			break;
			
		case HEIGHT:
			appendDim(sb, styles.getHeight(rule), styles.getHeightUnit(rule));
			break;
			
		case MARGIN_TOP:
			wrappingHolder = margins(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getTop(), wrappingHolder.getTopUnit(), wrappingHolder.getTopType());
			break;
		
		case MARGIN_RIGHT:
			wrappingHolder = margins(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getRight(), wrappingHolder.getRightUnit(), wrappingHolder.getRightType());
			break;
			
		case MARGIN_BOTTOM:
			wrappingHolder = margins(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getBottom(), wrappingHolder.getBottomUnit(), wrappingHolder.getBottomType());
			break;

		case MARGIN_LEFT:
			wrappingHolder = margins(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getLeft(), wrappingHolder.getLeftUnit(), wrappingHolder.getLeftType());
			break;
			
		case MARGIN:
			appendWrapping(sb, wrappingHolder);
			break;
			
		case PADDING_TOP:
			wrappingHolder = padding(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getTop(), wrappingHolder.getTopUnit(), wrappingHolder.getTopType());
			break;
		
		case PADDING_RIGHT:
			wrappingHolder = padding(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getRight(), wrappingHolder.getRightUnit(), wrappingHolder.getRightType());
			break;
			
		case PADDING_BOTTOM:
			wrappingHolder = padding(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getBottom(), wrappingHolder.getBottomUnit(), wrappingHolder.getBottomType());
			break;

		case PADDING_LEFT:
			wrappingHolder = padding(styles, rule, wrappingHolder);
			appendWrapping(sb, wrappingHolder.getLeft(), wrappingHolder.getLeftUnit(), wrappingHolder.getLeftType());
			break;
			
		case PADDING:
			appendWrapping(sb, wrappingHolder);
			break;

		case MIN_WIDTH:
			appendMin(sb, styles.getMinWidth(rule), styles.getMinWidthUnit(rule), styles.getMinWidthType(rule));
			break;
			
		case MAX_WIDTH:
			appendMax(sb, styles.getMaxWidth(rule), styles.getMaxWidthUnit(rule), styles.getMaxWidthType(rule));
			break;
			
		case MIN_HEIGHT:
			appendMin(sb, styles.getMinHeight(rule), styles.getMinHeightUnit(rule), styles.getMinHeightType(rule));
			break;
			
		case MAX_HEIGHT:
			appendMax(sb, styles.getMaxHeight(rule), styles.getMaxHeightUnit(rule), styles.getMaxHeightType(rule));
			break;
			
		case OVERFLOW:
			sb.append(styles.getOverflow(rule).getName());
			break;
			
		case TEXT_ALIGN:
			sb.append(styles.getTextAlign(rule).getName());
			break;
			
		case TEXT_DECORATION:
			sb.append(styles.getTextDecoration(rule).getName());
			break;

		case POSITION:
			sb.append(styles.getPosition(rule).getName());
			break;
			
		case Z_INDEX:
			sb.append(styles.getZIndex(rule));
			break;
			
		case BACKGROUND_COLOR:
			appendColor(sb, styles.getBgColorR(rule), styles.getBgColorG(rule), styles.getBgColorB(rule), styles.getBgColorA(rule));
			break;

		default:
			throw new UnsupportedOperationException("Unknown CSS style " + style);
		}

		return wrappingHolder;
	}
	
	private  static <RULE> WrappingHolder margins(ICSSDocumentStyles<RULE> styles, RULE rule, WrappingHolder wrappingHolder) {
		
		if (wrappingHolder == null) {
			wrappingHolder = new WrappingHolder();
		}
		
		styles.getMargins(rule, wrappingHolder, null);
		
		return wrappingHolder;
	}

	private  static <RULE> WrappingHolder padding(ICSSDocumentStyles<RULE> styles, RULE rule, WrappingHolder wrappingHolder) {
		
		if (wrappingHolder == null) {
			wrappingHolder = new WrappingHolder();
		}
		
		styles.getPadding(rule, wrappingHolder, null);
		
		return wrappingHolder;
	}
	
	private static void appendDim(StringBuilder sb, int i, CSSUnit unit) {
		appendDecimal(sb, i).append(unit.getName());
	}

	private static void appendWrapping(StringBuilder sb, int i, CSSUnit unit, CSSJustify justify) {
		if (justify == CSSJustify.SIZE) {
			sb.append(i).append(unit.getName());
		}
		else {
			sb.append(justify.getName());
		}
	}

	private static void appendWrapping(StringBuilder sb, WrappingHolder wrapping) {

		final CSSJustify topType 		= wrapping.getTopType();
		final CSSJustify rightType 		= wrapping.getRightType();
		final CSSJustify bottomType 	= wrapping.getBottomType();
		final CSSJustify leftType 		= wrapping.getLeftType();

		if (topType != null && rightType != null && bottomType != null && leftType != null) {

			if (     wrapping.hasTypes(CSSJustify.AUTO, CSSJustify.AUTO, CSSJustify.AUTO, CSSJustify.AUTO)
			      || wrapping.hasTypes(CSSJustify.INITIAL, CSSJustify.INITIAL, CSSJustify.INITIAL, CSSJustify.INITIAL)
			      || wrapping.hasTypes(CSSJustify.INHERIT, CSSJustify.INHERIT, CSSJustify.INHERIT, CSSJustify.INHERIT)) {

				sb.append(topType.getName());
			}
			else if (wrapping.hasTypes(CSSJustify.SIZE, CSSJustify.SIZE, CSSJustify.SIZE, CSSJustify.SIZE)) {
				// 4 size values
				appendDim(sb, wrapping.getTop(), wrapping.getTopUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getRight(), wrapping.getRightUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getBottom(), wrapping.getBottomUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getLeft(), wrapping.getLeftUnit());
			}
			else if (wrapping.hasTypes(CSSJustify.SIZE, CSSJustify.SIZE, CSSJustify.SIZE, CSSJustify.SIZE_COMPACTED)) {
				// 3 size values
				
				if (wrapping.getLeft() != wrapping.getRight() || wrapping.getLeftUnit() != wrapping.getRightUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}

				appendDim(sb, wrapping.getTop(), wrapping.getTopUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getRight(), wrapping.getRightUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getBottom(), wrapping.getBottomUnit());
			}
			else if (wrapping.hasTypes(CSSJustify.SIZE, CSSJustify.SIZE, CSSJustify.SIZE_COMPACTED, CSSJustify.SIZE_COMPACTED)) {
				// 2 size values
				if (wrapping.getTop() != wrapping.getBottom() || wrapping.getTopUnit() != wrapping.getBottomUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}
				
				if (wrapping.getLeft() != wrapping.getRight() || wrapping.getLeftUnit() != wrapping.getRightUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}

				appendDim(sb, wrapping.getTop(), wrapping.getTopUnit());
				sb.append(' ');
				appendDim(sb, wrapping.getRight(), wrapping.getRightUnit());
			}
			else if (wrapping.hasTypes(CSSJustify.SIZE, CSSJustify.SIZE_COMPACTED, CSSJustify.SIZE_COMPACTED, CSSJustify.SIZE_COMPACTED)) {
				if (wrapping.getTop() != wrapping.getRight() || wrapping.getTopUnit() != wrapping.getRightUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}

				if (wrapping.getTop() != wrapping.getBottom() || wrapping.getTopUnit() != wrapping.getBottomUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}
				
				if (wrapping.getTop() != wrapping.getLeft() || wrapping.getTopUnit() != wrapping.getLeftUnit()) {
					throw new IllegalStateException("Margins mismatch: " + wrapping);
				}

				appendDim(sb, wrapping.getTop(), wrapping.getTopUnit());
			}
			else {
				throw new IllegalStateException("Unknown types combination: " + wrapping);
			}
		}
		else {
			throw new IllegalStateException("Not all types set: " + wrapping);
		}
	}

	private static void appendMin(StringBuilder sb, int i, CSSUnit unit, CSSMin min) {
		if (min == CSSMin.SIZE) {
			appendDecimal(sb, i).append(unit.getName());
		}
		else {
			sb.append(min.getName());
		}
	}

	private static void appendMax(StringBuilder sb, int i, CSSUnit unit, CSSMax max) {
		if (max == CSSMax.SIZE) {
			appendDecimal(sb, i).append(unit.getName());
		}
		else {
			sb.append(max.getName());
		}
	}
	
	private static StringBuilder appendDecimal(StringBuilder sb, int i) {
		sb.append(DecimalSize.decodeToString(i));
		
		return sb;
	}

	private static void appendColor(StringBuilder sb, int r, int g, int b, int a) {
		if (a != ColorAlpha.NONE) {
			sb.append("rgba(").append(r).append(", ").append(g).append(", ").append(b).append(", ").append(a).append(")");
		}
		else {
			sb.append("rgb(").append(r).append(", ").append(g).append(", ").append(b).append(")");
		}
	}
}

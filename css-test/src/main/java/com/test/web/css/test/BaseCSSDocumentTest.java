package com.test.web.css.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSBackground;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSTextDecoration;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io._long.StringBuffers;
import com.test.web.io.common.SimpleLoadStream;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.css.ICSSDocumentParserListener;
import com.test.web.testdata.TestData;
import com.test.web.types.DecimalSize;

import junit.framework.TestCase;

public abstract class BaseCSSDocumentTest<ELEMENT, TOKENIZER extends Tokenizer> extends TestCase {
	
	protected abstract ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> createDocument();

	private ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> parse(String css) throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = createDocument();
		
		final StringBuffers buffers = new StringBuffers(new SimpleLoadStream(css));
		
		final CSSParser<TOKENIZER, Void> parser = new CSSParser<>(buffers, doc);
		
		parser.parseCSS();
	
		return doc;
	}
	
	public void testParser() throws IOException, ParserException {
		
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS);
	
		final ELEMENT h1Ref = doc.get(CSSTarget.TAG, "h1").get(0);
		
		assertThat(doc.isSet(CSSTarget.TAG, "h1", CSStyle.HEIGHT)).isTrue();
		assertThat(doc.isSet(CSSTarget.TAG, "h1", CSStyle.WIDTH)).isTrue();
		
		assertThat(doc.isSet(h1Ref, CSStyle.HEIGHT)).isTrue();
		assertThat(doc.isSet(h1Ref, CSStyle.WIDTH)).isTrue();
		
		assertThat(doc.getHeightUnit(h1Ref)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getHeight(h1Ref))).isEqualTo(100);
		
		assertThat(doc.getWidthUnit(h1Ref)).isEqualTo(CSSUnit.PCT);
		assertThat(DecimalSize.decodeToInt(doc.getWidth(h1Ref))).isEqualTo(20);
		
		final ELEMENT idRef = doc.get(CSSTarget.ID, "an_element").get(0);

		assertThat(doc.isSet(idRef, CSStyle.MARGIN_LEFT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.MARGIN_RIGHT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.FLOAT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.POSITION)).isTrue();
		
		final TestCSSJustify margins = new TestCSSJustify();
		
		assertThat(doc.getFloat(idRef)).isEqualTo(CSSFloat.LEFT);
		assertThat(doc.getPosition(idRef)).isEqualTo(CSSPosition.RELATIVE);
		
		doc.getMargins(idRef, margins, null);
		
		System.out.println("Margins: " + margins);
		
		assertThat(DecimalSize.decodeToInt(margins.left)).isEqualTo(10);
		assertThat(margins.leftUnit).isEqualTo(CSSUnit.PX);
		assertThat(margins.leftType).isEqualTo(CSSJustify.SIZE);
		
		assertThat(margins.rightType).isEqualTo(CSSJustify.AUTO);
		assertThat(margins.topType).isEqualTo(CSSJustify.NONE);
		assertThat(margins.bottomType).isEqualTo(CSSJustify.NONE);

		final ELEMENT classRef = doc.get(CSSTarget.CLASS, "a_class").get(0);
		checkThirdBlock(doc, classRef);

		final ELEMENT idRef2 = doc.get(CSSTarget.ID, "an_element").get(1);
		checkThirdBlock(doc, idRef2);
	}
	
	private void checkThirdBlock(ICSSDocument<ELEMENT> doc, ELEMENT ref) {
		assertThat(doc.isSet(ref, CSStyle.POSITION)).isTrue();
		assertThat(doc.isSet(ref, CSStyle.FLOAT)).isTrue();
		assertThat(doc.isSet(ref, CSStyle.PADDING_TOP)).isTrue();

		assertThat(doc.getFloat(ref)).isEqualTo(CSSFloat.RIGHT);
		assertThat(doc.getPosition(ref)).isEqualTo(CSSPosition.ABSOLUTE);
		
		final TestCSSJustify padding = new TestCSSJustify();
		doc.getPadding(ref, padding, null);
		
		System.out.println("Padding: " + padding);

		assertThat(DecimalSize.decodeToInt(padding.top)).isEqualTo(30);
		assertThat(padding.topUnit).isEqualTo(CSSUnit.PX);
		assertThat(padding.topType).isEqualTo(CSSJustify.SIZE);
	}
	
	public void testMargins() throws IOException, ParserException {
		
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_MARGINS);
	
		checkMargin(doc, "margin_auto",
				0, null, CSSJustify.AUTO,
				0, null, CSSJustify.AUTO,
				0, null, CSSJustify.AUTO,
				0, null, CSSJustify.AUTO);
		
		checkMargin(doc, "margin_initial",
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL);

		checkMargin(doc, "margin_inherit",
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT);
				
		checkMargin(doc, "margin_1",
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE);

		checkMargin(doc, "margin_2",
				50, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				50, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE);

		checkMargin(doc, "margin_3",
				45, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE,
				40, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE);

		checkMargin(doc, "margin_4",
				35, CSSUnit.PX, CSSJustify.SIZE,
				15, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE,
				20, CSSUnit.PX, CSSJustify.SIZE);
	}

	public void testPadding() throws IOException, ParserException {
		
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_PADDING);
	
		checkPadding(doc, "padding_initial",
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL,
				0, null, CSSJustify.INITIAL);

		checkPadding(doc, "padding_inherit",
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT,
				0, null, CSSJustify.INHERIT);
				
		checkPadding(doc, "padding_1",
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE);

		checkPadding(doc, "padding_2",
				50, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE,
				50, CSSUnit.PX, CSSJustify.SIZE,
				25, CSSUnit.PX, CSSJustify.SIZE);

		checkPadding(doc, "padding_3",
				45, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE,
				40, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE);

		checkPadding(doc, "padding_4",
				35, CSSUnit.PX, CSSJustify.SIZE,
				15, CSSUnit.PX, CSSJustify.SIZE,
				30, CSSUnit.PX, CSSJustify.SIZE,
				20, CSSUnit.PX, CSSJustify.SIZE);

		final ELEMENT ref = doc.get(CSSTarget.ID, "padding_4_em").get(0);
		final TestCSSJustify padding = new TestCSSJustify();
		doc.getPadding(ref, padding, null);

		checkWrapping(padding,
				DecimalSize.of("0.5"), CSSUnit.EM, CSSJustify.SIZE,
				DecimalSize.of(0), CSSUnit.PX, CSSJustify.SIZE,
				DecimalSize.of(0), CSSUnit.PX, CSSJustify.SIZE,
				DecimalSize.of(0), CSSUnit.PX, CSSJustify.SIZE);
	}

	private void checkMargin(ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc, String id,
			int top, CSSUnit topUnit, CSSJustify topJustify,
			int right, CSSUnit rightUnit, CSSJustify rightJustify,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomJustify,
			int left, CSSUnit leftUnit, CSSJustify leftJustify) {

		final ELEMENT ref = doc.get(CSSTarget.ID, id).get(0);
		
		final TestCSSJustify margins = new TestCSSJustify();

		doc.getMargins(ref, margins, null);
		
		checkWrapping(margins, top, topUnit, topJustify, right, rightUnit, rightJustify, bottom, bottomUnit, bottomJustify, left, leftUnit, leftJustify);
	}

	private void checkPadding(ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc, String id,
			int top, CSSUnit topUnit, CSSJustify topJustify,
			int right, CSSUnit rightUnit, CSSJustify rightJustify,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomJustify,
			int left, CSSUnit leftUnit, CSSJustify leftJustify) {

		final ELEMENT ref = doc.get(CSSTarget.ID, id).get(0);
		
		final TestCSSJustify padding = new TestCSSJustify();

		doc.getPadding(ref, padding, null);
		
		checkWrapping(padding, top, topUnit, topJustify, right, rightUnit, rightJustify, bottom, bottomUnit, bottomJustify, left, leftUnit, leftJustify);
	}

	private void checkWrapping(TestCSSJustify wrapping,
			int top, CSSUnit topUnit, CSSJustify topJustify,
			int right, CSSUnit rightUnit, CSSJustify rightJustify,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomJustify,
			int left, CSSUnit leftUnit, CSSJustify leftJustify) {
		
		assertThat(DecimalSize.decodeToInt(wrapping.top)).isEqualTo(top);
		assertThat(wrapping.topUnit).isEqualTo(topUnit);
		assertThat(wrapping.topType).isEqualTo(topJustify);
		assertThat(DecimalSize.decodeToInt(wrapping.right)).isEqualTo(right);
		assertThat(wrapping.rightUnit).isEqualTo(rightUnit);
		assertThat(wrapping.rightType).isEqualTo(rightJustify);
		assertThat(DecimalSize.decodeToInt(wrapping.bottom)).isEqualTo(bottom);
		assertThat(wrapping.bottomUnit).isEqualTo(bottomUnit);
		assertThat(wrapping.bottomType).isEqualTo(bottomJustify);
		assertThat(DecimalSize.decodeToInt(wrapping.left)).isEqualTo(left);
		assertThat(wrapping.leftUnit).isEqualTo(leftUnit);
		assertThat(wrapping.leftType).isEqualTo(leftJustify);
	}
	
	private void checkWrapping(TestCSSJustify wrapping,
			DecimalSize top, CSSUnit topUnit, CSSJustify topJustify,
			DecimalSize right, CSSUnit rightUnit, CSSJustify rightJustify,
			DecimalSize bottom, CSSUnit bottomUnit, CSSJustify bottomJustify,
			DecimalSize left, CSSUnit leftUnit, CSSJustify leftJustify) {
		
		assertThat(DecimalSize.decode(wrapping.top)).isEqualTo(top);
		assertThat(wrapping.topUnit).isEqualTo(topUnit);
		assertThat(wrapping.topType).isEqualTo(topJustify);
		assertThat(DecimalSize.decode(wrapping.right)).isEqualTo(right);
		assertThat(wrapping.rightUnit).isEqualTo(rightUnit);
		assertThat(wrapping.rightType).isEqualTo(rightJustify);
		assertThat(DecimalSize.decode(wrapping.bottom)).isEqualTo(bottom);
		assertThat(wrapping.bottomUnit).isEqualTo(bottomUnit);
		assertThat(wrapping.bottomType).isEqualTo(bottomJustify);
		assertThat(DecimalSize.decode(wrapping.left)).isEqualTo(left);
		assertThat(wrapping.leftUnit).isEqualTo(leftUnit);
		assertThat(wrapping.leftType).isEqualTo(leftJustify);
	}
	
	public void testFontSize() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_FONTSIZE);

		final ELEMENT medium = doc.get(CSSTarget.ID, "fontsize_medium").get(0);
		
		assertThat(doc.getFontSize(medium)).isEqualTo(0);
		assertThat(doc.getFontSizeUnit(medium)).isEqualTo(null);
		assertThat(doc.getFontSizeEnum(medium)).isEqualTo(CSSFontSize.MEDIUM);
		
		final ELEMENT fs20px = doc.get(CSSTarget.ID, "fontsize_20px").get(0);

		assertThat(DecimalSize.decodeToInt(doc.getFontSize(fs20px))).isEqualTo(20);
		assertThat(doc.getFontSizeUnit(fs20px)).isEqualTo(CSSUnit.PX);
		assertThat(doc.getFontSizeEnum(fs20px)).isEqualTo(null);
	}
	
	public void testFontWeight() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_FONTWEIGHT);

		final ELEMENT normal = doc.get(CSSTarget.ID, "fontweight_normal").get(0);
		
		assertThat(doc.getFontWeightNumber(normal)).isEqualTo(0);
		assertThat(doc.getFontWeightEnum(normal)).isEqualTo(CSSFontWeight.NORMAL);
		
		final ELEMENT fw300 = doc.get(CSSTarget.ID, "fontweight_300").get(0);

		assertThat(doc.getFontWeightNumber(fw300)).isEqualTo(300);
		assertThat(doc.getFontWeightEnum(fw300)).isEqualTo(null);
	}

	public void testColor() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_COLORS);

		final ELEMENT rgba1 = doc.get(CSSTarget.ID, "color_rgba1").get(0);
		
		assertThat(doc.getColorR(rgba1)).isEqualTo(125);
		assertThat(doc.getColorG(rgba1)).isEqualTo(234);
		assertThat(doc.getColorB(rgba1)).isEqualTo(83);
		assertThat(DecimalSize.decodeToInt(doc.getColorA(rgba1))).isEqualTo(1);
		
		final ELEMENT rgba2 = doc.get(CSSTarget.ID, "color_rgba2").get(0);

		assertThat(doc.getColorR(rgba2)).isEqualTo(134);
		assertThat(doc.getColorG(rgba2)).isEqualTo(76);
		assertThat(doc.getColorB(rgba2)).isEqualTo(223);
		
		try {
			DecimalSize.decodeToInt(doc.getColorA(rgba2));
			fail("Expected exception since is a decimla");
		}
		catch (IllegalStateException ex) {
		}
		
		assertThat(DecimalSize.decodeToString(doc.getColorA(rgba2))).isEqualTo("0.1");

		final ELEMENT rgba3 = doc.get(CSSTarget.ID, "color_rgba3").get(0);

		assertThat(doc.getColorR(rgba3)).isEqualTo(63);
		assertThat(doc.getColorG(rgba3)).isEqualTo(89);
		assertThat(doc.getColorB(rgba3)).isEqualTo(23);
		assertThat(DecimalSize.decodeToString(doc.getColorA(rgba3))).isEqualTo("0.015");

		final ELEMENT rgb = doc.get(CSSTarget.ID, "color_rgb").get(0);

		assertThat(doc.getColorR(rgb)).isEqualTo(39);
		assertThat(doc.getColorG(rgb)).isEqualTo(43);
		assertThat(doc.getColorB(rgb)).isEqualTo(24);
		assertThat(doc.getColorA(rgb)).isEqualTo(DecimalSize.NONE);

		final ELEMENT turquoise = doc.get(CSSTarget.ID, "color_turquoise").get(0);

		assertThat(doc.getColorR(turquoise)).isEqualTo(0x40);
		assertThat(doc.getColorG(turquoise)).isEqualTo(0xE0);
		assertThat(doc.getColorB(turquoise)).isEqualTo(0xD0);
		assertThat(doc.getColorA(turquoise)).isEqualTo(DecimalSize.NONE);

		final ELEMENT initial = doc.get(CSSTarget.ID, "color_initial").get(0);
		
		assertThat(doc.getColorType(initial)).isEqualTo(CSSForeground.INITIAL);
	}

	public void testBgColor() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_COLORS);

		final ELEMENT rgba1 = doc.get(CSSTarget.ID, "bgcolor_rgba1").get(0);
		
		assertThat(doc.getBgColorR(rgba1)).isEqualTo(125);
		assertThat(doc.getBgColorG(rgba1)).isEqualTo(234);
		assertThat(doc.getBgColorB(rgba1)).isEqualTo(83);
		assertThat(DecimalSize.decodeToInt(doc.getBgColorA(rgba1))).isEqualTo(1);
		
		final ELEMENT rgba2 = doc.get(CSSTarget.ID, "bgcolor_rgba2").get(0);

		assertThat(doc.getBgColorR(rgba2)).isEqualTo(134);
		assertThat(doc.getBgColorG(rgba2)).isEqualTo(76);
		assertThat(doc.getBgColorB(rgba2)).isEqualTo(223);
		
		try {
			DecimalSize.decodeToInt(doc.getBgColorA(rgba2));
			fail("Expected exception since is a decimal");
		}
		catch (IllegalStateException ex) {
		}
		
		assertThat(DecimalSize.decodeToString(doc.getBgColorA(rgba2))).isEqualTo("0.1");

		final ELEMENT rgba3 = doc.get(CSSTarget.ID, "bgcolor_rgba3").get(0);

		assertThat(doc.getBgColorR(rgba3)).isEqualTo(63);
		assertThat(doc.getBgColorG(rgba3)).isEqualTo(89);
		assertThat(doc.getBgColorB(rgba3)).isEqualTo(23);
		assertThat(DecimalSize.decodeToString(doc.getBgColorA(rgba3))).isEqualTo("0.015");

		final ELEMENT rgb = doc.get(CSSTarget.ID, "bgcolor_rgb").get(0);

		assertThat(doc.getBgColorR(rgb)).isEqualTo(39);
		assertThat(doc.getBgColorG(rgb)).isEqualTo(43);
		assertThat(doc.getBgColorB(rgb)).isEqualTo(24);
		assertThat(doc.getBgColorA(rgb)).isEqualTo(DecimalSize.NONE);

		final ELEMENT turquoise = doc.get(CSSTarget.ID, "bgcolor_turquoise").get(0);

		assertThat(doc.getBgColorR(turquoise)).isEqualTo(0x40);
		assertThat(doc.getBgColorG(turquoise)).isEqualTo(0xE0);
		assertThat(doc.getBgColorB(turquoise)).isEqualTo(0xD0);
		assertThat(doc.getBgColorA(turquoise)).isEqualTo(DecimalSize.NONE);

		final ELEMENT initial = doc.get(CSSTarget.ID, "bgcolor_initial").get(0);
		
		assertThat(doc.getBgColorType(initial)).isEqualTo(CSSBackground.TRANSPARENT);
	}

	public void testTextDecoration() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_TEXT_DECORATION);

		final ELEMENT td1 = doc.get(CSSTarget.ID, "text_decoration_1").get(0);
		
		assertThat(doc.getTextDecoration(td1)).isEqualTo(CSSTextDecoration.UNDERLINE);
	}
	
	
	private static class TestCSSJustify implements ICSSJustify<Void> {

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

		@Override
		public String toString() {
			return "TestCSSJustify [top=" + top + ", topUnit=" + topUnit + ", topType=" + topType + ", right=" + right
					+ ", rightUnit=" + rightUnit + ", rightType=" + rightType + ", bottom=" + bottom + ", bottomUnit="
					+ bottomUnit + ", bottomType=" + bottomType + ", left=" + left + ", leftUnit=" + leftUnit
					+ ", leftType=" + leftType + "]";
		}
	}
}

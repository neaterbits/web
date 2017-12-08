package com.test.web.css.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSClear;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSFilter;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSGradientDirectionType;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSPositionComponent;
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
import com.test.web.types.Angle;
import com.test.web.types.ColorAlpha;
import com.test.web.types.ColorRGB;
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

		checkMargin(doc, "margin_1_0",
				0, CSSUnit.PX, CSSJustify.SIZE,
				0, CSSUnit.PX, CSSJustify.SIZE,
				0, CSSUnit.PX, CSSJustify.SIZE,
				0, CSSUnit.PX, CSSJustify.SIZE);

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

	public void testClear() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_CLEAR);

		final ELEMENT clear = doc.get(CSSTarget.ID, "clear_both").get(0);
		
		assertThat(doc.getClear(clear)).isEqualTo(CSSClear.BOTH);
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
		
		assertThat(doc.getBgColorType(initial)).isEqualTo(CSSBackgroundColor.TRANSPARENT);
	}

	public void testBgImage() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_IMAGE);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgimage").get(0);
		
		assertThat(doc.getBgImageURL(pos, 0)).isEqualTo("http://test.com/image1.png");
		assertThat(doc.getBgImage(pos, 0)).isNull();
	
		assertThat(doc.getBgImageURL(pos, 1)).isNull();
		assertThat(doc.getBgImage(pos, 1)).isEqualTo(CSSBackgroundImage.NONE);
	
		assertThat(doc.getBgImageURL(pos, 2)).isEqualTo("http://test.com/image2.jpg");
		assertThat(doc.getBgImage(pos, 2)).isNull();
	}

	public void testBgPosition() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_POSITION);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgposition").get(0);
		
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionLeft(pos, 0))).isEqualTo(100);
		assertThat(doc.getBgPositionLeftUnit(pos, 0)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionTop(pos, 0))).isEqualTo(15);
		assertThat(doc.getBgPositionTopUnit(pos, 0)).isEqualTo(CSSUnit.PCT);
		assertThat(doc.getBgPosition(pos, 0)).isNull();
	
		assertThat(doc.getBgPositionLeft(pos, 1)).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getBgPositionLeftUnit(pos, 1)).isNull();
		assertThat(doc.getBgPositionTop(pos, 1)).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getBgPositionTopUnit(pos, 1)).isNull();
		assertThat(doc.getBgPosition(pos, 1)).isEqualTo(CSSBackgroundPosition.LEFT_TOP);
	}

	public void testBgSize() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_SIZE);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgsize").get(0);
		
		assertThat(DecimalSize.decodeToInt(doc.getBgWidth(pos, 0))).isEqualTo(250);
		assertThat(doc.getBgWidthUnit(pos, 0)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToString(doc.getBgHeight(pos, 0))).isEqualTo("120.3");
		assertThat(doc.getBgHeightUnit(pos, 0)).isEqualTo(CSSUnit.EM);
		assertThat(doc.getBgSize(pos, 0)).isNull();
	
		assertThat(doc.getBgWidth(pos, 1)).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getBgWidthUnit(pos, 1)).isNull();
		assertThat(doc.getBgHeight(pos, 1)).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getBgHeightUnit(pos, 1)).isNull();
		assertThat(doc.getBgSize(pos, 1)).isEqualTo(CSSBackgroundSize.AUTO);
	}

	public void testBgRepeat() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_REPEAT);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgrepeat").get(0);
		
		assertThat(doc.getBgRepeat(pos, 0)).isEqualTo(CSSBackgroundRepeat.REPEAT);
	
		assertThat(doc.getBgRepeat(pos, 1)).isEqualTo(CSSBackgroundRepeat.NO_REPEAT);
	}

	public void testBgAttachment() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_ATTACHMENT);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgattachment").get(0);
		
		assertThat(doc.getBgAttachment(pos, 0)).isEqualTo(CSSBackgroundAttachment.SCROLL);
	
		assertThat(doc.getBgAttachment(pos, 1)).isEqualTo(CSSBackgroundAttachment.FIXED);
	}

	public void testBgOrigin() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_ORIGIN);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgorigin").get(0);
		
		assertThat(doc.getBgOrigin(pos, 0)).isEqualTo(CSSBackgroundOrigin.PADDING_BOX);
	
		assertThat(doc.getBgOrigin(pos, 1)).isEqualTo(CSSBackgroundOrigin.BORDER_BOX);
	}

	public void testBgClip() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_CLIP);

		final ELEMENT pos = doc.get(CSSTarget.ID, "bgclip").get(0);
		
		assertThat(doc.getBgClip(pos, 0)).isEqualTo(CSSBackgroundOrigin.CONTENT_BOX);
	
		assertThat(doc.getBgClip(pos, 1)).isEqualTo(CSSBackgroundOrigin.PADDING_BOX);
	}

	public void testBg() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG);

		final ELEMENT bgColorOnly = doc.get(CSSTarget.ID, "bgcoloronly").get(0);
		
		assertThat(doc.getBgColorR(bgColorOnly)).isEqualTo((short)0xAA);
		assertThat(doc.getBgColorG(bgColorOnly)).isEqualTo((short)0xBB);
		assertThat(doc.getBgColorB(bgColorOnly)).isEqualTo((short)0xCC);
		assertThat(doc.getBgColorA(bgColorOnly)).isEqualTo(DecimalSize.NONE);
		
		final ELEMENT bgImgNone = doc.get(CSSTarget.ID, "bg_img_none_origin_content_box").get(0);
		
		assertThat(doc.getBgImage(bgImgNone, 0)).isEqualTo(CSSBackgroundImage.NONE);
		assertThat(doc.getBgOrigin(bgImgNone, 0)).isEqualTo(CSSBackgroundOrigin.CONTENT_BOX);
		
		final ELEMENT bgTwoImages = doc.get(CSSTarget.ID, "bg_two_images").get(0);
		
		assertThat(doc.getBgImageURL(bgTwoImages, 0)).isEqualTo("http://www.test.com/image1.png");
		assertThat(doc.getBgImage(bgTwoImages, 0)).isNull();
		
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionLeft(bgTwoImages, 0))).isEqualTo(100);
		assertThat(doc.getBgPositionLeftUnit(bgTwoImages, 0)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionTop(bgTwoImages, 0))).isEqualTo(120);
		assertThat(doc.getBgPositionTopUnit(bgTwoImages, 0)).isEqualTo(CSSUnit.PX);

		assertThat(doc.getBgSize(bgTwoImages, 0)).isEqualTo(CSSBackgroundSize.AUTO);
		assertThat(doc.getBgRepeat(bgTwoImages, 0)).isEqualTo(CSSBackgroundRepeat.REPEAT);
		assertThat(doc.getBgAttachment(bgTwoImages, 0)).isEqualTo(CSSBackgroundAttachment.SCROLL);

		assertThat(doc.getBgImageURL(bgTwoImages, 1)).isEqualTo("http://www.test.com/image2.jpg");
		assertThat(doc.getBgImage(bgTwoImages, 1)).isNull();
		
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionLeft(bgTwoImages, 1))).isEqualTo(150);
		assertThat(doc.getBgPositionLeftUnit(bgTwoImages, 1)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getBgPositionTop(bgTwoImages, 1))).isEqualTo(200);
		assertThat(doc.getBgPositionTopUnit(bgTwoImages, 1)).isEqualTo(CSSUnit.PX);

		assertThat(DecimalSize.decodeToInt(doc.getBgWidth(bgTwoImages, 1))).isEqualTo(400);
		assertThat(doc.getBgWidthUnit(bgTwoImages, 1)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getBgHeight(bgTwoImages, 1))).isEqualTo(300);
		assertThat(doc.getBgHeightUnit(bgTwoImages, 1)).isEqualTo(CSSUnit.PX);

		assertThat(doc.getBgRepeat(bgTwoImages, 1)).isEqualTo(CSSBackgroundRepeat.NO_REPEAT);
		assertThat(doc.getBgAttachment(bgTwoImages, 1)).isEqualTo(CSSBackgroundAttachment.FIXED);
		assertThat(doc.getBgOrigin(bgTwoImages, 1)).isEqualTo(CSSBackgroundOrigin.CONTENT_BOX);
		assertThat(doc.getBgClip(bgTwoImages, 1)).isEqualTo(CSSBackgroundOrigin.PADDING_BOX);

		final ELEMENT bgGradientAngle = doc.get(CSSTarget.ID, "bg_gradient_angle").get(0);
		
		assertThat(doc.getGradientDirectionType(bgGradientAngle, 0)).isEqualTo(CSSGradientDirectionType.ANGLE);
		assertThat(doc.getGradientAngle(bgGradientAngle, 0)).isEqualTo(25);
		assertThat(doc.getGradientPos1(bgGradientAngle, 0)).isNull();
		assertThat(doc.getGradientPos2(bgGradientAngle, 0)).isNull();
		
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[0].getR()).isEqualTo(0xAA);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[0].getG()).isEqualTo(0xBB);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[0].getB()).isEqualTo(0xCC);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[0].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[0].getColor()).isNull();
		checkHasNoDistance(doc, bgGradientAngle, 0, 0);
		
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getColor()).isEqualTo(CSSColor.RED);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].hasDistance()).isTrue();
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].distanceIsPercent()).isTrue();
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].distanceIsLength()).isFalse();
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getDistanceLength()).isEqualTo(DecimalSize.NONE);
		assertThat(DecimalSize.decodeToInt(doc.getGradientColorStops(bgGradientAngle, 0)[1].getDistancePercent())).isEqualTo(40);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[1].getUnit()).isEqualTo(CSSUnit.PCT);

		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getColor()).isEqualTo(CSSColor.BLUE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].hasDistance()).isTrue();
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].distanceIsPercent()).isFalse();
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].distanceIsLength()).isTrue();
		assertThat(DecimalSize.decodeToInt(doc.getGradientColorStops(bgGradientAngle, 0)[2].getDistanceLength())).isEqualTo(200);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getDistancePercent()).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[2].getUnit()).isEqualTo(CSSUnit.PX);

		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[3].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[3].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[3].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[3].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientAngle, 0)[3].getColor()).isEqualTo(CSSColor.GREEN);
		checkHasNoDistance(doc, bgGradientAngle, 0, 3);

		final ELEMENT bgGradientSide = doc.get(CSSTarget.ID, "bg_gradient_side").get(0);
		
		assertThat(doc.getGradientDirectionType(bgGradientSide, 0)).isEqualTo(CSSGradientDirectionType.SIDE);
		assertThat(doc.getGradientAngle(bgGradientSide, 0)).isEqualTo(Angle.NONE);
		assertThat(doc.getGradientPos1(bgGradientSide, 0)).isEqualTo(CSSPositionComponent.RIGHT);
		assertThat(doc.getGradientPos2(bgGradientSide, 0)).isNull();
			
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[0].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[0].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[0].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[0].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[0].getColor()).isEqualTo(CSSColor.RED);
		checkHasNoDistance(doc, bgGradientSide, 0, 0);

		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[1].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[1].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[1].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[1].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientSide, 0)[1].getColor()).isEqualTo(CSSColor.BLUE);
		checkHasNoDistance(doc, bgGradientSide, 0, 1);
		
		final ELEMENT bgGradientCorner = doc.get(CSSTarget.ID, "bg_gradient_corner").get(0);
		
		assertThat(doc.getGradientDirectionType(bgGradientCorner, 0)).isEqualTo(CSSGradientDirectionType.CORNER);
		assertThat(doc.getGradientAngle(bgGradientCorner, 0)).isEqualTo(Angle.NONE);
		assertThat(doc.getGradientPos1(bgGradientCorner, 0)).isEqualTo(CSSPositionComponent.TOP);
		assertThat(doc.getGradientPos2(bgGradientCorner, 0)).isEqualTo(CSSPositionComponent.RIGHT);
		
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[0].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[0].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[0].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[0].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[0].getColor()).isEqualTo(CSSColor.RED);
		checkHasNoDistance(doc, bgGradientCorner, 0, 0);
		
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getColor()).isEqualTo(CSSColor.GREEN);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].hasDistance()).isTrue();
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].distanceIsPercent()).isTrue();
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].distanceIsLength()).isFalse();
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getDistanceLength()).isEqualTo(DecimalSize.NONE);
		assertThat(DecimalSize.decodeToInt(doc.getGradientColorStops(bgGradientCorner, 0)[1].getDistancePercent())).isEqualTo(50);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[1].getUnit()).isEqualTo(CSSUnit.PCT);

		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[2].getR()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[2].getG()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[2].getB()).isEqualTo(ColorRGB.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[2].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientCorner, 0)[2].getColor()).isEqualTo(CSSColor.BLUE);
		checkHasNoDistance(doc, bgGradientCorner, 0, 2);
		
		final ELEMENT bgGradientNone = doc.get(CSSTarget.ID, "bg_gradient_none").get(0);
		
		assertThat(doc.getGradientDirectionType(bgGradientNone, 0)).isEqualTo(CSSGradientDirectionType.NONE);
		assertThat(doc.getGradientAngle(bgGradientNone, 0)).isEqualTo(Angle.NONE);
		assertThat(doc.getGradientPos1(bgGradientNone, 0)).isNull();
		assertThat(doc.getGradientPos2(bgGradientNone, 0)).isNull();
		
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[0].getR()).isEqualTo(0xAA);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[0].getG()).isEqualTo(0xBB);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[0].getB()).isEqualTo(0xCC);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[0].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[0].getColor()).isNull();
		checkHasNoDistance(doc, bgGradientNone, 0, 0);
		
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[1].getR()).isEqualTo(0xDD);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[1].getG()).isEqualTo(0xEE);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[1].getB()).isEqualTo(0xFF);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[1].getA()).isEqualTo(ColorAlpha.NONE);
		assertThat(doc.getGradientColorStops(bgGradientNone, 0)[1].getColor()).isNull();
		checkHasNoDistance(doc, bgGradientNone, 0, 2);
	}
	
	private void checkHasNoDistance(
			ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc,
			ELEMENT ref,
			int bgLayer,
			int colorStop) {
		
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].hasDistance()).isFalse();
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].distanceIsPercent()).isFalse();
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].distanceIsLength()).isFalse();
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].getDistanceLength()).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].getDistancePercent()).isEqualTo(DecimalSize.NONE);
		assertThat(doc.getGradientColorStops(ref, bgLayer)[bgLayer].getUnit()).isNull();
	}

	public void testBgBrowserSpecific() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_BROWSER_SPECIFIC);

		final ELEMENT bg = doc.get(CSSTarget.ID, "bg_browser_specific").get(0);

		// Should just skip this element without exception
	}
	
	public void testBgBrowserSpecificWithNestedFunctions() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_BG_BROWSER_SPECIFIC_WITH_NESTED_FUNCTIONS);

		final ELEMENT bg = doc.get(CSSTarget.ID, "bg_browser_specific_with_nested_functions").get(0);

		// Should just skip this element without exception
	}

	public void testTextDecoration() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_TEXT_DECORATION);

		final ELEMENT td1 = doc.get(CSSTarget.ID, "text_decoration_1").get(0);
		
		assertThat(doc.getTextDecoration(td1)).isEqualTo(CSSTextDecoration.UNDERLINE);
	}

	public void testFilterNone() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_FILTER_NONE);

		final ELEMENT f = doc.get(CSSTarget.ID, "filter_none").get(0);
		
		assertThat(doc.getFilter(f)).isEqualTo(CSSFilter.NONE);
	}

	public void testFilter() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_FILTER);

		final ELEMENT f = doc.get(CSSTarget.ID, "filter").get(0);
		
		assertThat(doc.getFilter(f)).isNull();
		assertThat(doc.getBlur(f)).isEqualTo(3);
		assertThat(DecimalSize.decodeToInt(doc.getBrightness(f))).isEqualTo(150);
		assertThat(DecimalSize.decodeToInt(doc.getContrast(f))).isEqualTo(120);
		
		assertThat(DecimalSize.decodeToInt(doc.getDropShadowH(f))).isEqualTo(5);
		assertThat(doc.getDropShadowHUnit(f)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getDropShadowV(f))).isEqualTo(7);
		assertThat(doc.getDropShadowVUnit(f)).isEqualTo(CSSUnit.PX);
		assertThat(doc.getDropShadowBlur(f)).isEqualTo(6);
		assertThat(doc.getDropShadowColor(f)).isEqualTo(CSSColor.RED);
		
		assertThat(DecimalSize.decodeToInt(doc.getGrayscale(f))).isEqualTo(100);
		assertThat(DecimalSize.decodeToInt(doc.getHueRotate(f))).isEqualTo(75);
		assertThat(DecimalSize.decodeToInt(doc.getInvert(f))).isEqualTo(95);
		assertThat(DecimalSize.decodeToInt(doc.getOpacity(f))).isEqualTo(25);
		assertThat(DecimalSize.decodeToInt(doc.getSaturate(f))).isEqualTo(4);
		assertThat(DecimalSize.decodeToInt(doc.getSepia(f))).isEqualTo(83);
		assertThat(doc.getFilterURL(f)).isEqualTo("http://foo");
	}

	public void testFilter2() throws IOException, ParserException {
		final ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc = parse(TestData.CSS_FILTER2);

		final ELEMENT f = doc.get(CSSTarget.ID, "filter2").get(0);
		
		assertThat(doc.getFilter(f)).isNull();
		
		assertThat(DecimalSize.decodeToInt(doc.getDropShadowH(f))).isEqualTo(5);
		assertThat(doc.getDropShadowHUnit(f)).isEqualTo(CSSUnit.PX);
		assertThat(DecimalSize.decodeToInt(doc.getDropShadowV(f))).isEqualTo(7);
		assertThat(doc.getDropShadowVUnit(f)).isEqualTo(CSSUnit.PX);
		assertThat(doc.getDropShadowBlur(f)).isEqualTo(6);
		assertThat(doc.getDropShadowSpread(f)).isEqualTo(4);
		assertThat(doc.getDropShadowR(f)).isEqualTo(0xAA);
		assertThat(doc.getDropShadowG(f)).isEqualTo(0xBB);
		assertThat(doc.getDropShadowB(f)).isEqualTo(0xCC);
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

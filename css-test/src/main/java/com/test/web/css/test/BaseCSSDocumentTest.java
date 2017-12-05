package com.test.web.css.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
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
	
	private void checkMargin(ICSSDocumentParserListener<ELEMENT, TOKENIZER, Void> doc, String id,
			int top, CSSUnit topUnit, CSSJustify topJustify,
			int right, CSSUnit rightUnit, CSSJustify rightJustify,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomJustify,
			int left, CSSUnit leftUnit, CSSJustify leftJustify) {

		final ELEMENT ref = doc.get(CSSTarget.ID, id).get(0);
		
		final TestCSSJustify margins = new TestCSSJustify();

		
		doc.getMargins(ref, margins, null);
		
		assertThat(DecimalSize.decodeToInt(margins.top)).isEqualTo(top);
		assertThat(margins.topUnit).isEqualTo(topUnit);
		assertThat(margins.topType).isEqualTo(topJustify);
		assertThat(DecimalSize.decodeToInt(margins.right)).isEqualTo(right);
		assertThat(margins.rightUnit).isEqualTo(rightUnit);
		assertThat(margins.rightType).isEqualTo(rightJustify);
		assertThat(DecimalSize.decodeToInt(margins.bottom)).isEqualTo(bottom);
		assertThat(margins.bottomUnit).isEqualTo(bottomUnit);
		assertThat(margins.bottomType).isEqualTo(bottomJustify);
		assertThat(DecimalSize.decodeToInt(margins.left)).isEqualTo(left);
		assertThat(margins.leftUnit).isEqualTo(leftUnit);
		assertThat(margins.leftType).isEqualTo(leftJustify);
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

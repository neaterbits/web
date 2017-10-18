package com.test.web.css._long;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.io.common.SimpleLoadStream;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;


public class CSSDocumentTest extends TestCase {

	public void testParser() throws IOException, ParserException {
		
		final String css = TestData.CSS; 
				
		final LongCSSDocument doc = new LongCSSDocument();
		
		final StringBuffers buffers = new StringBuffers(new SimpleLoadStream(css));
		
		final CSSParser<LongTokenizer, Void> parser = new CSSParser<>(buffers, doc);
		
		parser.parseCSS();
		
		final int h1Ref = doc.get(CSSTarget.TAG, "h1");
		
		assertThat(doc.isSet(CSSTarget.TAG, "h1", CSStyle.HEIGHT)).isTrue();
		assertThat(doc.isSet(CSSTarget.TAG, "h1", CSStyle.WIDTH)).isTrue();
		
		assertThat(doc.isSet(h1Ref, CSStyle.HEIGHT)).isTrue();
		assertThat(doc.isSet(h1Ref, CSStyle.WIDTH)).isTrue();
		
		assertThat(doc.getHeightUnit(h1Ref)).isEqualTo(CSSUnit.PX);
		assertThat(doc.getHeight(h1Ref)).isEqualTo(100);
		
		assertThat(doc.getWidthUnit(h1Ref)).isEqualTo(CSSUnit.PCT);
		assertThat(doc.getWidth(h1Ref)).isEqualTo(20);
		
		final int idRef = doc.get(CSSTarget.ID, "an_element");

		assertThat(doc.isSet(idRef, CSStyle.MARGIN_LEFT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.MARGIN_RIGHT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.FLOAT)).isTrue();
		assertThat(doc.isSet(idRef, CSStyle.POSITION)).isTrue();
		
		final TestCSSJustify margins = new TestCSSJustify();
		
		assertThat(doc.getFloat(idRef)).isEqualTo(CSSFloat.LEFT);
		assertThat(doc.getPosition(idRef)).isEqualTo(CSSPosition.RELATIVE);
		
		doc.getMargins(idRef, margins, null);
		
		System.out.println("Margins: " + margins);
		
		assertThat(margins.left).isEqualTo(10);
		assertThat(margins.leftUnit).isEqualTo(CSSUnit.PX);
		assertThat(margins.leftType).isEqualTo(CSSJustify.SIZE);
		
		assertThat(margins.rightType).isEqualTo(CSSJustify.AUTO);
		assertThat(margins.topType).isEqualTo(CSSJustify.NONE);
		assertThat(margins.bottomType).isEqualTo(CSSJustify.NONE);

		final int classRef = doc.get(CSSTarget.CLASS, "a_class");
		assertThat(doc.isSet(classRef, CSStyle.POSITION)).isTrue();
		assertThat(doc.isSet(classRef, CSStyle.FLOAT)).isTrue();
		assertThat(doc.isSet(classRef, CSStyle.PADDING_TOP)).isTrue();

		assertThat(doc.getFloat(classRef)).isEqualTo(CSSFloat.RIGHT);
		assertThat(doc.getPosition(classRef)).isEqualTo(CSSPosition.ABSOLUTE);
		
		final TestCSSJustify padding = new TestCSSJustify();
		doc.getPadding(classRef, padding, null);
		
		System.out.println("Padding: " + padding);

		assertThat(padding.top).isEqualTo(30);
		assertThat(padding.topUnit).isEqualTo(CSSUnit.PX);
		assertThat(padding.topType).isEqualTo(CSSJustify.SIZE);
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

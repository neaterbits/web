package com.test.web.css._long;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.test.web.css.common.CSSFloat;
import com.test.web.css.common.CSSTarget;
import com.test.web.css.common.CSSUnit;
import com.test.web.css.common.CSStyle;
import com.test.web.css.common.ICSSJustify;
import com.test.web.css.common.Justify;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.css.CSSPosition;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;


public class CSSDocumentTest extends TestCase {

	public void testParser() throws IOException, ParserException {
		
		final String css = 
				
"h1 {\n" +
"  width : 20%;\n" +
"  height : 100px;\n" +
"  background-color : #AABBCC;\n" +
"}\n" +

"#an_element {\n" +
"  margin-left : 10;\n" +
"  margin-right : auto;\n" +

"  float: left;\n" +
"  position: relative;\n" +
"}\n" +

".a_class {\n" +
"  position : absolute;\n" +
"  float : right;\n" +
"  padding-top : 30px;\n" +
"}\n";				
		
		
		final LongCSSDocument doc = new LongCSSDocument();
		
		final StringBuffers buffers = new StringBuffers(new ByteArrayInputStream(css.getBytes()));
		
		
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
		assertThat(margins.leftType).isEqualTo(Justify.SIZE);
		
		assertThat(margins.rightType).isEqualTo(Justify.AUTO);
		assertThat(margins.topType).isEqualTo(Justify.NONE);
		assertThat(margins.bottomType).isEqualTo(Justify.NONE);

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
		assertThat(padding.topType).isEqualTo(Justify.SIZE);
	}
	
	private static class TestCSSJustify implements ICSSJustify<Void> {

		int top;
		CSSUnit topUnit;
		Justify topType;
		
		int right;
		CSSUnit rightUnit;
		Justify rightType;
		
		int bottom;
		CSSUnit bottomUnit;
		Justify bottomType;
		
		int left;
		CSSUnit leftUnit;
		Justify leftType;
		
		@Override
		public void set(Void param,
				int top, CSSUnit topUnit, Justify topType,
				int right, CSSUnit rightUnit, Justify rightType,
				int bottom, CSSUnit bottomUnit, Justify bottomType,
				int left, CSSUnit leftUnit, Justify leftType) {
			
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

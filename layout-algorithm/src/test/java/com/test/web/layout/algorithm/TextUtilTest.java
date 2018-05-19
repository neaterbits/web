package com.test.web.layout.algorithm;

import com.test.web.layout.algorithm.TextUtil;
import com.test.web.layout.common.MockTextExtent;
import com.test.web.render.common.IFont;
import com.test.web.render.common.ITextExtent;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class TextUtilTest extends TestCase {

	public void testNewline() {
		final ITextExtent textExtent = new MockTextExtent();
		
		final TextUtil textUtil = new TextUtil(textExtent);
		final IFont font = textExtent.getFont(null, 0, 0);
	
		try {
			final int numChars = textUtil.findNumberOfChars("\n", 100, font).getNumberOfChars();

			fail("Expected exception due to newline");
		}
		catch (IllegalArgumentException ex) {
			
		}
	}
	
	
	public void testText() {
		final ITextExtent textExtent = new MockTextExtent();
		
		final TextUtil textUtil = new TextUtil(textExtent);
		final IFont font = textExtent.getFont(null, 0, 0);

		final int numChars = textUtil.findNumberOfChars("Ths is a text that will wrap", 50, font).getNumberOfChars();

		assertThat(numChars).isEqualTo(4);
	}
}

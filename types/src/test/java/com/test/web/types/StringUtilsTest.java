package com.test.web.types;

import static org.assertj.core.api.Assertions.assertThat;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

	public void testSplit() {
		checkSplit("", ':', "");
		checkSplit(":", ':', "", "");
		checkSplit("a:", ':', "a", "");
		checkSplit(":b", ':', "", "b");
		checkSplit("a:b", ':', "a", "b");
		checkSplit(" a : b ", ':', " a ", " b ");
	}
	
	private void checkSplit(String s, char c, String ... expected) {
		final String [] result = StringUtils.split(s, c);
		
		assertThat(result.length).isEqualTo(expected.length);
		
		for (int i = 0; i < expected.length; ++ i) {
			assertThat(result[i]).isEqualTo(expected[i]);
		}
	}
}

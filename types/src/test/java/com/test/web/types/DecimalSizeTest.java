package com.test.web.types;

import static org.assertj.core.api.Assertions.assertThat;

import junit.framework.TestCase;

public class DecimalSizeTest extends TestCase {

	public void testDecimals() {
		checkEncodeDecode(0, "150", "0.150");
		checkEncodeDecode(0, "", "0");
		checkEncodeDecode(0, "15", "0.15");
		checkEncodeDecode(0, "015", "0.015");
		checkEncodeDecode(0, "0150", "0.0150");
		checkEncodeDecode(1, "", "1");
		checkEncodeDecode(1, "0150", "1.0150");
	}
	
	private void checkEncodeDecode(int beforeComma, String afterComma, String expected) {
		final int encoded = new DecimalSize(beforeComma, afterComma).encodeAsInt();
		
		assertThat(DecimalSize.decodeToString(encoded)).isEqualTo(expected);
	}
}


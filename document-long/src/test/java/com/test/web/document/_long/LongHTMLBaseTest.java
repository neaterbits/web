package com.test.web.document._long;

import static org.assertj.core.api.Assertions.assertThat;

import junit.framework.TestCase;

public class LongHTMLBaseTest extends TestCase {

	
	public void testSetGetLower32() {
		
		final long [] buf = new long [3];
		
		LongHTMLBase.setLower32(buf, 1, 0xFFFFFFFF);
		
		assertThat(buf[0]).isEqualTo(0L);
		assertThat(buf[1]).isEqualTo(0x00000000FFFFFFFFL);
		assertThat(buf[2]).isEqualTo(0L);

		assertThat(LongHTMLBase.getLower32(buf, 1)).isEqualTo(0xFFFFFFFF);
		
	}
	
	public void testSetGetUpper32() {
		
		final long [] buf = new long [3];
		
		LongHTMLBase.setUpper32(buf, 1, 0xFFFFFFFF);
		
		assertThat(buf[0]).isEqualTo(0L);
		assertThat(buf[1]).isEqualTo(0xFFFFFFFF00000000L);
		assertThat(buf[2]).isEqualTo(0L);

		assertThat(LongHTMLBase.getUpper32(buf, 1)).isEqualTo(0xFFFFFFFF);
		
	}
}

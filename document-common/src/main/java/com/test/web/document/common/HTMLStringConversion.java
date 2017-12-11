package com.test.web.document.common;

import java.math.BigDecimal;

import com.test.web.types.BigDecimalConversion;

public class HTMLStringConversion {

	public static String booleanString(boolean value) {
		return value ? "true" : "false";
	}
	
	// returns null in case does not really parse to a boolean
	public static Boolean booleanValue(String value) {
		final Boolean ret;
		
		if (value.equals("true")) {
			ret = true;
		}
		else if (value.equals("false")) {
			ret = false;
		}
		else {
			ret = null;
		}

		return ret;
	}

	public static String fromBigDecimal(BigDecimal value) {
		return BigDecimalConversion.fromBigDecimal(value);
	}
}

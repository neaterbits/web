package com.test.web.jsapi.dom;

import com.neaterbits.util.IEnum;

public class JSEnum {

	public static <E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, String s) {
		return IEnum.asEnum(enumClass, s, true);
	}
}

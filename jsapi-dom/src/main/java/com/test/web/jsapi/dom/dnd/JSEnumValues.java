package com.test.web.jsapi.dom.dnd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JSEnumValues {
	public Class<? extends Enum<?>> value();
}

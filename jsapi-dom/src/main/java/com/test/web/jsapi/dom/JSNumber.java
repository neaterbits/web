package com.test.web.jsapi.dom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotating a JS facting API to be a Number even if returns or has double as parameter
 * 
 * When used on a method, targets the return value
 */


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface JSNumber {

}

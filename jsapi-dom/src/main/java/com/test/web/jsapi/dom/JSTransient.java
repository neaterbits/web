package com.test.web.jsapi.dom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tell that a method should not be mapped to JS when returned to JS engine
 * 
 *  TODO see if can make this work in practice
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface JSTransient {

}

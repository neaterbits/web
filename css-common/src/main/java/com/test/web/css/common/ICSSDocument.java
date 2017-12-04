package com.test.web.css.common;

import java.util.List;

import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;

/**
 * Read-only access to a CSS document, must be assumed to be thread-safe after parsing, ie. we will make sure to never update while rendering.
 *
 * @author nhl
 *
 */

public interface ICSSDocument<TARGET> extends ICSSDocumentStyles<TARGET> {

	/**
	 * Check whether a particular style is set for a target in this document
	 * 
	 * @param target
	 * @param targetName
	 * @param style
	 * 
	 * @return true if set
	 */
	
	boolean isSet(CSSTarget target, String targetName, CSStyle style);

	List<TARGET> get(CSSTarget target, String targetName);
	
}

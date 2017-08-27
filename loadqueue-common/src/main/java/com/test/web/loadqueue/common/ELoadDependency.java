package com.test.web.loadqueue.common;


/**
 * Type of load dependency
 * @author nhl
 *
 */

public enum ELoadDependency {

	LOAD_STYLESHEET, // must load stylesheet eg. before can compute layout
	
	IMAGE_SIZE, // must have image size before can layout element, eg. if img tag does not have width and height
	
	UNTIL_ELEMENT_LOADED, // For JS, if an element cannot be found, we might have to load until found, or loaded whole file
	
	UNTIL_BLOCK_LOADED; // For JS, until HTML sub block has been loaded
	
}

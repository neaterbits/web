package com.test.web.layout.algorithm;

/**
 * Helper interface for sizes of container box when computing layout dimensions for sub elements.
 * 
 */

interface ContainerDimensions {

	/**
	 * Container width, ie. the width that '%' sizes in sub element will be computed from.
	 * 
	 * @return width
	 */
	int getAvailableWidth();
	
	/**
	 * Container height, ie. the width that '%' sizes in sub element will be computed from.
	 * 
	 * @return width
	 */
	int getAvailableHeight();
	
	/**
	 * Remaining container width, ie. remaining space we can add to container
	 * Useful for margin: auto
	 * 
	 * @return width
	 */
	int getRemainingWidth();
	
	/**
	 * Remaining container height, ie. remaining space we can add to container
	 * 
	 * @return width
	 */
	int getRemainingHeight();
	
	/**
	 * Get current pixel x start position for text line within this container, ie. when adding inline-elements
	 * 
	 * @return position
	 */
	
	int getLineStartXPos();

	/**
	 * Get current pixel y start position for text line within this container, ie. when adding inline-elements.
	 * 
	 * @return position
	 */
	
	int getCurBlockYPos();
}

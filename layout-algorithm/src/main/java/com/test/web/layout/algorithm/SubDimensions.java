package com.test.web.layout.algorithm;

/**
 * Access to information about a sub element, useful when computing dimensions
 */
public interface SubDimensions {

	/**
	 * Max height of inline element, will only work if this sub-element is an inline element.
	 * Eg if one line of text, then is lengt of that line. If multiple lines that wrap, then the length of all those.
	 * Padding is not taken into consideration, only content (though padding in nested inline elements would be summarized in end-tag)
	 * 
	 * @return inline element width
	 */
	@Deprecated
	int getInlineContentMaxWidth();

	/**
	 * Complete height of inline element, will only work if this sub-element is an inline element
	 * 
	 * @return inline element height
	 */
	int getInlineContentHeight();


	/**
	 * For block element, get collected block height
	 * 
	 * @return block height
	 */
	int getCollectedBlockHeight();
}

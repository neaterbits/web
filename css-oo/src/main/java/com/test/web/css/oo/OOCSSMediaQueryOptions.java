package com.test.web.css.oo;

import com.test.web.css.common.enums.CSSLogicalOperator;

// Helper interface for building model
interface OOCSSMediaQueryOptions {

	void addOption(OOCSSMediaQueryOption option);
	
	void setLogicalOperator(CSSLogicalOperator logicalOperator);

	int getNumOptions();
	
	OOCSSMediaQueryOption getOption(int index);
}

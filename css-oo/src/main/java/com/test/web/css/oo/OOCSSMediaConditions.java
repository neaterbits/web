package com.test.web.css.oo;

import java.util.ArrayList;
import java.util.List;

import com.test.web.css.common.enums.CSSLogicalOperator;

final class OOCSSMediaConditions extends OOCSSMediaQueryOption implements OOCSSMediaQueryOptions {

	private final boolean negated;
	private final List<OOCSSMediaQueryOption> sub;
	private CSSLogicalOperator logicalOperator; // and/or
	
	OOCSSMediaConditions(boolean negated) {
		this.negated = negated;
		this.sub = new ArrayList<>();
	}

	boolean isNegated() {
		return negated;
	}

	List<OOCSSMediaQueryOption> getSub() {
		return sub;
	}

	CSSLogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	@Override
	public void addOption(OOCSSMediaQueryOption option) {
		
		if (option == null) {
			throw new IllegalArgumentException("option == null");
		}
		
		sub.add(option);
	}

	@Override
	public void setLogicalOperator(CSSLogicalOperator logicalOperator) {
		
		if (logicalOperator == null) {
			throw new IllegalArgumentException("logicalOperator == null");
		}

		if (this.logicalOperator != null && this.logicalOperator != logicalOperator) {
			throw new IllegalStateException("trying to set to different operator: " + this.logicalOperator + "/" + logicalOperator);
		}
	}

	@Override
	public int getNumOptions() {
		return sub.size();
	}

	@Override
	public OOCSSMediaQueryOption getOption(int index) {
		return sub.get(index);
	}
}

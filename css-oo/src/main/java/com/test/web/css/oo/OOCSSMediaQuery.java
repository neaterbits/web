package com.test.web.css.oo;

import java.util.ArrayList;
import java.util.List;

import com.test.web.css.common.enums.CSSLogicalOperator;
import com.test.web.css.common.enums.CSSMediaType;

final class OOCSSMediaQuery extends OOCSSBase implements OOCSSMediaQueryOptions {

	private final boolean negated;
	private final CSSMediaType mediaType;
	private List<OOCSSMediaQueryOption> options;

	OOCSSMediaQuery(boolean negated, CSSMediaType mediaType) {
		this.negated = negated;
		this.mediaType = mediaType;
		this.options = null;
	}

	boolean isNegated() {
		return negated;
	}

	CSSMediaType getMediaType() {
		return mediaType;
	}

	List<OOCSSMediaQueryOption> getOptions() {
		return options;
	}

	@Override
	public void addOption(OOCSSMediaQueryOption option) {
		if (option == null) {
			throw new IllegalArgumentException("option == null");
		}

		if (this.options == null) {
			this.options = new ArrayList<>();
		}
		
		this.options.add(option);
	}

	@Override
	public void setLogicalOperator(CSSLogicalOperator logicalOperator) {
		throw new UnsupportedOperationException("Logical operator is always AND at toplevel of media query");
	}

	@Override
	public int getNumOptions() {
		return options.size();
	}

	@Override
	public OOCSSMediaQueryOption getOption(int index) {
		return options.get(index);
	}
}

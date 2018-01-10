package com.test.web.css.oo;

import java.util.ArrayList;
import java.util.List;

import com.test.web.css.common.enums.CSSMediaType;

final class OOCSSMediaQueryList {
	
	private final List<OOCSSMediaQuery> entries;

	OOCSSMediaQueryList() {
		this.entries = new ArrayList<>();
	}

	OOCSSMediaQuery addMediaQuery(boolean negated, CSSMediaType mediaType) {

		final OOCSSMediaQuery mediaQuery = new OOCSSMediaQuery(negated, mediaType);

		entries.add(mediaQuery);

		return mediaQuery;
	}

	int getNumMediaQueries() {
		return entries.size();
	}
	
	OOCSSMediaQuery getMediaQuery(int mediaQueryIndex) {
		return entries.get(mediaQueryIndex);
	}
}

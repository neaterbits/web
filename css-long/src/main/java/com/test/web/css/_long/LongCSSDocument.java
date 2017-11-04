package com.test.web.css._long;

import com.test.web.css.common.CSSState;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.css.ICSSDocumentParserListener;

/***
 * For storing and accessing a CSS document, encoding in arrays of long [] for less GC and memory churn
 * 
 * Must be singlethreaded during parsing
 * 
 * @author nhl
 *
 */

public final class LongCSSDocument
	extends BaseLongCSSDocument
	implements ICSSDocumentParserListener<Integer, LongTokenizer, Void> {

	private final CSSState<Integer> state;

	public LongCSSDocument() {
		this.state = new CSSState<>();
	}
	
	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final Integer entry = state.get(target, targetName);

		final boolean isSet;

		if (entry == null) {
			isSet = false;
		}
		else {
			isSet = LongCSS.hasStyle(style, buf(entry), offset(entry));
		}

		return isSet;
	}
	
	@Override
	public Integer get(CSSTarget target, String targetName) {
		return state.get(target, targetName);
	}
	

	/***************************************************** Parse listener *****************************************************/ 

	@Override
	public Void onEntityStart(CSSTarget target, String targetName) {
		
		// Started a CSS entity, must allocate out of buffer
		// Start out allocating a compact element, we might re-allocate if turns out to have more than the standard compact elements
		final int element = allocateCurParseElement(target.name());
		
		state.add(target, targetName, element);
		
		return null;
	}

	@Override
	public void onEntityEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}
}

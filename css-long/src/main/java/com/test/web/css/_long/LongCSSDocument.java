package com.test.web.css._long;

import java.util.List;

import com.test.web.css.common.CSSState;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;
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
	implements ICSSDocumentParserListener<Integer, Void> {

	private final CSSState<Integer> state;

	public LongCSSDocument() {
		this.state = new CSSState<>();
	}
	
	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final List<Integer> entries = state.get(target, targetName);

		final boolean isSet;

		if (entries == null) {
			isSet = false;
		}
		else {
			boolean has = false;
			for (int entry : entries) {
				if (LongCSS.hasStyle(style, buf(entry), offset(entry))) {
					has = true;
					break;
				}
			}
			
			isSet = has;
		}

		return isSet;
	}
	
	@Override
	public List<Integer> get(CSSTarget target, String targetName) {
		return state.get(target, targetName);
	}
	

	/***************************************************** Parse listener *****************************************************/ 

	@Override
	public Void onBlockStart() {
		allocateCurParseElement();
		
		return null;
	}
	
	@Override
	public void onEntityMap(Void context, CSSTarget target, String targetName) {
		
		// Started a CSS entity, must allocate out of buffer
		// Start out allocating a compact element, we might re-allocate if turns out to have more than the standard compact elements
		
		state.add(target, targetName, ref());
	}


	@Override
	public void onBlockEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}
}

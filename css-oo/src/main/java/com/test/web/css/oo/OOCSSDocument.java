package com.test.web.css.oo;


import java.util.Collections;
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

public final class OOCSSDocument
	extends BaseOOCSSDocument
	implements ICSSDocumentParserListener<OOCSSElement, Void> {

	private final CSSState<OOCSSElement> state;
	
	public OOCSSDocument() {
		this.state = new CSSState<>();
	}

	/***************************************************** Access interface *****************************************************/ 

	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final List<OOCSSElement> entries = state.get(target, targetName);

		final boolean isSet;

		if (entries == null) {
			isSet = false;
		}
		else {
			boolean has = false;
			
			for (OOCSSElement entry : entries) {
				if (entry.hasStyle(style)) {
					has = true;
					break;
				}
			}
			
			isSet = has;
		}

		return isSet;
	}
	
	@Override
	public List<OOCSSElement> get(CSSTarget target, String targetName) {
		final List<OOCSSElement> elems = state.get(target, targetName);
		
		return elems != null ? elems : Collections.emptyList();
	}
	

	/***************************************************** Parse listener *****************************************************/ 


	@Override
	public Void onBlockStart() {
		allocateCurParseElement();
		
		return null;
	}
	
	
	@Override
	public void onEntityMap(Void context, CSSTarget target, String targetName) {
		
		//System.out.println("## CSS target start " + target + "/" + targetName);
		
	   state.add(target, targetName, ref());
	}

	@Override
	public void onBlockEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}
}

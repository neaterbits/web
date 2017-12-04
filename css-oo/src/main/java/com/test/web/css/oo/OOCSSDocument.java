package com.test.web.css.oo;


import com.test.web.css.common.CSSState;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.oo.OOTokenizer;
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
	implements ICSSDocumentParserListener<OOCSSElement, OOTokenizer, Void> {

	private final CSSState<OOCSSElement> state;
	
	public OOCSSDocument() {
		this.state = new CSSState<>();
	}

	/***************************************************** Access interface *****************************************************/ 

	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final OOCSSElement entry = state.get(target, targetName);

		final boolean isSet;

		if (entry == null) {
			isSet = false;
		}
		else {
			isSet = entry.hasStyle(style);
		}

		return isSet;
	}
	
	@Override
	public OOCSSElement get(CSSTarget target, String targetName) {
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
		
		//System.out.println("## CSS target start " + target + "/" + targetName);
		
	   state.add(target, targetName, ref());
	}

	@Override
	public void onBlockEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}
}

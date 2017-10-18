package com.test.web.css._long;


import java.util.HashMap;
import java.util.Map;

import com.test.web.css.common.ICSSDocument;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSStyle;

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
	implements ICSSDocument<Integer> {

	// CSS types by element tag, ID and class
	private Map<String, Integer> byTag;
	private Map<String, Integer> byId;
	private Map<String, Integer> byClass;
	

	private Map<String, Integer> getMap(CSSTarget target) {
		final Map<String, Integer> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new HashMap<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new HashMap<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new HashMap<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}

		return map;
	}
	
	/***************************************************** Access interface *****************************************************/ 
	

	@Override
	public boolean isSet(CSSTarget target, String targetName, CSStyle style) {

		final Integer entry = getMap(target).get(targetName);

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
		return getMap(target).get(targetName);
	}
	

	/***************************************************** Parse listener *****************************************************/ 

	@Override
	public Void onEntityStart(CSSTarget target, String targetName) {
		
		if (targetName == null || targetName.isEmpty()) {
			throw new IllegalArgumentException("no target name");
		}
		
		final Map<String, Integer> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new HashMap<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new HashMap<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new HashMap<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}
		
		
		// Started a CSS entity, must allocate out of buffer
		// Start out allocating a compact element, we might re-allocate if turns out to have more than the standard compact elements
		final int element = allocateCurParseElement(target.name());
		
		// Store in map
		if (map.containsKey(targetName)) {
			throw new IllegalStateException("TODO: handle multiple instanes for same target name");
		}
		
		map.put(targetName, element);

		return null;
	}

	@Override
	public void onEntityEnd(Void context) {
		// Nothing to do as write pos in buffer was already advanced
	}
}

package com.test.web.css.common;

import java.util.HashMap;
import java.util.Map;

import com.test.web.css.common.enums.CSSTarget;

public final class CSSState<ELEMENT> {
	// CSS types by element tag, ID and class
	private Map<String, ELEMENT> byTag;
	private Map<String, ELEMENT> byId;
	private Map<String, ELEMENT> byClass;

	public CSSState() {
		
	}
	
	private Map<String, ELEMENT> getMap(CSSTarget target) {
		final Map<String, ELEMENT> map;
		
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

	public ELEMENT get(CSSTarget target, String targetName) {
		return getMap(target).get(targetName);
	}

	public void add(CSSTarget target, String targetName, ELEMENT element) {

		if (targetName == null || targetName.isEmpty()) {
			throw new IllegalArgumentException("no target name");
		}
		
		final Map<String, ELEMENT> map;
		
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

		// Store in map
		if (map.containsKey(targetName)) {
			throw new IllegalStateException("TODO: handle multiple instances for same target name");
		}

		map.put(targetName, element);
	}
}

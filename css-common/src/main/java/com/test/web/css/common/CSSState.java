package com.test.web.css.common;

import java.util.Collections;
import java.util.List;

import com.test.web.css.common.enums.CSSTarget;
import com.test.web.types.MapOfList;

public final class CSSState<ELEMENT> {
	// CSS types by element tag, ID and class
	private MapOfList<String, ELEMENT> byTag;
	private MapOfList<String, ELEMENT> byId;
	private MapOfList<String, ELEMENT> byClass;

	public CSSState() {
		
	}
	
	private MapOfList<String, ELEMENT> getMap(CSSTarget target) {
		final MapOfList<String, ELEMENT> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new MapOfList<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new MapOfList<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new MapOfList<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}

		return map;
	}

	public List<ELEMENT> get(CSSTarget target, String targetName) {
		final List<ELEMENT> elems = getMap(target).get(targetName);
		
		return elems != null ? elems : Collections.emptyList();
	}

	public void add(CSSTarget target, String targetName, ELEMENT element) {

		if (targetName == null || targetName.isEmpty()) {
			throw new IllegalArgumentException("no target name");
		}
		
		final MapOfList<String, ELEMENT> map;
		
		switch (target) {
		case TAG:
			if (this.byTag == null) {
				this.byTag = new MapOfList<>();
			}
			map = this.byTag;
			break;
			
		case ID:
			if (this.byId == null) {
				this.byId = new MapOfList<>();
			}
			map = this.byId;
			break;

		case CLASS:
			if (this.byClass == null) {
				this.byClass = new MapOfList<>();
			}
			map = this.byClass;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected target " + target);
		}

		map.add(targetName, element);
	}
}

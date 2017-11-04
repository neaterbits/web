package com.test.web.document.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Common state for documents
public final class DocumentState<ELEMENT> {

	// Stack for position while parsing
	private final Map<String, ELEMENT> elementById;
	private final Map<String, List<ELEMENT>> elementsByClass;

	public DocumentState() {
		this.elementById = new HashMap<>();
		this.elementsByClass = new HashMap<>();
	}
	
	public void addElement(String idString, ELEMENT element) {
		elementById.put(idString, element);
	}
	
	public ELEMENT getElementById(String id) {
		return elementById.get(id);
	}
	
	public void addElementClass(String classString, ELEMENT element) {
		List<ELEMENT> elementsWithClass = elementsByClass.get(classString);
		
		if (elementsWithClass == null) {
			elementsWithClass = new ArrayList<>();
			elementsByClass.put(classString, elementsWithClass);

			elementsWithClass.add(element);
		}
		else {
			if (!elementsWithClass.contains(element)) {
				elementsWithClass.add(element);
			}
		}
	}

	public List<ELEMENT> getElementsWithClass(String _class) {
		
		if (_class == null) {
			throw new IllegalArgumentException("class == null");
		}
		
		final List<ELEMENT> elements = elementsByClass.get(_class);
		
		return elements == null ? Collections.emptyList() : Collections.unmodifiableList(elements);
	}
}

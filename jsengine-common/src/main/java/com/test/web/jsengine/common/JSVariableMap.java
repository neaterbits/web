package com.test.web.jsengine.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

// A map for adding JS variables that are prepared for the runtime
public class JSVariableMap {
	private final Map<String, IJavaAsJSObject> variables;
	
	public JSVariableMap() {
		this.variables = new HashMap<>();
	}
	
	public void add(String name, IJavaAsJSObject jsObject) {
		if (name == null) {
			throw new IllegalArgumentException("name == null");
		}
		
		if (jsObject == null) {
			throw new IllegalArgumentException("jsObject == null");
		}
		
		variables.put(name, jsObject);
	}

	public void forEachKeyValue(BiConsumer<String, IJavaAsJSObject> each) {
		for (Map.Entry<String, IJavaAsJSObject> entry : variables.entrySet()) {
			each.accept(entry.getKey(), entry.getValue());
		}
	}
}

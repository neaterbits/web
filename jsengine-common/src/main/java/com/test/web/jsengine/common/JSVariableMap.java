package com.test.web.jsengine.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

// A map for adding JS variables that are prepared for the runtime
public class JSVariableMap {
	private final Map<String, IJavaAsJSObject> variables;
	private final Map<String, Object> reflected;
	
	public JSVariableMap() {
		this.variables = new HashMap<>();
		this.reflected = new HashMap<>();
	}
	
	public void addWrapped(String name, IJavaAsJSObject jsObject) {
		if (name == null) {
			throw new IllegalArgumentException("name == null");
		}
		
		if (jsObject == null) {
			throw new IllegalArgumentException("jsObject == null");
		}
		
		variables.put(name, jsObject);
	}

	public void addReflected(String name, Object jsObject) {
		if (name == null) {
			throw new IllegalArgumentException("name == null");
		}
		
		if (jsObject == null) {
			throw new IllegalArgumentException("jsObject == null");
		}
		
		reflected.put(name, jsObject);
	}

	public void forEachWrapped(BiConsumer<String, IJavaAsJSObject> each) {
		for (Map.Entry<String, IJavaAsJSObject> entry : variables.entrySet()) {
			each.accept(entry.getKey(), entry.getValue());
		}
	}

	public void forEachReflected(BiConsumer<String, Object> each) {
		for (Map.Entry<String, Object> entry : reflected.entrySet()) {
			each.accept(entry.getKey(), entry.getValue());
		}
	}
}

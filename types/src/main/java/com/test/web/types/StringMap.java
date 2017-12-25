package com.test.web.types;

import java.util.HashMap;
import java.util.Map;

// Can be changed to a trie
public final class StringMap<T> {

	private final Map<String, T> map;


	public StringMap() {
		this.map = new HashMap<>();
	}
	
	public void add(String key, T value) {
		
		if (key == null) {
			throw new IllegalArgumentException("key == null");
		}
		
		if (map.put(key, value) != null) {
			throw new IllegalStateException("Aready contained " + key);
		}
	}
	
	public T get(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key == nul");
		}
		
		return map.get(key);
	}
}

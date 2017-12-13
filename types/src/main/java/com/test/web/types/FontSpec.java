package com.test.web.types;

public class FontSpec {
	private final String family;
	private final short size;

	public FontSpec(String family, short size) {
		
		if (family == null) {
			throw new IllegalArgumentException("family == null");
		}
	
		this.family = family;
		this.size = size;
	}
	
	protected FontSpec(FontSpec spec) {
		this(spec.family, spec.size);
	}
	
	public final String getFamily() {
		return family;
	}

	public final short getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "FontSpec [family=" + family + ", size=" + size + "]";
	}
}


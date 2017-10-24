package com.test.web.layout;

import com.test.web.types.FontSpec;

public class FontKey extends FontSpec {

	private final short style;

	public FontKey(FontSpec spec, short style) {
		super(spec);

		this.style = style;
	}
	
	public FontKey(String family, String name, short size, short style) {
		super(family, name, size);
		
		this.style = style;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + style;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontKey other = (FontKey) obj;
		if (style != other.style)
			return false;
		return true;
	}
}

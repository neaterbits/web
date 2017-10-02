package com.test.web.layout;

public class FontKey {

	private final String family;
	private final String name;
	private final short size;
	private final short style;

	public FontKey(String family, String name, short size, short style) {
		super();
		this.family = family;
		this.name = name;
		this.size = size;
		this.style = style;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + size;
		result = prime * result + style;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontKey other = (FontKey) obj;
		if (family == null) {
			if (other.family != null)
				return false;
		} else if (!family.equals(other.family))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size != other.size)
			return false;
		if (style != other.style)
			return false;
		return true;
	}
}

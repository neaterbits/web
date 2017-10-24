package com.test.web.types;

public class FontSpec {
	private final String family;
	private final String name;
	private final short size;

	public FontSpec(String family, String name, short size) {
		this.family = family;
		this.name = name;
		this.size = size;
	}
	
	protected FontSpec(FontSpec spec) {
		this(spec.family, spec.name, spec.size);
	}
	
	public final String getFamily() {
		return family;
	}

	public final String getName() {
		return name;
	}

	public final short getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + size;
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
		FontSpec other = (FontSpec) obj;
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
		return true;
	}
}

package com.test.web.document._long;

public enum HTMLDropzone {

	COPY("copy"),
	MOVE("move"),
	LINK("link");
	
	
	private final String name;

	private HTMLDropzone(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

package com.test.web.document.html.common.enums;

import com.neaterbits.util.IEnum;

public enum LinkRevType implements IEnum {

	ALTERNATE("alternate"),
	STYLESHEET("stylesheet"),
	START("start"),
	NEXT("next"),
	PREV("prev"),
	CONTENTS("contents"),
	INDEX("index"),
	GLOSSARY("glossary"),
	COPYRIGHT("copyright"),
	CHAPTER("chapter"),
	SECTION("section"),
	SUBSECTION("subsection"),
	APPENDIX("appendix"),
	HELP("help"),
	BOOKMARK("bookmark");
	
	private final String name;

	private LinkRevType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}

package com.test.web.layout;

import java.util.HashMap;
import java.util.Map;

import com.test.web.document.common.HTMLElement;
import com.test.web.types.FontSpec;

// Settings, default fonts for various elements
public class FontSettings {

	private static final String COURIER = "Courier";
	private static final String TIMES = "Times";
	
	private final Map<HTMLElement, FontSpec> defaultFonts;
	
	public FontSettings() {
		this.defaultFonts = new HashMap<>();

		final FontSpec defaultTextFont = new FontSpec(null, TIMES, (short)16);
		
		add(HTMLElement.DIV, defaultTextFont);
		add(HTMLElement.SPAN, defaultTextFont);
		add(HTMLElement.INPUT, defaultTextFont);
		add(HTMLElement.HTML, defaultTextFont);
		add(HTMLElement.BODY, defaultTextFont);
		add(HTMLElement.FIELDSET, defaultTextFont);
		add(HTMLElement.UL, defaultTextFont);
		add(HTMLElement.LI, defaultTextFont);
	}
	
	private void add(HTMLElement element, String name, int size) {
		add(element, new FontSpec(null, name, (short)size));
	}

	private void add(HTMLElement element, FontSpec fontSpec) {
		defaultFonts.put(element, fontSpec);
	}

	public FontSpec getFontForElement(HTMLElement element) {
		return defaultFonts.get(element);
	}
}

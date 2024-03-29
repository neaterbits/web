package com.test.web.render.html;

import java.util.HashMap;
import java.util.Map;

import com.test.web.document.html.common.HTMLElement;
import com.test.web.layout.common.IFontSettings;
import com.test.web.types.FontSpec;

// Settings, default fonts for various elements
public class FontSettings implements IFontSettings<HTMLElement> {

	private static final String COURIER = "Courier";
	private static final String TIMES = "Times";
	
	private final Map<HTMLElement, FontSpec> defaultFonts;
	
	public FontSettings() {
		this.defaultFonts = new HashMap<>();

		final FontSpec defaultTextFont = new FontSpec(TIMES, (short)16);
		
		add(HTMLElement.DIV, defaultTextFont);
		add(HTMLElement.SPAN, defaultTextFont);
		add(HTMLElement.INPUT, defaultTextFont);
		add(HTMLElement.HTML, defaultTextFont);
		add(HTMLElement.BODY, defaultTextFont);
		add(HTMLElement.FIELDSET, defaultTextFont);
		add(HTMLElement.UL, defaultTextFont);
		add(HTMLElement.LI, defaultTextFont);
		add(HTMLElement.A, defaultTextFont);
	}
	
	private void add(HTMLElement element, String family, int size) {
		add(element, new FontSpec(family, (short)size));
	}

	private void add(HTMLElement element, FontSpec fontSpec) {
		defaultFonts.put(element, fontSpec);
	}

	@Override
	public FontSpec getFontForElement(HTMLElement element) {
		return defaultFonts.get(element);
	}
}

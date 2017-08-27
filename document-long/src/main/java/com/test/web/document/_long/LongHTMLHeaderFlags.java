package com.test.web.document._long;

import com.test.web.parse.html.enums.HTMLDirection;
import com.test.web.parse.html.enums.HTMLElement;

public class LongHTMLHeaderFlags extends LongHTMLBase {
	private static final int HEADER_ELEMENT_BITS = 5;
	private static final int HEADER_DIRECTION_BITS = 2;
	private static final int HEADER_DROPZONE_BITS = 2;
	
	private static final long HEADER_TRANSLATE 			= 1 << 0;
	private static final long HEADER_SPELLCHECK 		= 1 << 1;
	private static final long HEADER_HIDDEN 			= 1 << 2;
	private static final long HEADER_DRAGGABLE 			= 1 << 3;
	private static final long HEADER_CONTENTEDITABLE 	= 1 << 4;
	private static final int HEADER_FLAG_MSB = 4;
	
	private static final int HEADER_DROPZONE_SHIFT 	= HEADER_FLAG_MSB + 1;
	private static final int HEADER_DIRECTION_SHIFT 	= HEADER_DROPZONE_SHIFT + HEADER_DROPZONE_BITS;
	private static final int HEADER_ELEMENT_SHIFT		= HEADER_DIRECTION_SHIFT + HEADER_DIRECTION_BITS;
	
	static {
		if (1 << HEADER_ELEMENT_BITS < HTMLElement.values().length) {
			throw new IllegalStateException("Not enought room for all HTML elements");
		}
		
		if (1 << HEADER_DIRECTION_BITS < HTMLDirection.values().length) {
			throw new IllegalStateException("Not enought room for " + HTMLDirection.class.getSimpleName());
		}
		
		if (1 << HEADER_DROPZONE_BITS < HTMLDropzone.values().length) {
			throw new IllegalStateException("Not enought room for " + HTMLDropzone.class.getSimpleName());
		}
		
		if (HEADER_ELEMENT_SHIFT + HEADER_ELEMENT_BITS > Integer.valueOf(31)) {
			throw new IllegalStateException("Not enough room for header");
		}
	}

	static void setHTML(
			long [] buf, int offset, 
			HTMLElement element,
			boolean contentEditable,
			HTMLDirection direction,
			boolean draggable,
			HTMLDropzone dropzone,
			boolean hidden,
			boolean spellcheck,
			boolean translate) {

		long encoded = 0;
		
		encoded |= (1L << element.ordinal()) << HEADER_ELEMENT_SHIFT;
		
		if (contentEditable) {
			encoded |= HEADER_CONTENTEDITABLE;
		}
		
		encoded |= (1L << direction.ordinal()) << HEADER_DIRECTION_SHIFT;
		
		if (draggable) {
			encoded |= HEADER_DRAGGABLE;
		}
		
		encoded |= (1L << dropzone.ordinal()) << HEADER_DROPZONE_SHIFT; 
		
		if (hidden) {
			encoded |= HEADER_HIDDEN;
		}
		
		if (spellcheck) {
			encoded |= HEADER_SPELLCHECK;
		}
		
		if (translate) {
			encoded |= HEADER_TRANSLATE;
		}

		setHeader(buf, offset, encoded);
	}

	
	static void setContentEditable(long [] buf, int offset, boolean editable) {
		setFlag(buf, offset, HEADER_CONTENTEDITABLE, editable);
	}

	static void setDirection(long [] buf, int offset, HTMLDirection direction) {
		setHeaderEnumBits(buf, offset, direction, HEADER_DIRECTION_SHIFT, HEADER_DIRECTION_BITS);
	}

	static void setHTMLElement(long [] buf, int offset, HTMLElement element) {
		setHeaderEnumBits(buf, offset, element, HEADER_ELEMENT_SHIFT, HEADER_ELEMENT_BITS);
	}

	static HTMLElement getHTMLElement(long [] buf, int offset) {
		return getHeaderEnum(buf, offset, HTMLElement.class, HEADER_ELEMENT_SHIFT, HEADER_ELEMENT_BITS);
	}
}

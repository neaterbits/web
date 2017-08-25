package com.test.web.document._long;

import com.test.web.buffers.BufferUtil;
import com.test.web.parse.html.enums.HTMLDirection;
import com.test.web.parse.html.enums.HTMLElement;

/**
 * Helper class for indexing attributes for HTML elements
 *  
 * @author nhl
 *
 */

final class LongHTML extends BufferUtil {

	private static final int HTML_COMPACT = 5;
	
	/* Compact header for HTML elements.
	 * 
	 * There are container and leaf elements.
	 * Container elements may have inline text or otherwise.
	 * 
	 * All elements are either text or HTML tag
	 * For every element there is a next-index which is an integer.
	 * 
	 * 
	 * This is decided by MSB, for text the rest of that long is a next-pointer.
	 * The second long of a text-entry is reference to String buffer, so a String element is 16 bytes.
	 * 
	 * For a HTML element, first MSB is element/text bit, 31 bits are flags and as for text a 32 bit next-reference.
	 * After that comes packed content for the element, common attributes are first so can be accessed in a common way.
	 * 
	 * Element header, always present:
	 * 
	 * long header - text/html + flags + next element (for containers)
	 * int id | int class - indices into String arrays containing classes and IDs
	 * int style | short accesskey | short contextmenu - reference to a style element for inline-styles and String array ref for language ans accesss key
	 * int title | short lang | short tabindex - string reference for element title
	 * 
	 *  
	 * flags:
	 *    - element type (html, h1, span, ...)
	 *    - contenteditable
	 *    - direction (2 bits)
	 *    - draggable
	 *    - dropzone (2 bits)
	 *    - hidden
	 *    - spellcheck
	 *    - translate
	 *    
	 */
	
	private static final long FLAG_TEXT = (1 << 63);
	
	private static final int IDX_HEADER = 0;
	
	private static final int IDX_TEXT = 1; // For text elements

	// Compact-header for element
	private static final int IDX_HTML_ID_AND_CLASS = 1;
	private static final int IDX_HTML_LAST_NEXT = 2; // All elements can have last and next elements, except root element but that's just one element
	private static final int IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU = 3;
	private static final int IDX_HTML_TITLE_LANG_TABINDEX = 4;

	private static final int IDX_HTML_HEAD_TAIL = 5;
	
	private static final int END_OF_LIST_MARKER = 0xFFFFFFFF;
	
	private static final int HEADER_ELEMENT_BITS = 5;
	private static final int HEADER_DIRECTION_BITS = 2;
	private static final int HEADER_DROPZONE_BITS = 2;
	
	private static final long HEADER_TRANSLATE 			= 1 << 0;
	private static final long HEADER_SPELLCHECK 		= 1 << 1;
	private static final long HEADER_HIDDEN 			= 1 << 2;
	private static final long HEADER_DRAGGABLE 			= 1 << 3;
	private static final long HEADER_CONTENTEDITABLE 	= 1 << 4, HEADER_FLAG_MSB = HEADER_CONTENTEDITABLE;
	
	private static final long HEADER_DROPZONE_SHIFT 	= HEADER_FLAG_MSB + 1;
	private static final long HEADER_DIRECTION_SHIFT 	= HEADER_DROPZONE_SHIFT + HEADER_DROPZONE_BITS;
	private static final long HEADER_ELEMENT_SHIFT		= HEADER_DIRECTION_SHIFT + HEADER_DIRECTION_BITS;
	
	static {
		if (1 << HEADER_ELEMENT_BITS < HTMLElement.values().length) {
			throw new IllegalStateException("Not enought room for all HTML elements");
		}
		
		if (1 << HEADER_DIRECTION_BITS < HTMLElement.values().length) {
			throw new IllegalStateException("Not enought room for " + HTMLDirection.class.getSimpleName());
		}
		
		if (1 << HEADER_DROPZONE_BITS < HTMLElement.values().length) {
			throw new IllegalStateException("Not enought room for " + HTMLDropzone.class.getSimpleName());
		}
		
		if (HEADER_ELEMENT_SHIFT + HEADER_ELEMENT_BITS > Integer.valueOf(31)) {
			throw new IllegalStateException("Not enough room for header");
		}
	}

	private static long header(long [] buf, int offset) {
		return buf[offset + IDX_HEADER];
	}

	static boolean isText(long [] buf, int offset) {
		final long header = header(buf, offset);
		
		return (header & FLAG_TEXT) != 0;
	}

	static boolean isElement(long [] buf, int offset) {
		return !isText(buf, offset);
	}

	// Get buffer index of next element
	static int getParent(long [] buf, int offset) {
		return (int)(header(buf, offset + IDX_HEADER) & 0x00000000FFFFFFFFL);
	}
	
	static void setParent(long [] buf, int offset, int next) {
		long encoded = buf[offset + IDX_HEADER];
		
		encoded &= 0xFFFFFFFF00000000L;
		encoded |= unsignedIntToLong(next);
		buf[offset + IDX_HEADER] = encoded;
	}

	private static void setText(long [] buf, int offset, long text) {
		buf[offset + IDX_TEXT] = text;
	}

	static void setText(long [] buf, int offset, int next, long text) {
		buf[offset + IDX_HEADER] = FLAG_TEXT | unsignedIntToLong(next);
		setText(buf, offset, text);
	}

	static int getLower32(long [] buf, int offsetAndIdx) {
		return (int)(((buf[offsetAndIdx] & 0xFFFFFFFF00000000L)) >> 32);
	}

	static void setLower32(long [] buf, int offsetAndIdx, int next) {
		long encoded = buf[offsetAndIdx];

		encoded &= 0x00000000FFFFFFFFL;
		encoded |= unsignedIntToLong(next) << 32;
		buf[offsetAndIdx] = encoded;
	}

	static int getUpper32(long [] buf, int offsetAndIdx) {
		return (int)(buf[offsetAndIdx] & 0x00000000FFFFFFFFL);
	}

	static void setUpper32(long [] buf, int offsetAndIdx, int value) {
		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFF00000000L;
		encoded |= unsignedIntToLong(value);
		buf[offsetAndIdx] = encoded;
	}

	static int get16To32(long [] buf, int offsetAndIdx) {
		return (int)((buf[offsetAndIdx] & 0x00000000FFFF0000L) >> 16);
	}

	static void set16To32(long [] buf, int offsetAndIdx, int value) {
		
		if (value > Short.MAX_VALUE) {
			throw new IllegalArgumentException("value > Short.MAX_VALUE");
		}

		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFF0000FFFFL;
		encoded |= unsignedIntToLong(value) << 16;
		buf[offsetAndIdx] = encoded;
	}

	static int get0To16(long [] buf, int offset) {
		return (int)(buf[offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU] & 0x000000000000FFFFL);
	}

	static void set0To16(long [] buf, int offsetAndIdx, int value) {

		if (value > Short.MAX_VALUE) {
			throw new IllegalArgumentException("value > Short.MAX_VALUE");
		}

		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFFFFFF0000L;
		encoded |= unsignedIntToLong(value);
		buf[offsetAndIdx] = encoded;
	}

	static int getLast(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_LAST_NEXT);
	}

	static void setLast(long [] buf, int offset, int next) {
		setUpper32(buf, offset + IDX_HTML_LAST_NEXT, next);
	}

	static int getNext(long [] buf, int offset) {
		return getLower32(buf, offset + IDX_HTML_LAST_NEXT);
	}

	static void setNext(long [] buf, int offset, int next) {
		setLower32(buf, offset, next);
	}
	
	// IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU
	static int getStyle(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU);
	}

	static void setStyle(long [] buf, int offset, int style) {
		setUpper32(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU, style);
	}

	static int getAccessKey(long [] buf, int offset) {
		return get16To32(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU);
	}

	static void setAccessKey(long [] buf, int offset, int value) {
		
		if (value > Short.MAX_VALUE) {
			throw new IllegalArgumentException("accessKey > Short.MAX_VALUE");
		}
		
		set16To32(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU, value);
	}

	static int getContextMenu(long [] buf, int offset) {
		return get0To16(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU);
	}

	static void setContextMenu(long [] buf, int offset, int contextMenu) {
		set0To16(buf, offset + IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU, contextMenu);
	}

	// IDX_HTML_TITLE_LANG_TABINDEX
	static int getTitle(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX);
	}

	static void setTitle(long [] buf, int offset, int title) {
		setUpper32(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX, title);
	}

	static int getLang(long [] buf, int offset) {
		return get16To32(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX);
	}

	static void setLang(long [] buf, int offset, int lang) {
		set16To32(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX, lang);
	}

	static int getTabIndex(long [] buf, int offset) {
		return get0To16(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX);
	}

	static void setTabIndex(long [] buf, int offset, int tabIndex) {
		set0To16(buf, offset + IDX_HTML_TITLE_LANG_TABINDEX, tabIndex);
	}

	static int getHead(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_HEAD_TAIL);
	}

	static void setHead(long [] buf, int offset, int next) {
		setUpper32(buf, offset + IDX_HTML_HEAD_TAIL, next);
	}
	
	static void setHTML(
			long [] buf, int offset, 
			HTMLElement element,
			int id,
			int _class,
			boolean contentEditable,
			HTMLDirection direction,
			boolean draggable,
			HTMLDropzone dropzone,
			boolean hidden,
			boolean spellcheck,
			boolean translate) {

		long encoded = FLAG_TEXT;
		
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

		buf[offset + IDX_HEADER] = encoded;
		buf[offset + IDX_HTML_ID_AND_CLASS] = unsignedIntToLong(id) << 32 | unsignedIntToLong(_class); 
	}
	
	
	private static long unsignedIntToLong(int integer) {
		// TODO
		final long l = integer;
		
		if (l < 0) {
			throw new IllegalStateException("l < 0");
		}
		return l;
	}
}

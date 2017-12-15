package com.test.web.document._long;

import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.enums.HTMLDirection;
import com.test.web.document.common.enums.HTMLDropzone;

/**
 * Helper class for indexing attributes for HTML elements
 *  
 * @author nhl
 *
 */

final class LongHTML extends LongHTMLHeaderFlags {

	static final int SIZE_LEAF_ELEMENT = 6;
	static final int SIZE_CONTAINER_ELEMENT = 6;
	static final int SIZE_TEXT = 3;
	
	
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
	
	
	
	private static final int IDX_TEXT = 1; // For text elements
	private static final int IDX_TEXT_LAST_NEXT = 2; // For text elements

	// Compact-header for element
	private static final int IDX_HTML_ID_AND_CLASS = 1;
	private static final int IDX_HTML_LAST_NEXT = 2; // All elements can have last and next elements, except root element but that's just one element
	private static final int IDX_HTML_STYLE_ACCESSKEY_CONTEXTMENU = 3;
	private static final int IDX_HTML_TITLE_LANG_TABINDEX = 4;

	// For container elements
	private static final int IDX_HTML_CONTAINER_HEAD_TAIL = 5, IDX_HTML_CONTAINER_LAST = IDX_HTML_CONTAINER_HEAD_TAIL;
	
	private static final int IDX_HTML_LEAF_TEXT = 5, IDX_HTML_LEAF_LAST = IDX_HTML_LEAF_TEXT;
	
	static final int END_OF_LIST_MARKER = 0xFFFFFFFF;
	
	
	static {
		if (IDX_HTML_CONTAINER_LAST >= SIZE_CONTAINER_ELEMENT) {
			throw new IllegalStateException("IDX_HTML_CONTAINER_LAST >= SIZE_CONTAINER_ELEMENT");
		}

		if (IDX_HTML_LEAF_LAST >= SIZE_LEAF_ELEMENT) {
			throw new IllegalStateException("IDX_HTML_LEAF_LAST >= SIZE_LEAF_ELEMENT");
		}
	}

	static void setText(long [] buf, int offset, long text) {
		set(buf, offset + IDX_TEXT, text);
	}

	static void setText(long [] buf, int offset, int next, long text) {
		setAsText(buf, offset, next);
		setText(buf, offset, text);
	}

	static long getText(long [] buf, int offset) {
		return buf[offset + IDX_TEXT];
	}


	static int getTextLast(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_TEXT_LAST_NEXT);
	}

	static void setTextLast(long [] buf, int offset, int next) {
		setUpper32(buf, offset + IDX_TEXT_LAST_NEXT, next);
	}

	static int getTextNext(long [] buf, int offset) {
		return getLower32(buf, offset + IDX_TEXT_LAST_NEXT);
	}

	static void setTextNext(long [] buf, int offset, int next) {
		setLower32(buf, offset, next);
	}

	static int getElementLast(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_TEXT_LAST_NEXT);
	}

	static void setElementLast(long [] buf, int offset, int next) {
		setUpper32(buf, offset + IDX_HTML_LAST_NEXT, next);
	}

	static int getElementNext(long [] buf, int offset) {
		return getLower32(buf, offset + IDX_HTML_LAST_NEXT);
	}

	static void setElementNext(long [] buf, int offset, int next) {
		setLower32(buf, offset + IDX_HTML_LAST_NEXT, next);
	}

	static void setId(long [] buf, int offset, int value) {
		setUpper32(buf, offset + IDX_HTML_ID_AND_CLASS, value);
	}

	static void setClass(long [] buf, int offset, int value) {
		setLower32(buf, offset + IDX_HTML_ID_AND_CLASS, value);
	}

	static int getId(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_ID_AND_CLASS);
	}

	static int getClass(long [] buf, int offset) {
		return getLower32(buf, offset + IDX_HTML_ID_AND_CLASS);
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
		return getUpper32(buf, offset + IDX_HTML_CONTAINER_HEAD_TAIL);
	}

	static void setHead(long [] buf, int offset, int next) {
		setUpper32(buf, offset + IDX_HTML_CONTAINER_HEAD_TAIL, next);
	}
	
	static int getTail(long [] buf, int offset) {
		
		
		final int ret = getLower32(buf, offset + IDX_HTML_CONTAINER_HEAD_TAIL);

		//System.out.println("## get tail at " + offset + IDX_HTML_HEAD_TAIL + " of " + System.identityHashCode(buf) + ": " + ret);
		
		return ret;
	}

	static void setTail(long [] buf, int offset, int next) {
		setLower32(buf, offset + IDX_HTML_CONTAINER_HEAD_TAIL, next);

		//System.out.println("## set tail at " + offset + IDX_HTML_HEAD_TAIL + " of " + System.identityHashCode(buf) + ": " + getTail(buf, offset));
	}
	
	static int getLeafText(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_HTML_LEAF_TEXT);
	}

	static void setLeafText(long [] buf, int offset, int textElement) {
		setUpper32(buf, offset + IDX_HTML_LEAF_TEXT, textElement);
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

		setHTML(buf, offset, element, contentEditable, direction, draggable, dropzone, hidden, spellcheck, translate);

		buf[offset + IDX_HTML_ID_AND_CLASS] = unsignedIntToLong(id) << 32 | unsignedIntToLong(_class); 
	}
	
	// Other tags
	private static final int IDX_SCRIPT_TYPE = SIZE_LEAF_ELEMENT;
	private static final int SIZE_SCRIPT_ELEMENT = SIZE_LEAF_ELEMENT + 1;

	static void setScriptType(long [] buf, int offset, int type) {
		setUpper32(buf, offset + IDX_SCRIPT_TYPE, type);
	}

	static int getScriptType(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_SCRIPT_TYPE);
	}

	private static final int IDX_LINK_REL_TYPE = SIZE_LEAF_ELEMENT;
	private static final int IDX_LINK_HREF = SIZE_LEAF_ELEMENT + 1;
	private static final int SIZE_LINK_ELEMENT = SIZE_LEAF_ELEMENT + 2;

	static void setLinkRel(long [] buf, int offset, int rel) {
		setUpper32(buf, offset + IDX_LINK_REL_TYPE, rel);
	}

	static int getLinkRel(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_LINK_REL_TYPE);
	}

	static void setLinkType(long [] buf, int offset, int type) {
		setLower32(buf, offset + IDX_LINK_REL_TYPE, type);
	}

	static int getLinkType(long [] buf, int offset) {
		return getLower32(buf, offset + IDX_LINK_REL_TYPE);
	}

	static void setLinkHRef(long [] buf, int offset, int rel) {
		setUpper32(buf, offset + IDX_LINK_HREF, rel);
	}

	static int getLinkHRef(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_LINK_HREF);
	}

	private static final int IDX_IMAGE_URL = SIZE_LEAF_ELEMENT ;
	private static final int SIZE_IMAGE_ELEMENT = SIZE_LEAF_ELEMENT + 1;

	static void setImgUrl(long [] buf, int offset, int rel) {
		setUpper32(buf, offset + IDX_IMAGE_URL, rel);
	}

	static int getImgUrl(long [] buf, int offset) {
		return getUpper32(buf, offset + IDX_IMAGE_URL);
	}
	
	static int elementSize(HTMLElement element) {
		final int ret;

		switch (element) {
		case SCRIPT:
			ret = LongHTML.SIZE_SCRIPT_ELEMENT;
			break;

		case LINK:
			ret = LongHTML.SIZE_LINK_ELEMENT;
			break;

		case IMG:
			ret = LongHTML.SIZE_IMAGE_ELEMENT;
			break;
			
		default:
			ret = element.isElemOrTextContainerElement()
				? LongHTML.SIZE_CONTAINER_ELEMENT
				: LongHTML.SIZE_LEAF_ELEMENT;
			break;
			
		}
		
		return ret;
	}
	
}

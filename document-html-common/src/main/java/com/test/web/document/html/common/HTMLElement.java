package com.test.web.document.html.common;

import com.test.web.css.common.enums.CSSDisplay;

/**
 * All known HTML elements
 * 
 * @author nhl
 *
 */

public enum HTMLElement {

    HTML(   "html",     Content.ELEMS_OR_TEXT,  Place.ROOT, CSSDisplay.BLOCK,   HTMLAttribute.XMLNS, HTMLAttribute.XML_LANG),
    HEAD(   "head",     Content.ELEMS_OR_TEXT,  Place.HTML, null),
    BODY(   "body",     Content.ELEMS_OR_TEXT,  Place.HTML, null),
    TITLE(  "title",    Content.TEXT,           Place.HEAD, null),
    META(   "meta",     Content.NONE,           Place.HEAD, null,               HTMLAttribute.CHARSET, HTMLAttribute.CONTENT, HTMLAttribute.HTTP_EQUIV, HTMLAttribute.NAME, HTMLAttribute.SCHEME),
    LINK(   "link",     Content.NONE,           Place.HEAD, null,               HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA, HTMLAttribute.REV),
    SCRIPT( "script",   Content.SCRIPT,         Place.BOTH, null,               HTMLAttribute.TYPE),
    STYLE(  "style",    Content.STYLING,        Place.BOTH, null,               HTMLAttribute.TYPE, HTMLAttribute.MEDIA, HTMLAttribute.SCOPED),
    DIV(    "div",      Content.ELEMS_OR_TEXT,  Place.BODY, CSSDisplay.BLOCK),
    SPAN(   "span",     Content.ELEMS_OR_TEXT,  Place.BODY, CSSDisplay.INLINE),
    INPUT(  "input",    Content.NONE,           Place.BODY, CSSDisplay.INLINE),
    FIELDSET("fieldset", Content.ELEMS,         Place.BODY, null),
    UL(     "ul",       Content.ELEMS,          Place.BODY, null),
    LI(     "li",       Content.ELEMS_OR_TEXT,  Place.BODY, null),
    
    A(      "a",        Content.ELEMS_OR_TEXT,  Place.BODY, CSSDisplay.INLINE,  HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA, HTMLAttribute.REV),
    AREA(   "area",     Content.NONE,           Place.MAP,  null,               HTMLAttribute.REL, HTMLAttribute.TYPE, HTMLAttribute.HREF, HTMLAttribute.HREFLANG, HTMLAttribute.MEDIA),
    IMG(    "img",      Content.NONE,           Place.BODY, CSSDisplay.INLINE),
    
    PROGRESS("progress", Content.ELEMS_OR_TEXT, Place.BODY, CSSDisplay.INLINE,  HTMLAttribute.MAX, HTMLAttribute.VALUE)

    ;
    
    private final String name;
    private final Content content;
    private final Place placement;
    private final CSSDisplay defaultDisplay;
    private final HTMLAttribute [] attributes;
    
    private HTMLElement(String name, Content content, Place placement, CSSDisplay defaultDisplay, HTMLAttribute ... attributes) {
        this.name = name;
        this.content = content;
        this.placement = placement;
        this.defaultDisplay = defaultDisplay;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public HTMLAttribute[] getAttributes() {
        return attributes;
    }
    
    public boolean isElemOrTextContainerElement() {
        return content.isElemOrTextContainer();
    }
    
    public boolean hasAnyContent() {
        return content.hasAnyContent();
    }
    
    public Place getPlacement() {
        return placement;
    }

    public CSSDisplay getDefaultDisplay() {
        return defaultDisplay;
    }
    
    public boolean isLayoutElement() {
        return defaultDisplay != null;
    }
}

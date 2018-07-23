package com.test.web.document.html.common.enums;

import com.test.web.types.IEnum;
import com.test.web.types.Status;

/**
 * List of all events that may occur, with their event handler names and so forth
 * @author nhl
 *
 */

public enum HTMLEvent implements IEnum {
    
    
    ABORT               ("abort",   "   onabort",               Status.OK),
    DOM_CONTENT_LOADED  ("DOMContentLoaded", null,              Status.OK),
    AFTERPRINT          ("afterprint",  null,                   Status.OK),
    AFTERSCRIPTEXECUTE  ("afterscriptexecute", null,            Status.OK),
    BEFOREPRINT         ("beforeprint", null,                   Status.OK),
    BEFORESCRIPTEXECUTE ("beforescriptexecute", null,           Status.OK),
    BEFOREUNLOAD        ("beforeunload", null,                  Status.OK),
    ANIMATION_CANCEL    (null,          "onanimationcancel",    Status.OK),
    ANIMATION_ITERATION (null,          "onanimationiteration", Status.EXPERIMENTAL),
    AUXCLICK            (null,          "onauxclick",           Status.EXPERIMENTAL),
    BLUR                ("blur",        "onblur",               Status.OK),
    CANCEL              ("cancel",      null,                   Status.OK),
    CHANGE              ("change",      "onchange",             Status.OK),
    CLICK               ("click",       "onclick",              Status.OK),
    CLOSE               ("close",       "onclose",              Status.OK),
    CONNECT             ("connect",     null,                   Status.OK),
    CONTEXTMENU         ("contextmenu", "oncontextmenu",        Status.OK),
    DBLCLICK            (null,          "ondblclick",           Status.OK),
    ERROR               ("error",       "onerror",              Status.OK),
    FOCUS               ("focus",       "onfocus",              Status.OK),
    HASHCHANGE          ("hashchange",  null,                   Status.OK),
    ONGOTPOINTERCAPTURE (null,          "ongotpointercapture",  Status.OK),
    INPUT               ("input",       "oninput",              Status.OK),
    INVALID             ("invalid",     null,                   Status.OK),
    KEYDOWN             (null,          "onkeydown",            Status.OK),
    KEYPRESS            (null,          "onkeypress",           Status.OK),
    KEYUP               (null,          "onkeyup",              Status.OK),
    
    LANGUAGECHANGE      ("languagechange", null,                Status.OK),
    
    LOAD                ("load",        "onload",               Status.OK),  
    LOADEND             ("loadend",     "onloadend",            Status.OK),  
    LOADSTART           ("loadstart",   "onloadstart",          Status.OK),  

    ONLOSTPOINTERCAPTURE(null,          "onlostpointercapture", Status.OK),
    
    MESSAGE             ("message",     null,                   Status.OK),
    
    MOUSEDOWN           (null,          "onmousedown",          Status.OK),
    MOUSEMOVE           (null,          "onmousemove",          Status.OK),
    MOUSEOUT            (null,          "onmouseout",           Status.OK),
    MOUSEOVER           (null,          "onmouseover",          Status.OK),
    MOUSEUP             (null,          "onmouseup",            Status.OK),
    
    OFFLINE             ("offline",     null,                   Status.OK),
    ONLINE              ("online",      null,                   Status.OK),
    
    OPEN                ("open",        null,                   Status.OK),
    
    PAGEHIDE            ("pagehide",    null,                   Status.OK),
    PAGESHOW            ("pageshow",    null,                   Status.OK),
    
    POINTERCANCEL       (null,          "onpointercancel",      Status.OK),
    POINTERDOWN         (null,          "onpointerdown",        Status.OK),
    POINTERENTER        (null,          "onpointerenter",       Status.OK),
    POINTERLEAVE        (null,          "onpointerleave",       Status.OK),
    POINTERMOVE         (null,          "onpointermove",        Status.OK),
    POINTEROUT          (null,          "onpointerout",         Status.OK),
    POINTEROVER         (null,          "onpointerover",        Status.OK),
    POINTERUP           (null,          "onpointerup",          Status.OK),

    POPSTATE            ("popstate",    null,                   Status.OK),
    PROGRESS            ("progress",    null,                   Status.OK),
    
    READYSTATECHANGE    ("readystatechange", null,              Status.OK),
    RESET               ("reset",       "onreset",              Status.OK),
    RESIZE              (null,          "onresize",             Status.OK),
    SCROLL              (null,          "onscroll",             Status.OK),
    SELECT              ("select",      "onselect",             Status.OK),
    SELECTIONCHANGE     (null,          "onselectionchange",    Status.OK),
    SHOW                ("show",        null,                   Status.OK),
    SORT                ("sort",        null,                   Status.OK),
    STORAGE             ("storage",     null,                   Status.OK),
    SUBMIT              ("submit",      "onsubmit",             Status.OK),
    
    TOUCHCANCEL         (null,          "ontouchcancel",        Status.OK),
    TOUCHMOVE           (null,          "ontouchmove",          Status.OK),
    TOUCHSTART          (null,          "ontouchsttart",        Status.OK),
    
    TRANSITIONCANCEL    (null,          "ontransitioncancel",   Status.OK),
    TRANSITIONED        (null,          "ontransitioned",       Status.OK),

    WHEEL               (null,          "onwheel",              Status.OK),
    
    
    UNLOAD              ("unload",      null,                   Status.OK),
    LOADEDDATA          ("loadeddata",  null,                   Status.OK),
    LOADEDMETADATA      ("loadedmetadata", null,                Status.OK),
    
    CANPLAY             ("canplay",     null,                   Status.OK),
    PLAYING             ("playing",     null,                   Status.OK),
    PLAY                ("play",        null,                   Status.OK),
    CANPLAYTHROUGH      ("canplaythrough", null,                Status.OK),
    
    SEEKED              ("seeked",      null,                   Status.OK),
    SEEKING             ("seeking",     null,                   Status.OK),
    STALLED             ("stalled",     null,                   Status.OK),
    SUSPEND             ("suspend",     null,                   Status.OK),
    TIMEUPDATE          ("timeupdate",  null,                   Status.OK),
    VOLUMECHANGE        ("volumechange", null,                  Status.OK),
    WAITING             ("waiting",     null,                   Status.OK),
    DURATIONCHANGE      ("durationchange", null,                Status.OK),
    EMPTIED             ("emptied",     null,                   Status.OK),
    UNHANDLEDREJECTION  ("unhandledrejection", null,            Status.OK),
    REJECTIONHANDLED    ("rejectionhandled", null,              Status.OK)
    
    
    ;

    
    
    
    
    private final String event;
    
    private final String handler;
    private final Status status;
    
    private HTMLEvent(String event, String handler, Status status) {
        this.event = event;
        this.handler = handler;
        this.status = status;
    }

    @Override
    public String getName() {
        return event;
    }
}

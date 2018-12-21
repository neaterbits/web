package com.test.web.document.html.common.enums;

import com.test.web.types.IEnum;
import com.test.web.types.Status;

/**
 * List of all events that may occur, with their event handler names and so forth
 * 
 * See 
 * 
 * @author nhl
 *
 */

public enum HTMLEvent implements IEnum {
    
    // Resource events
    ABORT               ("abort",       "onabort",              false, false, DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.ELEMENT),
    CACHED              ("cached",      null,                   false, false, DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.APPLICATION_CACHE),
    ERROR_RESOURCE      ("error",       "onerror",              false, false, DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.ELEMENT),
    LOAD                ("load",        "onload",               false, false, DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.WINDOW, EventTarget.DOCUMENT, EventTarget.ELEMENT),  
    BEFOREUNLOAD        ("beforeunload", null,                  false, true,  DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.DEFAULT_VIEW),
    UNLOAD              ("unload",       null,                  false, false, DefaultAction.NONE, EventCategory.RESOURCE,   Status.OK,  EventTarget.DEFAULT_VIEW, EventTarget.DOCUMENT, EventTarget.ELEMENT),

    // Network events
    ONLINE              ("online",      null,                   false, false, DefaultAction.NONE, EventCategory.NETWORK,    Status.OK,  EventTarget.DEFAULT_VIEW),
    OFFLINE             ("offline",     null,                   false, false, DefaultAction.NONE, EventCategory.NETWORK,    Status.OK,  EventTarget.DEFAULT_VIEW),

    // Focus events
    FOCUS               ("focus",       "onfocus",              false, false, DefaultAction.NONE, EventCategory.FOCUS,      Status.OK,  EventTarget.ELEMENT),
    BLUR                ("blur",        "onblur",               false, false, DefaultAction.NONE, EventCategory.FOCUS,      Status.OK,  EventTarget.ELEMENT),
    
    // Websocket events
    OPEN                ("open",        null,                   false, false, DefaultAction.NONE, EventCategory.WEBSOCKET,  Status.OK,  EventTarget.WEBSOCKET),   
    MESSAGE             ("message",     null,                   false, false, DefaultAction.NONE, EventCategory.WEBSOCKET,  Status.OK,  EventTarget.WEBSOCKET),
    ERROR_WS            ("error",       null,                   false, false, DefaultAction.NONE, EventCategory.WEBSOCKET,  Status.OK,  EventTarget.WEBSOCKET),
    CLOSE_WS            ("close",       null,                   false, false, DefaultAction.NONE, EventCategory.WEBSOCKET,  Status.OK,  EventTarget.WEBSOCKET),

    // Session history events
    PAGEHIDE            ("pagehide",    null,                   false, false, DefaultAction.NONE, EventCategory.SESSION_HISTORY, Status.OK, EventTarget.DOCUMENT_DISPATCHED_ON_WINDOW),
    PAGESHOW            ("pageshow",    null,                   false, false, DefaultAction.NONE, EventCategory.SESSION_HISTORY, Status.OK, EventTarget.DOCUMENT_DISPATCHED_ON_WINDOW),
    POPSTATE            ("popstate",    null,                   true,  false, DefaultAction.NONE, EventCategory.SESSION_HISTORY, Status.OK, EventTarget.DEFAULT_VIEW),

    // CSS animation events
    ANIMATION_START     ("animationstart",      "onanimationstart",     true,  false, DefaultAction.NONE, EventCategory.ANIMATION, Status.OK,           EventTarget.DOCUMENT, EventTarget.ELEMENT, EventTarget.WINDOW),
    ANIMATION_CANCEL    ("animationcancel",     "onanimationcancel",    false, false, DefaultAction.NONE, EventCategory.ANIMATION, Status.EXPERIMENTAL, EventTarget.ANIMATION),
    ANIMATION_ITERATION ("animationiteration",  "onanimationiteration", true,  false, DefaultAction.NONE, EventCategory.ANIMATION, Status.EXPERIMENTAL, EventTarget.DOCUMENT, EventTarget.ELEMENT, EventTarget.WINDOW),
    ANIMATION_END       ("animationend",        "onanimationend",       true,  false, DefaultAction.NONE, EventCategory.ANIMATION, Status.EXPERIMENTAL, EventTarget.DOCUMENT, EventTarget.ELEMENT, EventTarget.WINDOW),
    
    // CSS transition events
    TRANSITIONSTART     ("transitionstart",     "ontransitionstart",    true,  false, DefaultAction.NONE, EventCategory.CSS_TRANSITION, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    TRANSITIONCANCEL    ("transitioncancel",    "ontransitioncancel",   true,  false, DefaultAction.NONE, EventCategory.CSS_TRANSITION, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    TRANSITIONEND       ("transitionend",       "ontransitionend",      true,  true,  DefaultAction.UNDEFINED, EventCategory.CSS_TRANSITION, Status.OK, EventTarget.ELEMENT, EventTarget.DOCUMENT, EventTarget.WINDOW),
    TRANSITIONRUN       ("transitionrun",       null,                   true,  false, DefaultAction.NONE, EventCategory.CSS_TRANSITION, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),

    // Form events
    RESET               ("reset",       "onreset",              true,  true,  DefaultAction.RESET_FORM_VALUES, EventCategory.FORM, Status.OK, EventTarget.ELEMENT),
    SUBMIT              ("submit",      "onsubmit",             true,  true,  DefaultAction.SUBMIT,            EventCategory.FORM, Status.OK, EventTarget.ELEMENT),

    // Printing events
    BEFOREPRINT         ("beforeprint", null,                   false, false, DefaultAction.NONE,   EventCategory.PRINTING, Status.OK, EventTarget.DEFAULT_VIEW),
    AFTERPRINT          ("afterprint",  null,                   false, false, DefaultAction.NONE,   EventCategory.PRINTING, Status.OK, EventTarget.DEFAULT_VIEW),

    // Text composition events
    COMPOSITIONSTART    ("compositionstart",    null,           true,  true,  DefaultAction.NONE,   EventCategory.TEXT_COMPOSITION, Status.OK, EventTarget.ELEMENT),
    COMPOSITIONUPDATE   ("compositionupdate",   null,           true,  false, DefaultAction.NONE,   EventCategory.TEXT_COMPOSITION, Status.OK, EventTarget.ELEMENT),
    COMPOSITIONEND      ("compositionend",      null,           true,  false, DefaultAction.NONE,   EventCategory.TEXT_COMPOSITION, Status.OK, EventTarget.ELEMENT),
    
    // View events
    FULLSCREENCHANGE    ("fullscreenchange",    null,           true,  false, DefaultAction.NONE,   EventCategory.VIEW,  Status.OK, EventTarget.DOCUMENT),
    FULLSCREENERROR     ("fullscreenerror",     null,           true,  false, DefaultAction.NONE,   EventCategory.VIEW,  Status.OK, EventTarget.DOCUMENT),
    RESIZE              ("resize",          "onresize",         false, false, DefaultAction.NONE,   EventCategory.VIEW,  Status.OK, EventTarget.DEFAULT_VIEW),
    SCROLL              ("scroll",          "onscroll",         false, false, DefaultAction.NONE,   EventCategory.VIEW,  Status.OK, EventTarget.DEFAULT_VIEW, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    
    
    // Clipboard events
    CUT                 ("cut",                 null,           true,  true, DefaultAction.NONE,            EventCategory.CLIPBOARD, Status.OK, EventTarget.DEFAULT_VIEW, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    COPY                ("copy",                null,           true,  true, DefaultAction.CLIPBOARD_COPY,  EventCategory.CLIPBOARD, Status.OK, EventTarget.ELEMENT),
    PASTE               ("paste",               null,           true,  true, DefaultAction.CLIPBOARD_PASTE, EventCategory.CLIPBOARD, Status.OK, EventTarget.DEFAULT_VIEW, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    
    
    // Mouse events
    MOUSEENTER          ("mouseenter",          null,           false, false, DefaultAction.NONE,   EventCategory.MOUSE,     Status.OK, EventTarget.ELEMENT),
    MOUSEOVER           ("mouseover",           null,           true,  true,  DefaultAction.NONE,   EventCategory.MOUSE,     Status.OK, EventTarget.ELEMENT),
    MOUSEMOVE           ("mousemove",           "onmousemove",  true,  true,  DefaultAction.NONE,   EventCategory.MOUSE,     Status.OK, EventTarget.ELEMENT),
    MOUSEDOWN           ("mousedown",           "onmousedown",  true,  true,  DefaultAction.MOUSE_DOWN, EventCategory.MOUSE, Status.OK, EventTarget.ELEMENT),
    MOUSEUP             ("mouseup",             "onmouseup",    true,  true,  DefaultAction.CONTEXT_MENU, EventCategory.MOUSE, Status.OK, EventTarget.ELEMENT),
    AUXCLICK            ("auxclick",            "onauxclick",   true,  true,  DefaultAction.NONE,   EventCategory.MOUSE,    Status.EXPERIMENTAL, EventTarget.ELEMENT),
    CLICK               ("click",               "onclick",      true,  true,  DefaultAction.CLICK,  EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    DBLCLICK            ("dblclick",            "ondblclick",   true,  true,  DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    CONTEXTMENU         ("contextmenu",         "oncontextmenu", true, true,  DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    WHEEL               ("wheel",               "onwheel",      true,  true,  DefaultAction.WHEEL,  EventCategory.MOUSE,    Status.OK,  EventTarget.DEFAULT_VIEW, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    MOUSELEAVE          ("mouseleave",          null,           false, false, DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    MOUSEOUT            ("mouseout",            "onmouseout",   true,  true,  DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    SELECT              ("select",              "onselect",     true,  false, DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.ELEMENT),
    POINTERLOCKCHANGE   ("pointerlockchange",   null,           true,  false, DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.DOCUMENT),
    POINTERLOCKERROR    ("pointerlockerror",    null,           true,  false, DefaultAction.NONE,   EventCategory.MOUSE,    Status.OK,  EventTarget.DOCUMENT),

    // Drag and drop events
    DRAGSTART           ("dragstart",           "ondragstart",  true,  true,  DefaultAction.DRAG_START, EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    DRAG                ("drag",                "ondrag",       true,  true,  DefaultAction.DRAG,       EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    DRAGEND             ("dragend",             "ondragend",    true,  false, DefaultAction.DRAG_END,   EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    DRAGENTER           ("dragenter",           "ondragenter",  true,  true,  DefaultAction.DRAG_ENTER, EventCategory.DRAG_DROP, Status.OK, EventTarget.IMMEDIATE_USER_SELECTION_OR_BODY),
    DRAGOVER            ("dragover",            "ondragover",   true,  true,  DefaultAction.DRAG_OVER,  EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    DRAGLEAVE           ("dragleave",           "ondragleave",  true,  false, DefaultAction.NONE,       EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    DROP                ("drop",                "ondrop",       true,  true,  DefaultAction.DROP,       EventCategory.DRAG_DROP, Status.OK, EventTarget.DOCUMENT, EventTarget.ELEMENT),
    
    // Media events
    DURATIONCHANGE      ("durationchange",      "ondurationchange", false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    LOADEDMETADATA      ("loadedmetadata",      "onloadedmetadata", false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    LOADEDDATA          ("loadeddata",          "onloadeddata",     false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    CANPLAY             ("canplay",             "oncanplay",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    CANPLAYTHROUGH      ("canplaythrough",      "oncanplaythrough", false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    ENDED               ("ended",               "onended",          false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    EMPTIED             ("emptied",             "onemptied",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    STALLED             ("stalled",             "onstalled",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    SUSPEND             ("suspend",             "onsuspend",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    PLAY                ("play",                "onplay",           false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    PLAYING             ("playing",             "onplaying",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    PAUSE               ("pause",               "onpause",          false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    WAITING             ("waiting",             "onwaiting",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    SEEKING             ("seeking",             "onseeking",        false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    SEEKED              ("seeked",              "onseeked",         false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    RATECHANGE          ("ratechange",          "onratechange",     false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    TIMEUPDATE          ("timeupdate",          "ontimeupdate",     false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    VOLUMECHANGE        ("volumechange",        "onvolumechange",   false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.ELEMENT),
    COMPLETE            ("complete",            "oncomplete",       false, false, DefaultAction.NONE,   EventCategory.MEDIA,     Status.OK, EventTarget.OFFLINE_AUDIO_CONTEXT),
    
    AUDIOPROCESS        ("audioprocess",        null,               false, false, DefaultAction.NONE,   EventCategory.MEDIA,    Status.DEPRECATED,    EventTarget.SCRIPT_PROCESSOR_NODE)

    /*
    DOM_CONTENT_LOADED  ("DOMContentLoaded", null,              Status.OK),
    AFTERSCRIPTEXECUTE  ("afterscriptexecute", null,            Status.OK),
    BEFORESCRIPTEXECUTE ("beforescriptexecute", null,           Status.OK),
    
    CANCEL              ("cancel",      null,                   Status.OK),
    CHANGE              ("change",      "onchange",             Status.OK),
    CLOSE_WINDOW               ("close",       "onclose",              Status.OK),
    CONNECT             ("connect",     null,                   Status.OK),
    HASHCHANGE          ("hashchange",  null,                   Status.OK),
    ONGOTPOINTERCAPTURE (null,          "ongotpointercapture",  Status.OK),
    INPUT               ("input",       "oninput",              Status.OK),
    INVALID             ("invalid",     null,                   Status.OK),
    KEYDOWN             (null,          "onkeydown",            Status.OK),
    KEYPRESS            (null,          "onkeypress",           Status.OK),
    KEYUP               (null,          "onkeyup",              Status.OK),
    
    LANGUAGECHANGE      ("languagechange", null,                Status.OK),
    
    LOADEND             ("loadend",     "onloadend",            Status.OK),  
    LOADSTART           ("loadstart",   "onloadstart",          Status.OK),  

    ONLOSTPOINTERCAPTURE(null,          "onlostpointercapture", Status.OK),
    
    
    
    
    
    POINTERCANCEL       (null,          "onpointercancel",      Status.OK),
    POINTERDOWN         (null,          "onpointerdown",        Status.OK),
    POINTERENTER        (null,          "onpointerenter",       Status.OK),
    POINTERLEAVE        (null,          "onpointerleave",       Status.OK),
    POINTERMOVE         (null,          "onpointermove",        Status.OK),
    POINTEROUT          (null,          "onpointerout",         Status.OK),
    POINTEROVER         (null,          "onpointerover",        Status.OK),
    POINTERUP           (null,          "onpointerup",          Status.OK),

    PROGRESS            ("progress",    null,                   Status.OK),
    
    READYSTATECHANGE    ("readystatechange", null,              Status.OK),
    SELECTIONCHANGE     (null,          "onselectionchange",    Status.OK),
    SHOW                ("show",        null,                   Status.OK),
    SORT                ("sort",        null,                   Status.OK),
    STORAGE             ("storage",     null,                   Status.OK),
    
    TOUCHCANCEL         (null,          "ontouchcancel",        Status.OK),
    TOUCHMOVE           (null,          "ontouchmove",          Status.OK),
    TOUCHSTART          (null,          "ontouchsttart",        Status.OK),
    
    TRANSITIONED        (null,          "ontransitioned",       Status.OK),

    
    
    
    
    UNHANDLEDREJECTION  ("unhandledrejection", null,            Status.OK),
    REJECTIONHANDLED    ("rejectionhandled", null,              Status.OK)
    */
    
    ;

    
    
    
    
    private final String type;
    
    private final String handler;
    private final boolean bubbles;
    private final boolean cancelable;
    private final DefaultAction defaultAction;
    private final EventCategory category;
    private final Status status;
    private final EventTarget [] targets;
    
    private HTMLEvent(String event, String handler, boolean bubbles, boolean cancelable, DefaultAction defaultAction, EventCategory category, Status status, EventTarget ... targets) {
        this.type = event;
        this.handler = handler;
        this.bubbles = bubbles;
        this.cancelable = cancelable;
        this.defaultAction = defaultAction;
        this.category = category;
        this.status = status;
        this.targets = targets;
    }
    
    public String getType() {
        return type;
    }

    public boolean getBubbles() {
        return bubbles;
    }

    public boolean getCancelable() {
        return cancelable;
    }
    
    @Override
    public String getName() {
        return type;
    }
}

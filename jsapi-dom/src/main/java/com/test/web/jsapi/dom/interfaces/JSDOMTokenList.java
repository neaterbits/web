package com.test.web.jsapi.dom.interfaces;

import java.util.Iterator;

import com.test.web.jsapi.dom.JSRef;

/**
 * To be implemented by all objects that return a DOMTokenList
 */

public interface JSDOMTokenList {
    
    // Properties
    int getLength();
    
    String getValue();
    
    // Methods
    
    JSRef<String> item(int index);
    
    boolean contains(String token);

    void add(String token);
 
    void remove(String ... tokens);
    
    boolean replace(String oldToken, String newToken);
    
    boolean supports(String token);

    boolean toggle(String token, boolean force);

    Iterator<String> entries();
    
    void foreach(JSFunction callback);

    void foreach(JSFunction callback, Object argument);
    
}

package com.test.web.jsapi.dom;

// Somthing that may be undefined
public class JSRef<T> {
    private final T ref;
    private final JSRefState state;
    
    public JSRef(T ref, JSRefState state) {
        
        if (state == null) {
            throw new IllegalArgumentException("state == null");
        }
        
        switch (state) {
        case NULL:
        case UNDEFINED:
            if (ref != null) {
                throw new IllegalArgumentException("ref != null");
            }
            break;

        case REF:
            if (ref == null) {
                throw new IllegalArgumentException("ref == null");
            }
            break;
        }
        
        this.ref = ref;
        this.state = state;
    }
}

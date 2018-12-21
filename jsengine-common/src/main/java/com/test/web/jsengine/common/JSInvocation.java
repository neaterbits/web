package com.test.web.jsengine.common;

public interface JSInvocation {

    /**
     * Call a function that was passed from the JS engine 
     * 
     * @return return value from callback
     */
    Object callFunction(Object function, Object ... parameters);
    
    Object callFunctionWithThis(Object function, Object t, Object ... parameters);

}

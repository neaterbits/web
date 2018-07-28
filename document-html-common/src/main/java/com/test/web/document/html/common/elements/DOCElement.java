package com.test.web.document.html.common.elements;

import java.util.Iterator;


public interface DOCElement<ELEMENT> {

    // Class list DOMTokenList access
    public int getClassListLength(ELEMENT element);
    
    String getClassListValue(ELEMENT element);
    
    String classListItem(ELEMENT element, int index);
    
    boolean classListContains(ELEMENT element, String token);

    void classListAdd(ELEMENT element, String token);
 
    void classListRemove(ELEMENT element, String ... tokens);
    
    boolean classListReplace(ELEMENT element, String oldToken, String newToken);
    
    boolean classListSupports(ELEMENT element, String token);

    boolean classListToggle(ELEMENT element, String token, Boolean force);

    Iterator<String> classListEntries();
    
    String getClassName(ELEMENT element);
    
    void setClassName(ELEMENT element, String className);
    
    String getElementId(ELEMENT element);
    
    void setElementId(ELEMENT element, String id);

    String getLocalName(ELEMENT element);
    
    String getNamespaceURI(ELEMENT element);
    
    String getPrefix(ELEMENT element);
    
    boolean getTabStop(ELEMENT element);
    
    String getTagName(ELEMENT element);
    
}

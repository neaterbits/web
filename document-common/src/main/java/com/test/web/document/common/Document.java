package com.test.web.document.common;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.enums.LinkRelType;

/*
 * Represents document object model for HTML
 * 
 */

public interface Document<ELEMENT>
		extends IDocumentNavigation<ELEMENT>,
					 IDocumentAttributeGetters<ELEMENT>,
					 IDocumentAttributeSetters<ELEMENT>{

	
	public static final String DEFAULT_NAMESPACE = "http://www.w3.org/1999/xhtml";
	
	HTMLElement getType(ELEMENT element);
	
	ELEMENT getElementById(String id);
	
	String getId(ELEMENT element);
	
	default String getTag(ELEMENT element) {
		return getType(element).getName();
	}
	
	String [] getClasses(ELEMENT element);
	
	ICSSDocumentStyles<ELEMENT> getStyles(ELEMENT element);
	
	List<ELEMENT> getElementsWithClass(String _class);

	int getNumElements(ELEMENT element);

	List<ELEMENT> getElementsWithType(HTMLElement type);

	// Access methods for various elements
	String getScriptType(ELEMENT element);

	LinkRelType getLinkRel(ELEMENT element);
	String getLinkType(ELEMENT element);
	String getLinkHRef(ELEMENT element);
	String getLinkHRefLang(ELEMENT element);

	String getImgUrl(ELEMENT element);

	BigDecimal getProgressMax(ELEMENT element);
	BigDecimal getProgressValue(ELEMENT element);
	
	<PARAM> void iterate(HTMLElementListener<ELEMENT, PARAM> listener, PARAM param);
	
	<PARAM> void iterateFrom(ELEMENT element, HTMLElementListener<ELEMENT, PARAM> listener, PARAM param);

	void dumpFlat(PrintStream out);
}

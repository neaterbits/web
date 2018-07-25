package com.test.web.document.html.common;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.IDocumentAttributeGetters;
import com.test.web.document.common.IDocumentAttributeSetters;
import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IDocumentNavigation;
import com.test.web.document.html.common.enums.LinkRelType;

/*
 * Represents document object model for HTML
 * 
 */

public interface IDocument<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>>
		extends IDocumentBase<ELEMENT, HTMLElement, DOCUMENT>,
					 IDocumentNavigation<ELEMENT>,
					 IDocumentAttributeGetters<ELEMENT, ATTRIBUTE>,
					 IDocumentAttributeSetters<ELEMENT, ATTRIBUTE>{

	
	public static final String DEFAULT_NAMESPACE = "http://www.w3.org/1999/xhtml";
	
	boolean isSameElement(ELEMENT element1, ELEMENT element2);
	
	ELEMENT getElementById(String id);
	
	String getId(ELEMENT element);
	
	default String getTag(ELEMENT element) {
		return getType(element).getName();
	}

	@Override
	default boolean isLayoutElement(HTMLElement elementType) {
		return elementType.isLayoutElement();
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

	void dumpFlat(PrintStream out);
}

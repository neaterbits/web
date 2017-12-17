package com.test.web.layout.common;

import com.test.web.types.FontSpec;

public interface IFontSettings<ELEMENT_TYPE> {

	FontSpec getFontForElement(ELEMENT_TYPE element);

}

package com.test.web.css.common;

import java.util.ArrayList;
import java.util.List;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;

/*
 * CSS context for a document, used for figuring out CSS style when having multiple elements
 * 
 */

public class CSSContext<TARGET> {
	
	private static final MarginsSetter dimensionsSetter = new MarginsSetter();
	
	private final List<ICSSDocument<TARGET>> documents;
	
	CSSContext() {
		this.documents = new ArrayList<>();
	}

	public void addDocument(ICSSDocument<TARGET> document) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null"); 
		}
		
		if (documents.contains(document)) {
			throw new IllegalStateException("already contains document "	+ document);
		}
		
		documents.add(document);
	}
	
	public void getCSSLayoutStyles(String elementId, String elementType, String [] elementClasses, CSSLayoutStyles result) {
		
		// Loop through all documents from start to finish and apply styles
		for (ICSSDocument<TARGET> document : documents) {

			if (elementType != null) {
				applyLayoutStyles(document, CSSTarget.TAG, elementId, result);
			}
			
			if (elementClasses != null) {
				for (String elementClass : elementClasses) {
					applyLayoutStyles(document, CSSTarget.CLASS, elementClass, result);
				}
			}

			if (elementId != null) {
				applyLayoutStyles(document, CSSTarget.ID, elementId, result);
			}
		}
	}
	
	// eg for styles element
	public void applyLayoutStyles(ICSSDocument<TARGET> document, TARGET target, CSSLayoutStyles result) {
		// Apply dimensions from document
		final CSSDisplay display = document.getDisplay(target);
		final CSSPosition position = document.getPosition(target);
		final CSSFloat cssFloat = document.getFloat(target);
		
		final int positionLeft = document.getLeft(target);
		final CSSUnit positionLeftUnit = document.getLeftUnit(target);
					
		final int positionTop = document.getTop(target);
		final CSSUnit positionTopUnit = document.getTopUnit(target);

		final int width = document.getWidth(target);
		final CSSUnit widthUnit = document.getWidthUnit(target);
		
		final int height = document.getHeight(target);
		final CSSUnit heightUnit = document.getHeightUnit(target);

		final short zIndex = document.isSet(target, CSStyle.Z_INDEX)
			? document.getZIndex(target)
			: (short)0;

		result.init(display, position, cssFloat,
				positionLeft, positionLeftUnit, positionTop, positionTopUnit,
				width, widthUnit, height, heightUnit,
				zIndex);
		
		document.getMargins(target, dimensionsSetter, result.getMargins());
		document.getPadding(target, dimensionsSetter, result.getPadding());
	}
		

	private void applyLayoutStyles(ICSSDocument<TARGET> document, CSSTarget cssTarget, String targetName, CSSLayoutStyles result) {
		final TARGET target = document.get(cssTarget, targetName);

		if (target != null) {
			applyLayoutStyles(document, target, result);
		}
	}
	
	private static class MarginsSetter implements ICSSJustify<CSSDimensions> {

		@Override
		public void set(
				CSSDimensions param,
				int top, CSSUnit topUnit, CSSJustify topType,
				int right, CSSUnit rightUnit, CSSJustify rightType,
				int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
				int left, CSSUnit leftUnit, CSSJustify leftType) {

			param.merge(
					top, topUnit, topType,
					right, rightUnit, rightType,
					bottom, bottomUnit, bottomType,
					left, leftUnit, leftType);
		}
	}
}
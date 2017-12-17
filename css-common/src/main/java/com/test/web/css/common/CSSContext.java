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
import com.test.web.layout.common.StyleDimensions;
import com.test.web.layout.common.ILayoutStylesSetters;
import com.test.web.layout.common.LayoutStyles;
import com.test.web.types.FontSpec;

/*
 * CSS context for a document, used for figuring out CSS style when having multiple elements
 * 
 */

public class CSSContext<TARGET> {
	
	private static final MarginsSetter dimensionsSetter = new MarginsSetter();
	
	private final List<ICSSDocument<TARGET>> documents;
	
	public CSSContext() {
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
	
	public void getCSSLayoutStyles(
			CSSDisplay defaultDisplay,
			FontSpec defaultFont,
			String elementId,
			String elementType,
			String [] elementClasses,
			ILayoutStylesSetters result) {
		
		if (defaultDisplay != null) {
			result.setDisplay(defaultDisplay.getLayoutDisplay());
		}
		
		if (defaultFont != null) {
			result.setFont(defaultFont);
		}

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
	
	// eg for styles attribute within any html tag
	public void applyLayoutStyles(ICSSDocumentStyles<TARGET> document, TARGET target, ILayoutStylesSetters result) {
		
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

		result.merge(display.getLayoutDisplay(), position.getLayoutPosition(), cssFloat.getLayoutFloat(),
				
				null, // TODO: font from CSS document
				
				positionLeft, positionLeftUnit.getLayoutUnit(), positionTop, positionTopUnit.getLayoutUnit(),
				width, widthUnit.getLayoutUnit(), height, heightUnit.getLayoutUnit(),
				zIndex);
		
		document.getMargins(target, dimensionsSetter, result.getMargins());
		document.getPadding(target, dimensionsSetter, result.getPadding());
	}
		

	private void applyLayoutStyles(ICSSDocument<TARGET> document, CSSTarget cssTarget, String targetName, ILayoutStylesSetters result) {
		final List<TARGET> targets = document.get(cssTarget, targetName);

		if (targets != null) {
			
			for (TARGET target : targets) {
				applyLayoutStyles(document, target, result);
			}
		}
	}
	
	private static class MarginsSetter implements ICSSJustify<StyleDimensions> {

		@Override
		public void set(
				StyleDimensions param,
				int top, CSSUnit topUnit, CSSJustify topType,
				int right, CSSUnit rightUnit, CSSJustify rightType,
				int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
				int left, CSSUnit leftUnit, CSSJustify leftType) {

			if (param == null) {
				throw new IllegalArgumentException("param == null");
			}

			param.merge(
					top, topUnit.getLayoutUnit(), topType.getLayoutJustify(),
					right, rightUnit.getLayoutUnit(), rightType.getLayoutJustify(),
					bottom, bottomUnit.getLayoutUnit(), bottomType.getLayoutJustify(),
					left, leftUnit.getLayoutUnit(), leftType.getLayoutJustify());
		}
	}
}

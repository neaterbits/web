package com.test.web.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.test.web.ui.common.Alignment;

abstract class SWTBox extends SWTPanel {
	
	SWTBox(Composite container) {
		super(container);
	}

	static int alignmentToSWT(Alignment alignment) {
		int swt;
		
		switch (alignment) {
		case BEGINNING:
			swt = SWT.BEGINNING;
			break;
			
		case CENTER:
			swt = SWT.CENTER;
			break;
			
		case END:
			swt = SWT.END;
			break;
			
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
		
		return swt;
	}
}

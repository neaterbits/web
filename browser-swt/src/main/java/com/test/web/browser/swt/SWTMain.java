package com.test.web.browser.swt;

import org.eclipse.swt.widgets.Display;

import com.test.web.browser.common.BrowserMain;
import com.test.web.browser.common.IBrowserDocumentLoader;
import com.test.web.browser.common.OOBrowserDocumentLoader;
import com.test.web.document.oo.OOTagElement;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.swt.SWTBufferRenderFactory;
import com.test.web.render.swt.SWTTextExtent;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.swt.SWTUIFactory;

public class SWTMain {

	public static void main(String [] args) {
		
		final Display display = Display.getDefault();
		
		final IUIFactory uiFactory = new SWTUIFactory(display);
		
		final IBufferRenderFactory renderFactory = new SWTBufferRenderFactory(display);
		final ITextExtent textExtent = new SWTTextExtent(display);
		
		final IBrowserDocumentLoader<OOTagElement> documentLoader
				= new OOBrowserDocumentLoader(renderFactory, textExtent);
		
		final BrowserMain<OOTagElement> main = new BrowserMain<>(uiFactory, documentLoader);
		
		main.showStartPage();
		
		uiFactory.mainLoop();
	}
}

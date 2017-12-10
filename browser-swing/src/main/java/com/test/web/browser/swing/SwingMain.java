package com.test.web.browser.swing;

import com.test.web.browser.common.BrowserMain;
import com.test.web.browser.common.IBrowserDocumentLoader;
import com.test.web.browser.common.OOBrowserDocumentLoader;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.oo.OOTagElement;
import com.test.web.render.awt.AWTBufferRenderFactory;
import com.test.web.render.awt.AWTTextExtent;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.queue.QueueRendererFactory;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.swing.SwingUIFactory;

public class SwingMain {

	public static void main(String [] args) {
		final IUIFactory uiFactory = new SwingUIFactory();
		
		final IBufferRendererFactory renderFactory = new AWTBufferRenderFactory();
		final ITextExtent textExtent = new AWTTextExtent();
		
		final IBrowserDocumentLoader<OOTagElement, OOCSSElement> documentLoader
				= new OOBrowserDocumentLoader(new QueueRendererFactory(), renderFactory, textExtent);
		
		final BrowserMain<OOTagElement, OOCSSElement> main = new BrowserMain<>(uiFactory, documentLoader);
		
		main.showStartPage();
		
		uiFactory.mainLoop();
	}
}

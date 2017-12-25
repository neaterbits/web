package com.test.web.browser.swt;

import org.eclipse.swt.widgets.Display;

import com.test.web.browser.common.BrowserMain;
import com.test.web.browser.common.DebugListeners;
import com.test.web.browser.common.IBrowserDocumentLoader;
import com.test.web.browser.common.OOBrowserDocumentLoader;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.layout.algorithm.PrintlnLayoutDebugListener;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.html.PrintlnDisplayRendererListener;
import com.test.web.render.html.PrintlnRenderDebugListener;
import com.test.web.render.queue.PrintlnRenderQueueDebugListener;
import com.test.web.render.queue.QueueRendererFactory;
import com.test.web.render.swt.SWTBufferRenderFactory;
import com.test.web.render.swt.SWTTextExtent;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.swt.SWTUIFactory;

public class SWTMain {

	public static void main(String [] args) {
		

		final DebugListeners debugListeners = new DebugListeners(
				new PrintlnLayoutDebugListener(System.out),
				new PrintlnRenderDebugListener(System.out),
				new PrintlnRenderQueueDebugListener(System.out),
				new PrintlnDisplayRendererListener(System.out));

		final Display display = Display.getDefault();
		
		final IUIFactory uiFactory = new SWTUIFactory(display);
		
		final IBufferRendererFactory renderFactory = new SWTBufferRenderFactory(display);
		final ITextExtent textExtent = new SWTTextExtent(display);
		
		final IBrowserDocumentLoader<OOTagElement, OOAttribute, OOCSSRule> documentLoader
				= new OOBrowserDocumentLoader(
						new QueueRendererFactory(debugListeners.getRenderQueueListener()),
						renderFactory,
						textExtent,
						debugListeners);
		
		final BrowserMain<OOTagElement, OOAttribute, OOCSSRule> main = new BrowserMain<>(uiFactory, documentLoader);
		
		main.showStartPage();
		
		uiFactory.mainLoop();
	}
}

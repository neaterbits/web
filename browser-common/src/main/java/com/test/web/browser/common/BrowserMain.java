package com.test.web.browser.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.util.parse.ParserException;
import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.IDocument;
import com.test.web.ui.common.Alignment;
import com.test.web.ui.common.HBoxLayoutData;
import com.test.web.ui.common.IUICanvas;
import com.test.web.ui.common.IUIContainer;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIHBox;
import com.test.web.ui.common.IUIVBox;
import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.VBoxLayoutData;

public class BrowserMain<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT extends IDocument<HTML_ELEMENT, HTML_ATTRIBUTE, DOCUMENT>> {

	private final int defaultWindowWidth;
	private final int defaultWindowHeight;
	
	private final IUIFactory uiFactory;
	private final IBrowserDocumentLoader<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> documentLoader;
	private final List<BrowserWindow<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT>> windows;
	
	public BrowserMain(IUIFactory uiFactory, IBrowserDocumentLoader<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> documentLoader) {
		
		if (uiFactory == null) {
			throw new IllegalArgumentException("uiFactory == null");
		}
		
		if (documentLoader == null) {
			throw new IllegalArgumentException("documentLoader == null");
		}
		
		this.defaultWindowWidth = 1024;
		this.defaultWindowHeight = 768;

		this.uiFactory = uiFactory;
		this.documentLoader = documentLoader;
		this.windows = new ArrayList<>();
	}

	public BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> loadInNewWindow(URL url) {
		final BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> tab = openBrowserWindow();
		
		tab.loadURL(url);
		
		return tab;
	}
	
	private static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.01 Frameset//EN\" \"http://w3.org/TR/html4/frameset.dtd\">";
	
	public static final String HTML =
	
	DOCTYPE  + "\n" +			
	"<html>\n" +
	"<!-- a single line comment -->\n" +
	"<head>\n" +
	"  <title id=\"title_id\" class=\"title_class\">Start page</title>\n" +
	"</head>\n" +
	" <body>\n" +
	"   <div id=\"main_div\">\n" +
	"     Start page\n" +
	"   </div>\n" +
	" </body>\n" +
	"</html>";

	public BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> showInNewWindow(String html) throws ParserException {
		final BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> tab = openBrowserWindow();
		
		final CSSContext<CSS_ELEMENT> cssContext = new CSSContext<>();
		
		tab.showHTML(html, cssContext);
		
		return tab;
	}
	
	public BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> showStartPage() {
		try {
			return showInNewWindow(HTML);
		} catch (ParserException ex) {
			throw new IllegalStateException("Failed to show start page");
		}
	}
	
	private BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> openBrowserWindow() {
		final IUIWindow window = uiFactory.createWindow(
				"",
				defaultWindowWidth,
				defaultWindowHeight,
				uiWindow -> {
					windows.removeIf(browserWindow -> browserWindow.hasUIWindow(uiWindow));
					
					if (windows.isEmpty()) {
						// Last open window, exit the application
						uiFactory.exitMainLoop();
					}
				});

		// Browser URL line is a horizontal line containing url string line
		final IUIVBox vbox = window.createVBox(null);

		// The browser itself, will be created using a canvas
		final BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> tab = createBrowserTab(vbox);

		final BrowserWindow<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> browserWindow = new BrowserWindow<>(window,  tab);

		windows.add(browserWindow);
		
		window.open();
		
		return tab;
	}
	
	private BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> createBrowserTab(IUIContainer container) {
		
		final IUIHBox inputHBox = container.createHBox(new VBoxLayoutData(Alignment.BEGINNING, true));

		final IUICanvas canvas = container.createCanvas(new VBoxLayoutData(true, true));

		final BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> tab = new BrowserTab<>(canvas, documentLoader);

		inputHBox.createString(
			new HBoxLayoutData(true, false),
			changedValue -> {
			try {
				final URL url;

				url = new URL(changedValue);

				tab.loadURL(url);
			} catch (MalformedURLException e) {
				// TODO: Error dialog?
			}
		});

		return tab;
	}
}

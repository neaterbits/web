package com.test.web.loadqueue.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.neaterbits.util.io.strings.Tokenizer;
import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLElementListener;
import com.test.web.document.html.common.IDocument;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IFontSettings;
import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.layout.common.ViewPort;
import com.test.web.layout.common.page.PageLayout;
import com.test.web.layout.html.HTMLLayoutAlgorithm;
import com.test.web.layout.html.HTMLLayoutContext;
import com.test.web.layout.html.HTMLLayoutState;
import com.test.web.loadqueue.common.ILoadQueue;
import com.test.web.loadqueue.common.LoadCompletionListener;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.parse.html.IDocumentParserListener;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

/*
 * Collects dependencies, ie. CSS style sheets during parser and utilizes these to perform layout and rendering during the loading phase
 * as soon as this information is available.
 */

public class DependencyCollectingParserListener<ELEMENT, ATTRIBUTE, CSS_LISTENER_CONTEXT, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>>
			implements IHTMLParserListener<ELEMENT> {

	// Base URL of the document we are loading, in order to resolve URLs to external dependencies
	private final URL documentURL;
	
	private final IDocumentParserListener<ELEMENT, ATTRIBUTE, CSS_LISTENER_CONTEXT, DOCUMENT> delegate;
	private final ILoadQueue loadQueue;

	private final HTMLLayoutAlgorithm<ELEMENT, HTMLElement, DOCUMENT> layoutAlgorithm;

	private HTMLElement curElement;
	
	private ELEMENT elementWhereLayoutStoppedDueToLoadingDependencies;
	
	private String linkRel;
	private String linkType;
	private String linkHRef;

	private Integer imgWidth;
	private Integer imgHeight;
	private String imgSrc;

	private final CSSContext<ELEMENT> cssContext;

	private final HTMLLayoutState<ELEMENT, HTMLElement, DOCUMENT> layoutState;
	
	public DependencyCollectingParserListener(
			URL documentURL,
			IDocumentParserListener<ELEMENT, ATTRIBUTE, CSS_LISTENER_CONTEXT, DOCUMENT> delegate,
			ILoadQueue loadQueue,
			ViewPort viewPort,
			ITextExtent textExtent,
			IDelayedRendererFactory renderFactory,
			IFontSettings<HTMLElement> fontSettings,
			PageLayout<ELEMENT> pageLayout,
			HTMLElementListener<ELEMENT, ATTRIBUTE, IElementRenderLayout, DOCUMENT> renderListener,
			ILayoutDebugListener<HTMLElement> layoutDebugListener) {

		this.documentURL = documentURL;
		this.delegate = delegate;
		this.loadQueue = loadQueue;
		
		this.layoutAlgorithm = new HTMLLayoutAlgorithm<>(textExtent, renderFactory, fontSettings, layoutDebugListener);
		
		this.cssContext = new CSSContext<>();
		this.layoutState = new HTMLLayoutState<>(
				textExtent,
				viewPort,
				renderFactory,
				new HTMLLayoutContext<>(cssContext),
				pageLayout,
				renderListener,
				layoutDebugListener);
	}

	@Override
	public ELEMENT onElementStart(Tokenizer tokenizer, HTMLElement htmlElement) throws IOException {
		final ELEMENT element = delegate.onElementStart(tokenizer, htmlElement);
		
		switch (htmlElement) {
		case LINK:
			this.curElement = htmlElement;
			this.linkRel = linkType = linkHRef = null;
			break;
			
		default:
			break;
		}

		// as long as there are no stylesheets or retrieving image dimensions, we might forward directly to layout algorith and compute layout on the fly as we parse
		
		if (canProcessElements()) {
            // TODO casting hack
			layoutAlgorithm.onElementStart((DOCUMENT)delegate, element, layoutState);
		}
		
		return element;
	}

	@Override
	public ELEMENT onElementEnd(Tokenizer tokenizer, HTMLElement htmlElement) throws IOException {
		final ELEMENT elementRef = delegate.onElementEnd(tokenizer, htmlElement);
		
		if (curElement != null) {

			switch (curElement) {
			case LINK:
				
				if ("stylesheet".equalsIgnoreCase(linkRel) || "text/css".equals(linkType) && (linkHRef != null && !linkHRef.isEmpty())) {
					queueLoadingOfUrl(linkHRef, loadQueue::addStyleSheet, elementRef);
				}
				break;
				
			case IMG:
				// Image tag, may not know width or height, in that case we have to load image first before we can continue layout
				if (imgWidth  == null || imgHeight  == null) {
					// Queue loading of image
					queueLoadingOfUrl(imgSrc, loadQueue::addImageLoadingForDimensions, elementRef);
				}

				this.imgWidth = null;
				this.imgHeight = null;
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + curElement);
			}
			
			this.curElement = null;
		}
		
		if (canProcessElements()) {
		    // TODO casting hack
			layoutAlgorithm.onElementEnd((DOCUMENT)delegate, elementRef, layoutState);
		}

		return elementRef;
	}
	
	private URL getBaseURL() {
		// TODO base url tag
		final String path = documentURL.getPath();
		final int directoryStart = path.lastIndexOf('/');
		final String directory = directoryStart >=0 ? path.substring(0, directoryStart) : "/";

		final String urlString = documentURL.getProtocol() + "://" + documentURL.getAuthority() + directory;
		try {
			return new URL(urlString);
		} catch (MalformedURLException ex) {
			throw new IllegalStateException("Failed to parse URL \"" + urlString + "\"");
		}
	}
	
	private String resolveDependencyURL(String url) {
		// Sometimes URls are relative so must resolve them here
		
		final String ret;
		final String trimmed = url.trim();
		
		if (url.contains(":")) {
			// Absolute URL with scheme
			ret = trimmed;
		}
		else {
			final URL base = getBaseURL();
			// URL without scheme
			if (base == null) {
				// No base, assume local file
				ret = "file:" + trimmed;
			}
			else {
				if (trimmed.startsWith("/")) {
					// Root of base
					ret = base.getProtocol() + "://" + base.getAuthority() + trimmed;
				}
				else {
					// Append to base
					ret = base.toString() + "/" + trimmed;
				}
			}
		}
		
		return ret;
	}
	
	interface Queue {
		void queue(String url, LoadCompletionListener listener) throws IOException;
	}
	
	private void queueLoadingOfUrl(String url, Queue queueFunction, ELEMENT elementRef) throws IOException {
		// If has not already stored element
		if (elementWhereLayoutStoppedDueToLoadingDependencies != null) {
			this.elementWhereLayoutStoppedDueToLoadingDependencies = elementRef;
		}

		final String resolvedURL = resolveDependencyURL(url);
		
		queueFunction.queue(resolvedURL, () -> {
			processAnyElementsParsedWhileWaitingForDependency();
		});
	}
	
	private void processAnyElementsParsedWhileWaitingForDependency() {
		if (canProcessElements()) {
			// No more outstanding requests in load queue, so we can fast-forward layout and rendering from the point we left off
			layoutElementsFrom(elementWhereLayoutStoppedDueToLoadingDependencies);
			
			this.elementWhereLayoutStoppedDueToLoadingDependencies = null;
		}
	}
	
	private void layoutElementsFrom(ELEMENT elementRef) {
		// Must run through document from the elementRef onto end and call onElementStart and onElementEnd etc for each element
		delegate.iterateFrom(elementRef, layoutAlgorithm, layoutState);
	}
	
	private boolean canProcessElements() {
		return ! (loadQueue.hasStyleSheet() || loadQueue.hasImageLoadingForDimensions());
	}

	@Override
	public void onText(Tokenizer tokenizer, long stringRef) {
		delegate.onText(tokenizer, stringRef);
	}

	@Override
	public void onAttributeWithoutValue(Tokenizer tokenizer, HTMLAttribute attribute) {
		delegate.onAttributeWithoutValue(tokenizer, attribute);
	}

	@Override
	public void onAttributeWithValue(Tokenizer tokenizer, HTMLAttribute attribute, long stringRef, HTMLElement element) {

		delegate.onAttributeWithValue(tokenizer, attribute, stringRef, element);
		
		if (curElement != null) {
			switch (curElement) {
			case LINK:
				// Must add to load queue
				switch (attribute) {
				case REL:
					this.linkRel = tokenizer.asString(stringRef);
					break;
					
				case TYPE:
					this.linkType = tokenizer.asString(stringRef);
					break;
					
				case HREF:
					this.linkHRef = tokenizer.asString(stringRef);
					break;
					
				default:
					break;
				}
				break;
				
			case IMG:
				switch (attribute) {
				case WIDTH:
					this.imgWidth = tokenizer.asInteger(stringRef);
					break;

				case HEIGHT:
					this.imgHeight = tokenizer.asInteger(stringRef);
					break;
					
				case SRC:
					this.imgSrc = tokenizer.asString(stringRef);
					break;

				default:
					break;
					
				}
			default:
				
				throw new IllegalStateException("Unexpected elememnt " + curElement);
			}
		}
		
	}

	@Override
	public void onClassAttributeValue(Tokenizer tokenizer, long stringRef) {
		delegate.onClassAttributeValue(tokenizer, stringRef);
	}

	@Override
	public void onStyleAttributeValue(Tokenizer tokenizer, String key, String value) {
		delegate.onStyleAttributeValue(tokenizer, key, value);
	}
}

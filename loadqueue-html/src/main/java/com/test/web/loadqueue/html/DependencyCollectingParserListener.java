package com.test.web.loadqueue.html;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.Tokenizer;
import com.test.web.layout.FontSettings;
import com.test.web.layout.IElementRenderLayout;
import com.test.web.layout.LayoutAlgorithm;
import com.test.web.layout.LayoutState;
import com.test.web.layout.ViewPort;
import com.test.web.loadqueue.common.ILoadQueue;
import com.test.web.loadqueue.common.LoadCompletionListener;
import com.test.web.parse.html.HTMLParserListener;
import com.test.web.parse.html.IDocumentParserListener;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;

/*
 * Collects dependencies, ie. CSS style sheets during parser and utilizes these to perform layout and rendering during the loading phase
 * as soon as this information is available.
 */

public class DependencyCollectingParserListener<ELEMENT, TOKENIZER extends Tokenizer>
			implements HTMLParserListener<ELEMENT, TOKENIZER> {
	
	private final IDocumentParserListener<ELEMENT, TOKENIZER> delegate;
	private final ILoadQueue loadQueue;

	private final LayoutAlgorithm<ELEMENT, TOKENIZER> layoutAlgorithm;

	private final FontSettings fontSettings;
	
	// temp var for computing styles
	private final CSSLayoutStyles tempLayoutStyles;
	
	private HTMLElement curElement;
	
	private ELEMENT elementWhereLayoutStoppedDueToLoadingDependencies;
	
	private String linkRel;
	private String linkType;
	private String linkHRef;
	
	private final ViewPort viewPort;
	
	private CSSContext<ELEMENT> cssContext;

	private final LayoutState<ELEMENT>layoutState;
	
	public DependencyCollectingParserListener(
			IDocumentParserListener<ELEMENT, TOKENIZER> delegate,
			ILoadQueue loadQueue,
			ViewPort viewPort,
			ITextExtent textExtent,
			IRenderer displayRenderer,
			IBufferRenderFactory renderFactory,
			FontSettings fontSettings,
			HTMLElementListener<ELEMENT, IElementRenderLayout> renderListener) {

		this.delegate = delegate;
		this.loadQueue = loadQueue;
		this.viewPort = viewPort;
		
		this.fontSettings = fontSettings;
	
		this.layoutAlgorithm = new LayoutAlgorithm<>(textExtent, renderFactory, fontSettings, null);
		
		this.tempLayoutStyles = new CSSLayoutStyles();
		
		this.layoutState = new LayoutState<>(textExtent, viewPort, displayRenderer, cssContext, renderListener);
	}

	@Override
	public ELEMENT onElementStart(TOKENIZER tokenizer, HTMLElement htmlElement) throws IOException {
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
			layoutAlgorithm.onElementStart(delegate, element, layoutState);
		}
		
		return element;
	}

	@Override
	public ELEMENT onElementEnd(TOKENIZER tokenizer, HTMLElement htmlElement) throws IOException {
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
				cssContext.getCSSLayoutStyles(
						curElement.getDefaultDisplay(),
						fontSettings.getFontForElement(curElement),
						delegate.getId(elementRef),
						delegate.getTag(elementRef),
						delegate.getClasses(elementRef),
						tempLayoutStyles);
				
				// Add any from style attribute
				cssContext.applyLayoutStyles(delegate.getStyles(elementRef), elementRef, tempLayoutStyles);
				
				if ( ! (tempLayoutStyles.hasWidth() && tempLayoutStyles.hasWidth()) ) {
					// must load image in order to figure real width and heigth, so apply here
					queueLoadingOfUrl(delegate.getImgUrl(elementRef), loadQueue::addImageLoadingForDimensions, elementRef);
				}
				
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + curElement);
			}
			
			this.curElement = null;
		}
		
		if (canProcessElements()) {
			layoutAlgorithm.onElementEnd(delegate, elementRef, layoutState);
		}

		return elementRef;
	}
	
	interface Queue {
		void queue(String url, LoadCompletionListener listener) throws IOException;
	}
	
	private void queueLoadingOfUrl(String url, Queue queueFunction, ELEMENT elementRef) throws IOException {
		// If has not already stored element
		if (elementWhereLayoutStoppedDueToLoadingDependencies != null) {
			this.elementWhereLayoutStoppedDueToLoadingDependencies = elementRef;
		}

		queueFunction.queue(url, () -> {
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
		// Must run thorugh document from the elementRef onto end and call onElementStart and onElementEnd etc for each element
		delegate.iterateFrom(elementRef, layoutAlgorithm, layoutState);
	}
	
	private boolean canProcessElements() {
		return ! (loadQueue.hasStyleSheet() || loadQueue.hasImageLoadingForDimensions());
	}

	public void onText(TOKENIZER tokenizer, int startOffset, int endSkip) {
		delegate.onText(tokenizer, startOffset, endSkip);
	}

	public void onAttributeWithoutValue(TOKENIZER tokenizer, HTMLAttribute attribute) {
		delegate.onAttributeWithoutValue(tokenizer, attribute);
	}

	public void onAttributeWithValue(TOKENIZER tokenizer, HTMLAttribute attribute, int startOffset, int endSkip, HTMLElement element) {

		delegate.onAttributeWithValue(tokenizer, attribute, startOffset, endSkip, element);
		
		if (curElement != null) {
			switch (curElement) {
			case LINK:
				// Must add to load queue
				switch (attribute) {
				case REL:
					this.linkRel = tokenizer.asString(startOffset, endSkip);
					break;
					
				case TYPE:
					this.linkType = tokenizer.asString(startOffset, endSkip);
					break;
					
				case HREF:
					this.linkHRef = tokenizer.asString(startOffset, endSkip);
					break;
					
				default:
					break;
				}
				break;

			default:
				
				throw new IllegalStateException("Unexpected elememnt " + curElement);
			}
		}
		
	}

	public void onClassAttributeValue(TOKENIZER tokenizer, int startOffset, int endSkip) {
		delegate.onClassAttributeValue(tokenizer, startOffset, endSkip);
	}

	public void onStyleAttributeValue(TOKENIZER tokenizer, String key, String value) {
		delegate.onStyleAttributeValue(tokenizer, key, value);
	}
}

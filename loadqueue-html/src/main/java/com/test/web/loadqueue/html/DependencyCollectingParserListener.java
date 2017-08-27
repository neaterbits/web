package com.test.web.loadqueue.html;

import java.io.IOException;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.io.common.Tokenizer;
import com.test.web.loadqueue.common.ILoadQueue;
import com.test.web.parse.html.HTMLParserListener;

public class DependencyCollectingParserListener<TOKENIZER extends Tokenizer>
			implements HTMLParserListener<TOKENIZER> {
	
	private final HTMLParserListener<TOKENIZER> delegate;
	private final ILoadQueue loadQueue;

	private HTMLElement curElement;
	
	private String linkRel;
	private String linkType;
	private String linkHRef;
	
	
	public DependencyCollectingParserListener(HTMLParserListener<TOKENIZER> delegate, ILoadQueue loadQueue) {
		this.delegate = delegate;
		this.loadQueue = loadQueue;
	}

	public void onElementStart(TOKENIZER tokenizer, HTMLElement element) throws IOException {
		delegate.onElementStart(tokenizer, element);
		
		switch (element) {
		case LINK:
			this.curElement = element;
			this.linkRel = linkType = linkHRef = null;
			break;
			
		default:
			break;
		}
		
	}

	public void onElementEnd(TOKENIZER tokenizer, HTMLElement element) throws IOException {
		delegate.onElementEnd(tokenizer, element);
		
		if (curElement != null) {

			switch (curElement) {
			case LINK:
				
				if ("stylesheet".equalsIgnoreCase(linkRel) || "text/css".equals(linkType) && (linkHRef != null && !linkHRef.isEmpty())) {
					loadQueue.addStyleSheet(linkHRef);
				}
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + curElement);
					
			}
			
			this.curElement = null;
		}
		
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

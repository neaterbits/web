package com.test.web.document.oo;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.DocumentState;
import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.LoadStream;
import com.test.web.io.common.SimpleLoadStream;
import com.test.web.io.oo.OOStringBuffer;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.IParse;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.HTMLUtils;
import com.test.web.parse.html.IDocumentParserListener;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.parse.html.IHTMLStyleParserListener;
import com.test.web.parse.html.enums.HTMLDirection;

public class OOHTMLDocument implements IDocumentParserListener<OOTagElement, OOTokenizer>{

	private final List<OOTagElement> stack;

	private final DocumentState<OOTagElement> state;

	private OOTagElement rootElement;

	public static <STYLE_DOCUMENT> HTMLParser<OOTagElement, OOTokenizer, STYLE_DOCUMENT> createParser(
			OOHTMLDocument document,
			IHTMLParserListener<OOTagElement, OOTokenizer> parserListener,
			LoadStream stream,
			IParse<STYLE_DOCUMENT> parseStyleDocument) {

		final OOStringBuffer input = new OOStringBuffer(stream);
		
		final HTMLParser<OOTagElement, OOTokenizer, STYLE_DOCUMENT> parser = new HTMLParser<>(
				input,
				input,
				parserListener,
				document.getStyleParserListener(),
				parseStyleDocument);
		
		return parser;
	}
	
	public static <STYLE_DOCUMENT> OOHTMLDocument parseHTMLDocument(String html, IParse<STYLE_DOCUMENT> parseStyleDocument) throws ParserException {

		final OOHTMLDocument document = new OOHTMLDocument();
		
		final HTMLParser<OOTagElement, OOTokenizer, STYLE_DOCUMENT> parser = createParser(document, document, new SimpleLoadStream(html), parseStyleDocument);
		
		try {
			parser.parseHTMLFile();
		}
		catch (IOException ex) {
			throw new IllegalStateException("IO eception while parsing", ex);
		}
		
		return document;
	}

	
	public OOHTMLDocument() {
		
		this.stack = new ArrayList<>();

		this.state = new DocumentState<>();
	}
	
	private void pushElement(OOTagElement element) {
		
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		
		if (stack.size() == 0) {
			if (this.rootElement != null) {
				throw new IllegalStateException("this.rootElement already set");
			}
			
			this.rootElement = element;
		}
		
		stack.add(element);
	}
	
	private void popElement() {
		stack.remove(stack.size() - 1);
	}
	
	private OOTagElement getCurElement() {
		return stack.get(stack.size() - 1);
	}

	@Override
	public HTMLElement getType(OOTagElement element) {
		return element.getType();
	}

	@Override
	public OOTagElement getElementById(String id) {
		return state.getElementById(id);
	}

	@Override
	public String getId(OOTagElement element) {
		return element.getId();
	}

	@Override
	public String[] getClasses(OOTagElement element) {
		return ((OOTagElement)element).getClasses();
	}

	private static final OOHTMLStyleDocumentWrapper styleWrapper = new OOHTMLStyleDocumentWrapper();
	
	@Override
	public ICSSDocumentStyles<OOTagElement> getStyles(OOTagElement element) {
		return styleWrapper;
	}

	@Override
	public List<OOTagElement> getElementsWithClass(String _class) {
		return state.getElementsWithClass(_class);
	}

	@Override
	public int getNumElements(OOTagElement element) {
		return element.isContainer() ? ((OOContainerElement)element).getNumElements() : 1;
	}

	private void foreach(OOTagElement element, Consumer<OODocumentElement> callback) {
		if (element.isContainer()) {
			foreach((OOContainerElement)element, callback);
		}
		else {
			callback.accept(element);
		}
	}
		
	private void foreach(OOContainerElement element, Consumer<OODocumentElement> callback) {
		
		callback.accept(element);
		
		for (OODocumentElement ref = element.head; ref != null; ref = ref.next) {
			if (ref.isContainer()) {
				foreach((OOContainerElement)ref, callback);
			}
			else {
				callback.accept(ref);
			}
		}
	}
	
	@Override
	public List<OOTagElement> getElementsWithType(HTMLElement type) {
		final List<OOTagElement> elements = new ArrayList<>();

		foreach(rootElement, e -> {
			if (e.isHTML()) {
				final OOTagElement tagElement = (OOTagElement)e;

				if (tagElement.getType() == type) {
					elements.add(tagElement);
				}
			}
		});
		
		return elements;
	}

	@Override
	public String getScriptType(OOTagElement element) {
		return ((OOScript)element).getScriptType();
	}

	@Override
	public String getLinkRel(OOTagElement element) {
		return ((OOLink)element).getRel();
	}

	@Override
	public String getLinkType(OOTagElement element) {
		return ((OOLink)element).getLinkType();
	}

	@Override
	public String getLinkHRef(OOTagElement element) {
		return ((OOLink)element).getHRef();
	}

	@Override
	public String getImgUrl(OOTagElement element) {
		return ((OOImg)element).getUrl();
	}

	@Override
	public <PARAM> void iterate(HTMLElementListener<OOTagElement, PARAM> listener, PARAM param) {
		iterate(null, rootElement, listener, param, rootElement,  true);
	}
	
	@Override
	public <PARAM> void iterateFrom(OOTagElement element, HTMLElementListener<OOTagElement, PARAM> listener, PARAM param) {
		iterate(null, rootElement, listener, param, element, false);
	}
	
	private <PARAM> boolean iterate(
			OOTagElement containerElement,
			OODocumentElement curElement,
			HTMLElementListener<OOTagElement, PARAM> listener,
			PARAM param,
			OODocumentElement startCallListenerElement,
			boolean callListener) {
		
		
		if (curElement.isHTML()) {

			final OOTagElement tagElement = (OOTagElement)curElement;
			
			if (callListener) {
				listener.onElementStart(this, tagElement, param);
			}
			
			if (curElement.isContainer()) {
				final OOContainerElement container = (OOContainerElement)curElement;
			
				for (OODocumentElement ref = container.head; ref != null; ref = ref.next) {
					callListener = iterate(container, ref, listener, param, startCallListenerElement, callListener);
				}
			}
			
			if (callListener) {
				listener.onElementEnd(this, tagElement, param);
			}
			
		}
		else {
			if (callListener) {
				
				String text = ((OOTextElement)curElement).getText();
				
				text = HTMLUtils.removeNewlines(text);
				
				listener.onText(this, containerElement, text,  param);
			}
		}

		if (!callListener && curElement == startCallListenerElement) {
			// found element where we are to start call listener, call listeners for all elements after this one
			callListener = true;
		}
		
		return callListener;
	}

	@Override
	public OOTagElement onElementStart(OOTokenizer tokenizer, HTMLElement element) throws IOException {

		final OOTagElement ret;
		
		switch (element) {
		case HTML:
			ret = new OOHTMLRootElement();
			break;
			
		case HEAD:
			ret = new OOHeadElement();
			break;
			
		case LINK:
			ret = new OOLink();
			break;
			
		case STYLE:
			ret = new OOStyleElement();
			break;
			
		case SCRIPT:
			ret = new OOScript();
			break;
			
		case TITLE:
			ret = new OOTitleElement();
			break;
			
		case META:
			ret = new OOMetaTagElement();
			break;
			
		case BODY:
			ret = new OOBodyElement();
			break;
			
		case DIV:
			ret = new OODivElement();
			break;
			
		case SPAN:
			ret = new OOSpanElement();
			break;

		default:
			throw new UnsupportedOperationException("Unknown HTML element " + element);
		}

		if (stack.size() != 0) {
			final OOContainerElement cur = (OOContainerElement)getCurElement();
			
			cur.add(ret);
		}
		
		pushElement(ret);

		return ret;
	}

	@Override
	public OOTagElement onElementEnd(OOTokenizer tokenizer, HTMLElement element) throws IOException {

		final OODocumentElement curElement = getCurElement();
		
		popElement();
		
		return (OOTagElement)curElement;
	}

	@Override
	public void onText(OOTokenizer tokenizer, int startOffset, int endSkip) {
		final String text = tokenizer.asString(startOffset, endSkip);
		
		final OOTextElement element = new OOTextElement(text);
		
		final OOContainerElement cur = (OOContainerElement)getCurElement();
		
		cur.add(element);
	}

	@Override
	public void onAttributeWithoutValue(OOTokenizer tokenizer, HTMLAttribute attribute) {
		switch (attribute) {
		
		case CONTENTEDITABLE:
			getCurElement().setContentEditable(true);
			break;
			
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}
	
	private static boolean booleanValue(OOTokenizer tokenizer) {
		return tokenizer.equalsIgnoreCase("true");
	}

	@Override
	public void onAttributeWithValue(OOTokenizer tokenizer, HTMLAttribute attribute, int startOffset, int endSkip, HTMLElement element) {
		final OOTagElement ref = getCurElement();

		if (element != ref.getType()) {
			throw new IllegalStateException("Mismatch between element type and ref type");
		}
		
		switch (attribute) {
		
		case ID:
			final String idString = tokenizer.asString(startOffset, endSkip);
			
			state.addElement(idString, getCurElement());
		
			ref.setId(idString);
			break;
		
		case ACCESSKEY:
			ref.setAccessKey(tokenizer.asString(startOffset, endSkip));
			break;
			
		case CONTENTEDITABLE:
			ref.setContentEditable(booleanValue(tokenizer));
			break;
			
		case CONTEXTMENU:
			ref.setContextMenu(tokenizer.asString(startOffset, endSkip));
			break;
			
		case DIRECTION:
			ref.setDirection(tokenizer.asEnum(HTMLDirection.class, false));
			break;
			
		case TITLE:
			ref.setTitleAttribute(tokenizer.asString(startOffset, endSkip));
			break;

		case REL:
			switch (element) {
			case LINK:
				((OOLink)ref).setRel(tokenizer.asString(startOffset, endSkip));
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		// script type as string
		case TYPE:
			switch (element) {
			case SCRIPT:
				((OOScript)ref).setScriptType(tokenizer.asString(startOffset, endSkip));
				break;

			case LINK:
				((OOLink)ref).setLinkType(tokenizer.asString(startOffset, endSkip));
				break;
				
			case STYLE:
				((OOStyleElement)ref).setStyleType(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
				
			break;
			
		case HREF:
			switch (element) {
			case LINK:
				((OOLink)ref).setHRef(tokenizer.asString(startOffset, endSkip));
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case MEDIA:
			switch (element) {
			case LINK:
				((OOLink)ref).setMedia(tokenizer.asString(startOffset, endSkip));
				break;
				
			case STYLE:
				((OOStyleElement)ref).setMedia(tokenizer.asString(startOffset, endSkip));
				break;
			
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case SCOPED:
			switch (element) {
			case STYLE:
				((OOStyleElement)ref).setScoped(booleanValue(tokenizer));
				break;
			
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case REV:
			switch (element) {
			case LINK:
				// not supported in HTML 5
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case LANG:
			ref.setLang(tokenizer.asString(startOffset, endSkip));
			break;
			
		case XMLNS:
			switch (element) {
			case HTML:
				((OOHTMLRootElement)ref).setXMLNS(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case XML_LANG:
			// ignore for now
			break;
			
		case CHARSET:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setCharset(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case CONTENT:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setContent(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case HTTP_EQUIV:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setHttpEquiv(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case NAME:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setName(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case SCHEME:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setScheme(tokenizer.asString(startOffset, endSkip));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute + " for element " + element);
		}
	}

	@Override
	public void onClassAttributeValue(OOTokenizer tokenizer, int startOffset, int endSkip) {
		
		final String classString = tokenizer.asString(startOffset, endSkip);
		
		final OOTagElement element = getCurElement();
		
		state.addElementClass(classString, element);
		
		final String [] classes = element.getClasses();
		if (classes == null) {
			element.setClasses(new String [] { classString });
		}
		else {
			final String [] newClasses = Arrays.copyOf(classes, classes.length + 1);
			
			newClasses[classes.length] = classString;
			
			element.setClasses(newClasses);
		}
	}

	@Override
	public void onStyleAttributeValue(OOTokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dumpFlat(PrintStream out) {
		//throw new UnsupportedOperationException("TODO");
	}

	private static final OOStyleDocument styleParserListener = new OOStyleDocument();
	
	@Override
	public IHTMLStyleParserListener<OOTagElement, OOTokenizer> getStyleParserListener() {
		return styleParserListener;
	}
}

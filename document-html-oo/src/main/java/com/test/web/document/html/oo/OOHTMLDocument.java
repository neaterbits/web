package com.test.web.document.html.oo;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.DocumentState;
import com.test.web.document.common.IElementListener;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLElementListener;
import com.test.web.document.html.common.HTMLStringConversion;
import com.test.web.document.html.common.IDocument;
import com.test.web.document.html.common.IDocumentListener;
import com.test.web.document.html.common.enums.HTMLDirection;
import com.test.web.document.html.common.enums.HTMLDropzone;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.document.html.common.enums.LinkRevType;
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

public class OOHTMLDocument implements IDocumentParserListener<OOTagElement, OOAttribute, OOTokenizer>{

	private final IDocumentListener<OOTagElement> listener;
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
		this(null);
	}
	
	public OOHTMLDocument(IDocumentListener<OOTagElement> listener) {
		
		this.listener = listener;
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
	public LinkRelType getLinkRel(OOTagElement element) {
		return ((OOLink)element).getRel();
	}

	@Override
	public String getLinkType(OOTagElement element) {
		return ((OOLink)element).getMediaType();
	}

	@Override
	public String getLinkHRef(OOTagElement element) {
		return ((OOLink)element).getHRef();
	}

	@Override
	public String getLinkHRefLang(OOTagElement element) {
		return ((OOLink)element).getHRefLang();
	}

	@Override
	public String getImgUrl(OOTagElement element) {
		return ((OOImg)element).getUrl();
	}

	@Override
	public BigDecimal getProgressMax(OOTagElement element) {
		return ((OOProgressElement)element).getMax();
	}

	@Override
	public BigDecimal getProgressValue(OOTagElement element) {
		return ((OOProgressElement)element).getValue();
	}

	@Override
	public <PARAM> void iterate(IElementListener<OOTagElement, HTMLElement, IDocument<OOTagElement, OOAttribute>, PARAM> listener, PARAM param) {
		iterate(null, rootElement, listener, param, rootElement,  true);
	}
	
	@Override
	public <PARAM> void iterateFrom(OOTagElement element, IElementListener<OOTagElement, HTMLElement, IDocument<OOTagElement, OOAttribute>, PARAM> listener, PARAM param) {
		iterate(null, rootElement, listener, param, element, false);
	}
	
	private <PARAM> boolean iterate(
			OOTagElement containerElement,
			OODocumentElement curElement,
			IElementListener<OOTagElement, HTMLElement, IDocument<OOTagElement, OOAttribute>, PARAM> listener,
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

		case INPUT:
			ret = new OOInputElement();
			break;

		case IMG:
			ret = new OOImgElement();
			break;

		case A:
			ret = new OOAElement();
			break;
			
		case FIELDSET:
			ret = new OOFieldsetElement();
			break;
			
		case UL:
			ret = new OOUlElement();
			break;
			
		case LI:
			ret = new OOLiElement();
			break;
			
		case PROGRESS:
			ret = new OOProgressElement();
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
	public void onText(OOTokenizer tokenizer, long stringRef) {
		final String text = tokenizer.asString(stringRef);
		
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
	
	private static boolean booleanValue(OOTokenizer tokenizer, long stringRef) {
		return HTMLStringConversion.booleanValue(tokenizer.asString(stringRef));
	}

	private static String booleanMinimizable(OOTokenizer tokenizer, long stringRef) {
		return tokenizer.asString(stringRef);
	}

	private static boolean yesNoValue(OOTokenizer tokenizer, long stringRef) {
		return HTMLStringConversion.yesNoValue(tokenizer.asString(stringRef));
	}

	@Override
	public void onAttributeWithValue (OOTokenizer tokenizer, HTMLAttribute attribute, long stringRef, HTMLElement element) {
		final OOTagElement ref = getCurElement();

		if (element != ref.getType()) {
			throw new IllegalStateException("Mismatch between element type and ref type");
		}
		
		switch (attribute) {
		
		case ID:
			final String idString = tokenizer.asString(stringRef);
			
			ref.setId(idString, state);
			break;
		
		case ACCESSKEY:
			ref.setAccessKey(tokenizer.asString(stringRef));
			break;
			
		case CONTENTEDITABLE:
			ref.setContentEditable(booleanValue(tokenizer, stringRef));
			break;
			
		case CONTEXTMENU:
			ref.setContextMenu(tokenizer.asString(stringRef));
			break;
			
		case DIRECTION:
			ref.setDirection(tokenizer.asEnum(HTMLDirection.class, stringRef, false));
			break;
			
		case TITLE:
			ref.setTitleAttribute(tokenizer.asString(stringRef));
			break;

		case TRANSLATE:
			ref.setTranslate(yesNoValue(tokenizer, stringRef));
			break;

		case SPELLCHECK:
			ref.setSpellcheck(booleanValue(tokenizer, stringRef));
			break;
			
		case HIDDEN:
			ref.setHidden(booleanMinimizable(tokenizer, stringRef));
			break;

		case DRAGGABLE:
			ref.setDraggable(booleanValue(tokenizer, stringRef));
			break;
			
		case DROPZONE:
			ref.setDropzone(tokenizer.asEnum(HTMLDropzone.class, stringRef, false));
			break;
			
		case TABINDEX:
			ref.setTabindex(tokenizer.asInteger(stringRef));
			break;

		case REL:
			final LinkRelType linkRelType = tokenizer.asEnum(LinkRelType.class, stringRef, false);
			
			switch (element) {
			case LINK:
				((OOLink)ref).setRel(linkRelType);
				break;
				
			case A:
				((OOAElement)ref).setRel(linkRelType);
				break;

			case AREA:
				((OOAElement)ref).setRel(linkRelType);
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		// script type as string
		case TYPE:
			final String mediaType = tokenizer.asString(stringRef);
			
			switch (element) {
			case SCRIPT:
				((OOScript)ref).setScriptType(mediaType);
				break;

			case LINK:
				((OOLink)ref).setMediaType(mediaType);
				break;
				
			case STYLE:
				((OOStyleElement)ref).setStyleType(mediaType);
				break;

			case A:
				((OOAElement)ref).setMediaType(mediaType);
				break;

			case AREA:
				((OOAreaElement)ref).setMediaType(mediaType);
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case HREF:
			final String href = tokenizer.asString(stringRef);
			
			switch (element) {
			case LINK:
				((OOLink)ref).setHRef(href);
				break;

			case A:
				((OOAElement)ref).setHRef(href);
				break;

			case AREA:
				((OOAreaElement)ref).setHRef(href);
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case HREFLANG:
			final String hrefLang = tokenizer.asString(stringRef);

			switch (element) {
			case LINK:
				((OOLink)ref).setHRefLang(hrefLang);
				break;

			case A:
				((OOAElement)ref).setHRefLang(hrefLang);
				break;

			case AREA:
				((OOAreaElement)ref).setHRefLang(hrefLang);
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case MEDIA:
			final String media = tokenizer.asString(stringRef);

			switch (element) {
			case LINK:
				((OOLink)ref).setMedia(media);
				break;

			case STYLE:
				((OOStyleElement)ref).setMedia(media);
				break;

			case A:
				((OOAElement)ref).setMedia(media);
				break;

			case AREA:
				((OOAreaElement)ref).setMedia(media);
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;
			
		case SCOPED:
			switch (element) {
			case STYLE:
				((OOStyleElement)ref).setScoped(booleanMinimizable(tokenizer, stringRef));
				break;
			
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case REV:
			final LinkRevType linkRevType = tokenizer.asEnum(LinkRevType.class, stringRef, false);

			switch (element) {
			case LINK:
				((OOLink)ref).setRev(linkRevType);
				break;
				
			case A:
				((OOAElement)ref).setRev(linkRevType);
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case LANG:
			ref.setLang(tokenizer.asString(stringRef));
			break;
			
		case XMLNS:
			switch (element) {
			case HTML:
				((OOHTMLRootElement)ref).setXMLNS(tokenizer.asString(stringRef));
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
				((OOMetaTagElement)ref).setCharset(tokenizer.asString(stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case CONTENT:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setContent(tokenizer.asString(stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case HTTP_EQUIV:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setHttpEquiv(tokenizer.asString(stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case NAME:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setName(tokenizer.asString(stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case SCHEME:
			switch (element) {
			case META:
				((OOMetaTagElement)ref).setScheme(tokenizer.asString(stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;


		case MAX:
			switch (element) {
			case PROGRESS:
				((OOProgressElement)ref).setMax(tokenizer.asBigDecimal(stringRef));
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + ref + " for attribute " + attribute);
			}
			break;

		case VALUE:
			switch (element) {
			case PROGRESS:
				((OOProgressElement)ref).setValue(tokenizer.asBigDecimal(stringRef));
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
	public void onClassAttributeValue(OOTokenizer tokenizer, long stringRef) {
		
		final String classString = tokenizer.asString(stringRef);
		
		final OOTagElement element = getCurElement();
		
		element.addClass(classString, state);
	}

	@Override
	public void onStyleAttributeValue(OOTokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dumpFlat(PrintStream out) {
		//throw new UnsupportedOperationException("TODO");
	}

	private final OOStyleDocument styleParserListener = new OOStyleDocument();
	
	@Override
	public IHTMLStyleParserListener<OOTagElement, OOTokenizer> getStyleParserListener() {
		return styleParserListener;
	}

	// Document navigation
	@Override
	public OOTagElement getParentElement(OOTagElement element) {
		return (OOTagElement)element.parent;
	}

	
	// Attributes

	@Override
	public int getNumAttributes(OOTagElement element) {
		return element.getNumAttributes();
	}
	
	@Override
	public OOAttribute getAttributeWithName(OOTagElement element, String name) {
		return element.getAttributeWithName(name);
	}

	@Override
	public OOAttribute getAttributeWithNameNS(OOTagElement element, String namespaceURI, String localName) {
		return element.getAttributeWithNameNS(namespaceURI, localName);
	}

	@Override
	public OOAttribute getAttribute(OOTagElement element, int idx) {
		return element.getAttribute(idx);
	}

	@Override
	public String getAttributeName(OOTagElement element, OOAttribute attribute) {
		return attribute.getName();
	}

	@Override
	public String getAttributeNamespaceURI(OOTagElement element, OOAttribute attribute) {
		return attribute.getNamespaceURI();
	}

	@Override
	public String getAttributeLocalName(OOTagElement element, OOAttribute attribute) {
		return attribute.getLocalName();
	}

	@Override
	public String getAttributePrefix(OOTagElement element, OOAttribute attribute) {
		return attribute.getPrefix();
	}

	@Override
	public String getAttributeValue(OOTagElement element, OOAttribute attribute) {
		return element.getAttributeValue(attribute);
	}

	@Override
	public OOAttribute setAttributeValue(OOTagElement element, int idx, String value) {
		return triggerUIUpdates(element, element.setAttributeValue(idx, value, state));
	}
	
	@Override
	public OOAttribute setAttributeValue(OOTagElement element, OOAttribute attribute, String value) {
		return triggerUIUpdates(element, element.setAttributeValue(attribute, value, state));
	}

	@Override
	public OOAttribute setAttributeValue(OOTagElement element, String name, String value) {
		return triggerUIUpdates(element, element.setAttributeValue(name, value, state));
	}

	@Override
	public OOAttribute setAttributeValue(OOTagElement element, String namespaceURI, String localName, String value) {
		return triggerUIUpdates(element, element.setAttributeValue(namespaceURI, localName, value, state));
	}

	@Override
	public OOAttribute removeAttribute(OOTagElement element, String name) {
		return triggerUIUpdates(element, element.removeAttribute(name, state));
	}

	@Override
	public OOAttribute removeAttribute(OOTagElement element, String namespaceURI, String localName) {
		return triggerUIUpdates(element, element.removeAttribute(namespaceURI, localName, state));
	}

	private OOAttribute triggerUIUpdates(OOTagElement element, OOAttribute ooAttribute) {
		if (listener != null && ooAttribute.getStandard() != null) {
			listener.onAttributeUpdated(element, ooAttribute.getStandard());
		}
		
		return ooAttribute;
	}
}

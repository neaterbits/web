package com.test.web.document.html._long;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neaterbits.util.buffers.LongBuffersIntegerIndex;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;
import com.test.util.io.buffers.StringBuffers;
import com.test.web.css.common.ICSSDocumentStyles;
import com.test.web.document.common.DocumentState;
import com.test.web.document.common.IElementListener;
import com.test.web.document.html.common.HTMLAttribute;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.enums.HTMLDirection;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.parse.html.HTMLUtils;
import com.test.web.parse.html.IDocumentParserListener;
import com.test.web.parse.html.IHTMLStyleParserListener;
import com.test.web.types.Debug;

/**
 * Stores the read document as a series of compacted long-values.
 * This in order to perform as few allocations as possible, avoid memory use for object headers
 * and avoid GC churn.
 * 
 * All common attributes are implemented as bit-flags into buffer with a certain length
 * 
 * Downside is dynamic pages where there are a lot of updates via Javascript
 *  
 * @author nhl
 *
 */


public class LongHTMLDocument extends LongBuffersIntegerIndex

	implements IDocumentParserListener<Integer, Integer, Void, LongHTMLDocument> {

	private static final boolean CHECK_OVERWRITE = true;
	private static final boolean CHECK_IS_CONTAINER = true;

	private static final boolean CHECK_TEXT_OVERWRITE = true;

	private final StringBuffers textBuffer;
	
	// Stack for position while parsing
	private final int [] stack;
	private int depth;

	private final MapStringStorageBuffer idBuffer;
	private final MapStringStorageBuffer classBuffer;
	private final MapStringStorageBuffer accessKeyBuffer;
	private final MapStringStorageBuffer contextMenuBuffer;
	private final MapStringStorageBuffer langBuffer;
	private final MapStringStorageBuffer titleBuffer;
	private final MapStringStorageBuffer relBuffer;
	private final MapStringStorageBuffer typeBuffer;
	private final MapStringStorageBuffer hrefBuffer;

	private final LongStyleDocument styleDocument;

	private final DocumentState<Integer> state;
	
	// Array of element classes that are appended to every time we read an element
	// TODO: perhaps use a linked-list? We can only add classes here since we only stor index and count for each element, class would seldom be updated dynamically though
	
	private int [] elementClasses;
	private int numElementClasses;
	
	private int rootElement;
	
	public LongHTMLDocument(StringBuffers textBuffer) {
		
		this.textBuffer = textBuffer;
		
		this.stack = new int[100];

		this.state = new DocumentState<>();
		
		this.idBuffer = new MapStringStorageBuffer();
		this.classBuffer = new MapStringStorageBuffer();
		
		this.elementClasses = new int[100];
		this.numElementClasses = 1; // Skip initial so that default encoded class value (0) never points to an index 
		
		// Just reuse same buffer
		this.accessKeyBuffer = this.contextMenuBuffer = this.langBuffer = this.titleBuffer = this.relBuffer = this.typeBuffer = this.hrefBuffer
		        = new MapStringStorageBuffer();
		
		this.rootElement = LongHTML.END_OF_LIST_MARKER;
	
		this.styleDocument = new LongStyleDocument();
	}


	private void pushElement(int element) {
		
		if (depth == 0) {
			
			if (rootElement != LongHTML.END_OF_LIST_MARKER) {
				throw new IllegalStateException("Root element already set");
			}
			
			this.rootElement = element;
		}
		
		if (depth == stack.length) {
			throw new IllegalStateException("Reached max stack size: " + stack.length);
		}
		
		stack[depth ++] = element;
	}
	
	private int getCurElement() {
		return stack[depth - 1];
	}
	
	private void popElement() {
		if (depth == 0) {
			throw new IllegalStateException("depth == 0");
		}
		
		-- depth;
	}


	@Override
	public Integer onElementStart(Tokenizer tokenizer, HTMLElement element) {
		
		// Start of an HTML element, add to buffer and
		
		final int numLongs = LongHTML.elementSize(element);
		
		final int elementRef = allocate(numLongs, element.getName());

		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		if (LongHTML.getHeader(elementBuf, elementOffset) != 0L) {
			throw new IllegalStateException("Header not 0 at " + elementOffset);
		}

		if (element.isElemOrTextContainerElement()) {
			initHeadTail(elementBuf, elementOffset);
		}
		
		if (depth != 0) {
			final int cur = getCurElement();
			// Append to current
			final long [] curBuf = buf(cur);
			final int curOffset = offset(cur);

			appendElement(cur, curBuf, curOffset, elementRef, element);
		}
		else {
			// Root element
			LongHTML.setElementLast(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
			LongHTML.setElementNext(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
		}

		LongHTML.setHTMLElement(elementBuf, elementOffset, element);
		
		//if (element.isContainerElement()) {
			pushElement(elementRef);
		//}
			
		return elementRef;
	}

	private void initHeadTail(long [] elementBuf, int elementOffset) {
		LongHTML.setHead(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
		LongHTML.setTail(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
	}
	
	
	private void appendElement(final int cur, final long [] curBuf, final int curOffset, final int elementRef, final HTMLElement element) {
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		if (Debug.DEBUG_APPEND) {
			System.out.println("## append " + element + " at " + elementOffset + " to " + LongHTML.getHTMLElement(curBuf, curOffset) + " at " + curOffset);
		}
		
		final long mask = element != null ? 0xFFFFFFFFFFFFFFFFL : 0x7FFFFFFFFFFFFFFFL;
		
		if (CHECK_OVERWRITE && (LongHTML.getHeader(elementBuf, elementOffset) & mask) != 0L) {
			throw new IllegalStateException("Header not 0");
		}

		if (CHECK_IS_CONTAINER && !LongHTML.getHTMLElement(curBuf, curOffset).isElemOrTextContainerElement()) {
			throw new IllegalStateException("Not a container element at " + curOffset
					+ ": " + LongHTML.getHTMLElement(curBuf, curOffset)
					+ " when appending " + element);
		}
			
		// ref to tail element of list, we must update this one
		final int tail = LongHTML.getTail(curBuf, curOffset);
		
		if (tail == 0L) {
			throw new IllegalStateException("tail == 0L from " + curOffset);
		}
		
		if (tail == LongHTML.END_OF_LIST_MARKER) {
			if (Debug.DEBUG_APPEND) {
				System.out.println("## appending to empty list");
			}
			
			// Empty list, sets head and tail to this new element
			LongHTML.setHead(curBuf, curOffset, elementRef);
			LongHTML.setTail(curBuf, curOffset, elementRef);

			LongHTML.setElementLast(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
			LongHTML.setElementNext(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
		}
		else {
			final long [] tailBuf = buf(tail);
			final int tailOffset = offset(tail);

			// Set next ref of tail to point to this
			LongHTML.setElementNext(tailBuf, tailOffset, elementRef);
			
			if (Debug.DEBUG_APPEND) {
				System.out.println("## appending after " + tailOffset);
			}

			// Set last ref of this to point to tail
			LongHTML.setElementLast(elementBuf, elementOffset, tail);
			LongHTML.setElementNext(elementBuf, elementOffset, LongHTML.END_OF_LIST_MARKER);
			
			// Set list tail to point to this
			LongHTML.setTail(curBuf, curOffset, elementRef);
		}

		if (CHECK_OVERWRITE && (LongHTML.getHeader(elementBuf, elementOffset) & mask) != 0L) {
			throw new IllegalStateException("Header not 0");
		}

		LongHTML.setParent(elementBuf, elementOffset, cur);
		
		if (CHECK_OVERWRITE && (LongHTML.getHeader(elementBuf, elementOffset) & mask) != 0L) {
			throw new IllegalStateException("Header not 0");
		}
	}

	@Override
	public Integer onElementEnd(Tokenizer tokenizer, HTMLElement element) {
		
		final int cur = getCurElement();
		
		//if (element.isContainerElement()) {
			popElement();
		//}
			
		return cur;
	}

	@Override
	public void onText(Tokenizer tokenizer, long stringRef) {
		final int elementRef = allocate(LongHTML.SIZE_TEXT, "text");
		
		final int cur = getCurElement();
		// Append to current
		final long [] curBuf = buf(cur);
		final int curOffset = offset(cur);
		
		final HTMLElement curElement = LongHTML.getHTMLElement(curBuf, curOffset); 

		if (curElement.isElemOrTextContainerElement()) {
			appendElement(cur, curBuf, curOffset, elementRef, null);
		}
		else {
			// Leaf element that can contain only text, add text
			final long [] buf = buf(elementRef);
			final int offset = offset(elementRef);

			if (CHECK_TEXT_OVERWRITE && LongHTML.getLeafText(curBuf, curOffset) != 0) {
				throw new IllegalStateException("Text already set at " + curOffset);
			}
			
			LongHTML.setLeafText(curBuf, curOffset, elementRef);
			LongHTML.setParent(buf, offset, cur);
		}
		
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		LongHTML.setAsText(elementBuf, elementOffset);
		
		LongHTML.setText(elementBuf, elementOffset, stringRef);
	}

	@Override
	public void onAttributeWithoutValue(Tokenizer tokenizer, HTMLAttribute attribute) {
		
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		switch (attribute) {
		
		case CONTENTEDITABLE:
			LongHTML.setContentEditable(elementBuf, elementOffset, true);
			break;
			
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}


	@Override
	public void onAttributeWithValue(Tokenizer tokenizer, HTMLAttribute attribute, long stringRef, HTMLElement element) {
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		switch (attribute) {
		
		case ID:
			final int idStringRef = tokenizer.addToBuffer(idBuffer, stringRef);
			final String idString = idBuffer.getString(idStringRef);
			
			state.addElement(idString, getCurElement());
			
			LongHTML.setId(elementBuf, elementOffset, idStringRef);
			break;
		
		case ACCESSKEY:
			LongHTML.setAccessKey(elementBuf, elementOffset, tokenizer.addToBuffer(accessKeyBuffer, stringRef));
			break;
			
		case CONTENTEDITABLE:
			LongHTML.setContentEditable(elementBuf, elementOffset, tokenizer.equalsIgnoreCase("true", stringRef));
			break;
			
		case CONTEXTMENU:
			LongHTML.setContextMenu(elementBuf, elementOffset, tokenizer.addToBuffer(contextMenuBuffer, stringRef));
			break;
			
		case DIRECTION:
			LongHTML.setDirection(elementBuf, elementOffset, tokenizer.asEnum(HTMLDirection.class, stringRef, false));
			break;
			
		case REL:
			switch (element) {
			case LINK:
				LongHTML.setLinkRel(elementBuf, elementOffset, tokenizer.addToBuffer(relBuffer, stringRef));
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + element + " for attribute " + attribute);
			}
			break;
			
		// script type as string
		case TYPE:
			switch (element) {
			case SCRIPT:
				LongHTML.setScriptType(elementBuf, elementOffset, tokenizer.addToBuffer(typeBuffer, stringRef));
				break;

			case LINK:
				LongHTML.setLinkType(elementBuf, elementOffset, tokenizer.addToBuffer(typeBuffer, stringRef));
				break;

			default:
				throw new IllegalStateException("Unknown element " + element + " for attribute " + attribute);
			}
				
			break;
			
		case HREF:
			switch (element) {
			case LINK:
				LongHTML.setLinkHRef(elementBuf, elementOffset, tokenizer.addToBuffer(hrefBuffer, stringRef));
				break;
				
			default:
				throw new IllegalStateException("Unknown element " + element + " for attribute " + attribute);
			}
			break;
			
		default:
			throw new IllegalArgumentException("Unknown attribute " + attribute);
		}
	}

	@Override
	public void onClassAttributeValue(Tokenizer tokenizer, long stringRef) {
		final int elementRef = getCurElement();
		final int elementOffset = offset(elementRef);
		final long [] elementBuf = buf(elementRef);

		final int classStringRef = tokenizer.addToBuffer(classBuffer, stringRef);
		
		final String classString = classBuffer.getString(classStringRef);
		
		final int element = getCurElement();
		
		state.addElementClass(classString, element);
		
		final int existingClassRef = LongHTML.getClass(elementBuf, elementOffset);

		if (numElementClasses == elementClasses.length) {
			this.elementClasses = Arrays.copyOf(elementClasses, elementClasses.length * 2);
		}
		
		final int updatedClassRef;

		if (existingClassRef == 0) {
			// Not set since starts counting from 0
			updatedClassRef = numElementClasses << 16 | 1; // length 1 
		}
		else {
			final int classTableIdx = existingClassRef >> 16;
			final int classTableLength = existingClassRef & 0x0000FFFF;

			if (classTableIdx + classTableLength != numElementClasses ) {
				throw new IllegalArgumentException("Not last element");
			}
			
			updatedClassRef = (classTableIdx << 16) | (classTableLength + 1);
		}
		
		elementClasses[numElementClasses ++] = classStringRef;

		LongHTML.setClass(elementBuf, elementOffset, updatedClassRef);
	}


	@Override
	public void onStyleAttributeValue(Tokenizer tokenizer, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getElementById(String id) {
		return state.getElementById(id);
	}

	@Override
	public String getId(Integer element) {
		final int elementOffset = offset(element);
		final long [] elementBuf = buf(element);

		final int id = LongHTML.getId(elementBuf, elementOffset);
				
		return id == MapStringStorageBuffer.NONE ? null : idBuffer.getString(id);
	}
	
	@Override
	public HTMLElement getType(Integer element) {
		final int elementOffset = offset(element);
		final long [] elementBuf = buf(element);

		final HTMLElement htmlElement = LongHTML.getHTMLElement(elementBuf, elementOffset);
	
		return htmlElement;
	}


	@Override
	public String [] getClasses(Integer element) {
		final int elementOffset = offset(element);
		final long [] elementBuf = buf(element);
		
		final int classRef = LongHTML.getClass(elementBuf, elementOffset);
		
		final int numClasses = classRef & 0x0000FFFF;
		
		final String [] ret = new String[numClasses];
		
		final int startIdx = classRef >> 16;
		
		for (int i = 0; i < numClasses; ++ i) {
			ret[i] = classBuffer.getString(elementClasses[startIdx + i]);
		}
		
		return ret;
	}

	@Override
	public ICSSDocumentStyles<Integer> getStyles(Integer element) {
		return styleDocument.hasElement(element) ? styleDocument : null;
	}

	@Override
	public List<Integer> getElementsWithClass(String _class) {
		return state.getElementsWithClass(_class);
	}

	@Override
	public String getScriptType(Integer element) {
		final int ref = LongHTML.getScriptType(buf(element), offset(element));
		
		return typeBuffer.getString(ref);
	}

	@Override
	public LinkRelType getLinkRel(Integer element) {
		final int ref = LongHTML.getLinkRel(buf(element), offset(element));
		
		// TODO store in binary
		return LinkRelType.valueOf(relBuffer.getString(ref));
	}

	@Override
	public String getLinkType(Integer element) {
		final int ref = LongHTML.getLinkType(buf(element), offset(element));
		
		return typeBuffer.getString(ref);
	}

	@Override
	public String getLinkHRef(Integer element) {
		final int ref = LongHTML.getLinkHRef(buf(element), offset(element));
		
		return hrefBuffer.getString(ref);
	}

	@Override
	public String getLinkHRefLang(Integer element) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getImgUrl(Integer element) {
		final int ref = LongHTML.getImgUrl(buf(element), offset(element));
		
		return hrefBuffer.getString(ref);
	}

	@Override
	public BigDecimal getProgressMax(Integer element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getProgressValue(Integer element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumElements(Integer element) {
		// Not constant-time for now
		
		final int elementOffset = offset(element);
		final long [] elementBuf = buf(element);
		
		if (!LongHTML.isElement(elementBuf, elementOffset)) {
			throw new IllegalArgumentException("Not a HTML element: " + element);
		}
		
		int count = 0;

		for (int curElement = LongHTML.getHead(elementBuf, elementOffset);
				curElement != LongHTML.END_OF_LIST_MARKER;) {

			final long [] curElementBuf = buf(element);
			final int curElementOffset = offset(curElement);

			final int nextElement = LongHTML.getElementNext(curElementBuf, curElementOffset);

			//System.out.println("## get next of " + curElement + ": " + nextElement);

			curElement = nextElement;

			++ count;
		}
		
		return count;
	}
	
	@Override
	public <PARAM> void iterate(IElementListener<Integer, HTMLElement, LongHTMLDocument, PARAM> listener, PARAM param) {
		iterate(INITIAL_ELEMENT, listener, param, INITIAL_ELEMENT,  true);
	}
	
	@Override
	public <PARAM> void iterateFrom(Integer element, IElementListener<Integer, HTMLElement, LongHTMLDocument, PARAM> listener, PARAM param) {
		iterate(INITIAL_ELEMENT, listener, param, element, false);
	}
	
	private <PARAM> boolean iterate(
			int curElement,
			IElementListener<Integer, HTMLElement, LongHTMLDocument, PARAM> listener,
			PARAM param,
			int startCallListenerElement,
			boolean callListener) {
		
		final int elementOffset = offset(curElement);
		final long [] elementBuf = buf(curElement);

		if (LongHTML.isElement(elementBuf, elementOffset)) {

			if (callListener) {
				listener.onElementStart(this, curElement, param);
			}
			
			for (int ref = LongHTML.getHead(elementBuf, elementOffset);
					ref != LongHTML.END_OF_LIST_MARKER;
					ref = LongHTML.getElementNext(buf(ref), offset(ref))) {
				
				callListener = iterate(ref, listener, param, startCallListenerElement, callListener);
			}
			
			if (callListener) {
				listener.onElementEnd(this, curElement, param);
			}
			
		}
		else {
			if (callListener) {
				
				String text = textBuffer.getString(LongHTML.getText(elementBuf, elementOffset));
				
				text = HTMLUtils.removeNewlines(text);
				
				listener.onText(this, curElement, text,  param);
			}
		}

		if (!callListener && curElement == startCallListenerElement) {
			// found element where we are to start call listener, call listeners for all elements after this one
			callListener = true;
		}
		
		return callListener;
	}


	@FunctionalInterface
	interface LongElementVisitor {
		void onElement(int ref, long [] buf, int offset, HTMLElement type);
	}
	
	private void foreEachElement(LongElementVisitor visitor) {
		foreEachElement(INITIAL_ELEMENT, visitor);
	}

	private void foreEachElement(int element, LongElementVisitor visitor) {
		for (;;) {
			final int elementOffset = offset(element);
			final long [] elementBuf = buf(element);
			
			final int elementSize;

			if (LongHTML.isElement(elementBuf, elementOffset)) {
				final HTMLElement type = LongHTML.getHTMLElement(elementBuf, elementOffset);
				
				elementSize = LongHTML.elementSize(type);

				visitor.onElement(element, elementBuf, elementOffset, type);
			}
			else {
				elementSize = LongHTML.SIZE_TEXT;
				
				visitor.onElement(element, elementBuf, elementOffset, null);
			}
	
			int nextElement = element + elementSize;
			
			if (offset(nextElement) >= getBufferSize()) {
				// Skipped to next buffer, rount
				element = nextElement - (nextElement % getBufferSize());
			}
			else {
				element = nextElement;
			}
			
			if (element >= getWritePos()) {
				break;
			}
		}
	}
	
	@Override
	public void dumpFlat(PrintStream out) {
		dumpFlat(INITIAL_ELEMENT, out);
	}
	
	void dumpFlat(int element, PrintStream out) {
		// Start at top of buffer and iterate linearly?
		foreEachElement(element, (ref, buf, offset, type) -> dumpElement(0, buf, offset, type, out));
	}
	
	private void dumpElement(int indent, long [] elementBuf, int elementOffset, HTMLElement type, PrintStream out) {

		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < indent; ++ i) {
			sb.append("  ");
		}

		sb.append(String.format("%08x[%d] header %016x:", System.identityHashCode(elementBuf), elementOffset, LongHTML.getHeader(elementBuf, elementOffset)));

		sb.append(" ");
		
		final int elementSize;

		if (LongHTML.isElement(elementBuf, elementOffset)) {
			
			final HTMLElement htmlElement = type;
			
			sb.append("element")
			.append(" ")
			.append(htmlElement);
			
			elementSize = LongHTML.elementSize(htmlElement);
		}
		else {
			final long text = LongHTML.getText(elementBuf, elementOffset);
			
			sb.append(String.format(" text %016x: ", text)).append("\"").append(textBuffer.getString(text)).append("\"");	
			elementSize = LongHTML.SIZE_TEXT;
		}

		out.println(sb.toString());
	}


	@Override
	public List<Integer> getElementsWithType(HTMLElement type) {
		
		final List<Integer> ret = new ArrayList<>();
		
		foreEachElement((ref, buf, offset, t) -> {
			if (type == t) {
				ret.add(ref);
			}
		});
		
		// Iterate all elements and collect those of type
		return ret;
	}
	
	@Override
	public IHTMLStyleParserListener<Integer, Void> getStyleParserListener() {
		return styleDocument;
	}
	
	// Document navigation
	@Override
	public Integer getParentElement(Integer element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public boolean isSameElement(Integer element1, Integer element2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HTMLAttribute getStandard(Integer attribute) {
        // TODO Auto-generated method stub
        return null;
    }


    // Attribute getters and setters
	@Override
	public int getNumAttributes(Integer element) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Integer getAttributeWithName(Integer element, String name) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Integer getAttributeWithNameNS(Integer element, String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public Integer getAttribute(Integer element, int idx) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAttributeName(Integer element, Integer attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeLocalName(Integer element, Integer attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeNamespaceURI(Integer element, Integer attribute) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAttributePrefix(Integer element, Integer attribute) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAttributeValue(Integer element, Integer attribute) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer setAttributeValue(Integer element, int idx, String value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer setAttributeValue(Integer element, Integer attribute, String value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer setAttributeValue(Integer element, String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer setAttributeValue(Integer element, String namespaceURI, String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer removeAttribute(Integer element, String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer removeAttribute(Integer element, String namespaceURI, String localName) {
		// TODO Auto-generated method stub
		return null;
	}
}

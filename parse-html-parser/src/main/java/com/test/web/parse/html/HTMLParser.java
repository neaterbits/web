package com.test.web.parse.html;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import com.neaterbits.util.Value;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringCharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.BaseParser;
import com.neaterbits.util.parse.IParse;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;
import com.neaterbits.util.parse.TokenMergeHelper;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.parse.css.CSSParser;
import com.test.web.types.IIndent;

public class HTMLParser<ELEMENT, STYLE_DOCUMENT, CSS_LISTENER_CONTEXT>
	extends BaseParser<HTMLToken, CharInput>
	implements IIndent {
	
	private static final boolean DEBUG = false;
	private static final PrintStream DEBUG_OUT = System.out;
	
	private int debugStackLevel;
	
	private final Tokenizer tokenizer;
	private final Lexer<HTMLToken, CharInput> lexer;
	private final IHTMLParserListener<ELEMENT> htmlListener;

	private final IHTMLStyleParserListener<ELEMENT, CSS_LISTENER_CONTEXT> styleAttributeListener;
	private final CSSParser<CSS_LISTENER_CONTEXT> styleAttributeParser;
	
	private final IParse<STYLE_DOCUMENT> parseStyleDocument;
	
	private static Lexer<HTMLToken, CharInput> createLexer(CharInput input) {
		return new Lexer<HTMLToken, CharInput>(input, HTMLToken.class, HTMLToken.NONE, null);
	}

	public HTMLParser(
			CharInput input,
			Tokenizer tokenizer,
			IHTMLParserListener<ELEMENT> htmlListener,
			IHTMLStyleParserListener<ELEMENT, CSS_LISTENER_CONTEXT> styleAttributeListener,
			IParse<STYLE_DOCUMENT> parseStyleDocument) {

		super(createLexer(input), HTMLToken.WS);
	
		this.tokenizer = tokenizer;
		this.lexer = getLexer();
		this.htmlListener = htmlListener;
		
		this.styleAttributeListener = styleAttributeListener;
		this.styleAttributeParser = new CSSParser<>(input, tokenizer, styleAttributeListener);
		
		this.parseStyleDocument = parseStyleDocument;
	}

	private HTMLToken rootTag(HTMLToken ... tagTokens) throws IOException, ParserException {
		return endTagOrSub(null, true, tagTokens);
	}
	
	private HTMLToken endTagOrSubOrText(HTMLToken currentTag, boolean allowNone, boolean allowText, HTMLToken ... tagTokens) throws IOException, ParserException {
		final HTMLToken found;
		
		if (allowText) {
			// Look for text, tag start and WS in that order
			final HTMLToken text = lexer.lex(HTMLToken.TEXT);
			if (text == HTMLToken.TEXT) {
				
				debugText();
				
				found = text;
			}
			else {
				found = endTagOrSub(currentTag, allowNone, tagTokens);
			}
		}
		else {
			found = endTagOrSub(currentTag, allowNone, tagTokens);
		}
		
		return found;
	}

	private HTMLToken endTagOrSub(HTMLToken currentTag, boolean allowNone, HTMLToken ... tagTokens) throws IOException, ParserException {
		
		HTMLToken found = null;

		do {
			switch (lexSkipWS(HTMLToken.TAG_LESS_THAN)) {
			case TAG_LESS_THAN: // May be start of end-tag 
				
				final HTMLToken [] tokens = currentTag != null
					? TokenMergeHelper.merge(tagTokens, HTMLToken.COMMENT_CONTENT, HTMLToken.TAG_SLASH) 
					: TokenMergeHelper.merge(tagTokens, HTMLToken.COMMENT_CONTENT);
				
				HTMLToken tagToken;
					
				switch ((tagToken = lexSkipWS(tokens))) {
				case NONE:
					if (allowNone) {
						found = tagToken;
					}
					else {
						throw lexer.unexpectedToken();
					}
					break;

				case COMMENT_CONTENT:
					// Probably a comment
					found = null;
					break;

				case TAG_SLASH:
					// This is end-of-tag for current tag, skip tag
					if (lexSkipWS(currentTag) != currentTag) {
						throw lexer.unexpectedToken();
					}

					debugExit(currentTag.getElement().getName());

					htmlListener.onElementEnd(tokenizer, currentTag.getElement());
	
					// Skip '>' too
					if (lexSkipWS(HTMLToken.TAG_GREATER_THAN) != HTMLToken.TAG_GREATER_THAN) {
						throw lexer.unexpectedToken();
					}
					
					found = HTMLToken.TAG_END;
					break;
					
				default:
					// Found element
					debugEnter(tagToken.getElement().getName());
					final ELEMENT documentElement = htmlListener.onElementStart(tokenizer, tagToken.getElement());
					
					// Parse any attributes
					final HTMLToken [] attributeTokens = tagToken.getAttributeTokens();
					
					if (attributeTokens != null && attributeTokens.length > 0) {
						switch (parseTagAttributes(tagToken.getElement(), documentElement, attributeTokens)) {
						// In '/' then this is start and end tag in one 
						case TAG_SLASH:
							if (lexSkipWS(HTMLToken.TAG_GREATER_THAN) != HTMLToken.TAG_GREATER_THAN) {
								throw lexer.unexpectedToken();
							}
							debugExit(tagToken.getElement().getName());

							htmlListener.onElementEnd(tokenizer, tagToken.getElement());
							break;
							
						case TAG_GREATER_THAN:
							// End of tag, just exit loop with start tag
							if ( ! tagToken.getElement().hasAnyContent() ) {
								// Cannot have content so look for end tag
								checkTagEnd(tagToken);
							}
							found = tagToken;
							break;
							
						default:
							throw lexer.unexpectedToken();
						}
						
						// Already skipped '>' or '/>' as part of attribute parsing
					}
					else {
						// This was start of sub-tag, skip '>'
						if (lexSkipWS(HTMLToken.TAG_GREATER_THAN) != HTMLToken.TAG_GREATER_THAN) {
							throw lexer.unexpectedToken();
						}
					}
					found = tagToken;
					break;
				}
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
		}
		while (found == null);
		
		return found;
	}
	
	private void debugEnter(String context) {

		if (DEBUG) {
			indent(debugStackLevel, DEBUG_OUT);
	
			DEBUG_OUT.append("Start ").append(context).println();
		}

		++ debugStackLevel;
	}

	private void debugExit(String context) {
		-- debugStackLevel;

		if (DEBUG) {
			indent(debugStackLevel, DEBUG_OUT);
			DEBUG_OUT.append("End ").append(context).println();
		}
	}

	private void debugText() {
		if (DEBUG) {
			indent(debugStackLevel, DEBUG_OUT);
			DEBUG_OUT.append("Text: \"").append(tokenizer.asString(lexer.getStringRef(0, lexer.getEndSkip()))).println("\"");
		}
	}

	public void parseHTMLFile() throws IOException, ParserException {
		
		// Should always start with "<"
		if (lexSkipWS(HTMLToken.TAG_LESS_THAN) != HTMLToken.TAG_LESS_THAN) {
			throw lexer.unexpectedToken();
		}
		
		// Now may be !DOCTYPE or html
		switch (lexSkipWS(HTMLToken.TAG_EXCLAMATION_POINT, HTMLToken.HTML)) {
		
		case TAG_EXCLAMATION_POINT:
			parseDocType();
			
			// Probably HTML next
			if (rootTag(HTMLToken.HTML) == HTMLToken.HTML) {
				parseHTML();
			}
			else {
				throw new ParserException("Empty HTML file");
			}
			break;
		
		case HTML:
			if (lexSkipWS(HTMLToken.TAG_LESS_THAN) != HTMLToken.TAG_LESS_THAN) {
				throw lexer.unexpectedToken();
			}
			parseHTML();
			break;
			
		default:
			throw new ParserException("Empty HTML file");
		}
	}
	
	private void parseDocType() throws IOException, ParserException {

		debugEnter("DOCTYPE");
		
		if (lexSkipWS(HTMLToken.DOCTYPE) != HTMLToken.DOCTYPE) {
			throw lexer.unexpectedToken();
		}

		if (lexSkipWS(HTMLToken.DOCTYPE_HTML) != HTMLToken.DOCTYPE_HTML) {
			throw lexer.unexpectedToken();
		}

		if (lexSkipWS(HTMLToken.DOCTYPE_PUBLIC) != HTMLToken.DOCTYPE_PUBLIC) {
			throw lexer.unexpectedToken();
		}

		// DTD
		if (lexSkipWS(HTMLToken.DOUBLE_QUOTED_STRING) != HTMLToken.DOUBLE_QUOTED_STRING) {
			throw lexer.unexpectedToken();
		}

		// URL
		if (lexSkipWS(HTMLToken.DOUBLE_QUOTED_STRING) != HTMLToken.DOUBLE_QUOTED_STRING) {
			throw lexer.unexpectedToken();
		}
		
		// End of tag
		if (lexSkipWS(HTMLToken.TAG_GREATER_THAN) != HTMLToken.TAG_GREATER_THAN) {
			throw lexer.unexpectedToken();
		}
		
		debugExit("DOCTYPE");
	}
	
	private void parseHTML() throws IOException, ParserException {
		
		HTMLToken token = endTagOrSub(HTMLToken.HTML, true, HTMLToken.HEAD, HTMLToken.BODY);
		
		boolean parsedBody = false;
		
		switch (token) {
		case HEAD:
			parseHead();
			break;
			
		case BODY:
			parsedBody = true;
			parseBody();
			break;
			
		case TAG_END:
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
		
		if ( ! parsedBody ) {

			token = endTagOrSub(HTMLToken.HTML, true, HTMLToken.BODY);

			switch (token) {
			case BODY:
				parseBody();
				break;
				
			case TAG_END:
				break;

			default:
				throw lexer.unexpectedToken();
			}
		}
	}

	private void parseHead() throws IOException, ParserException {

		boolean done = false;

		do {
			switch (endTagOrSub(HTMLToken.HEAD, false, HTMLToken.TITLE, HTMLToken.META, HTMLToken.LINK, HTMLToken.SCRIPT, HTMLToken.ELEM_STYLE)) {

			case TITLE:
				parseText(HTMLToken.TITLE);
				break;

			case META:
				break;

			case SCRIPT:
				parseText(HTMLToken.SCRIPT);
				break;

			case ELEM_STYLE:
				parseStyleContent(HTMLToken.ELEM_STYLE);
				break;

			case LINK:
				break;

			case TAG_END:
				// </head>end tag
				done = true;
				break;
	
			default:
				throw lexer.unexpectedToken();
			}
		} while (!done);
	}
	
	private void parseBody() throws IOException, ParserException {
		
		parseContainer(HTMLToken.BODY, true);
		
	}
	
	
	private void parseDiv() throws IOException, ParserException {
		parseContainer(HTMLToken.DIV, true);
	}
	
	private void parseSpan() throws IOException, ParserException {
		parseContainer(HTMLToken.SPAN, true);
	}
	
	private void parseA() throws IOException, ParserException {
		parseContainer(HTMLToken.A, true);
	}

	private void parseFieldset() throws IOException, ParserException {
		parseContainer(HTMLToken.FIELDSET, true);
	}

	private void parseUl() throws IOException, ParserException {
		parseContainer(HTMLToken.UL, true);
	}

	private void parseLi() throws IOException, ParserException {
		parseContainer(HTMLToken.LI, true);
	}
	
	private void parseProgress() throws IOException, ParserException {
		parseContainer(HTMLToken.PROGRESS, true);
	}

	private void parseContainer(HTMLToken curToken, boolean text) throws IOException, ParserException {
		
		boolean done = false;
		
		do {
		
			switch (endTagOrSubOrText(curToken, false, text,
					HTMLToken.DIV,
					HTMLToken.SPAN,
					HTMLToken.A,
					HTMLToken.INPUT,
					HTMLToken.IMG,
					HTMLToken.FIELDSET,
					HTMLToken.UL,
					HTMLToken.LI,
					HTMLToken.PROGRESS)) {
			
			case DIV:
				parseDiv();
				break;
				
			case SPAN:
				parseSpan();
				break;
				
			case A:
				parseA();
				break;
				
			case FIELDSET:
				parseFieldset();
				break;
				
			case UL:
				parseUl();
				break;
				
			case LI:
				parseLi();
				break;
			
			case PROGRESS:
				parseProgress();
				break;
				
			case INPUT:
				// only attributes
				break;
				
			case IMG:
				// only attributes
				break;

			case TEXT:
				htmlListener.onText(tokenizer, lexer.getStringRef(0, lexer.getEndSkip()));
				break;
				
			case TAG_END:
				done = true;
				break;
	
			default:
				throw lexer.unexpectedToken();
			}
		} while (!done);
	}
	
	private HTMLToken parseTagAttributes(HTMLElement element, ELEMENT documentElement, HTMLToken ... attributes) throws IOException, ParserException {

		// Look for any attributes or for tag start
		final HTMLToken [] tokens = Arrays.copyOf(attributes, attributes.length + 3);

		// Must support WS between attributes

		tokens[attributes.length] 	  = HTMLToken.WS;
		
		tokens[attributes.length + 1] = HTMLToken.TAG_SLASH; // In case start and end-tag in same eg <tag [attributes] /> 
		tokens[attributes.length + 2] = HTMLToken.TAG_GREATER_THAN; // In case end of tag eg <tag [attributes]>"
		
		
		boolean done = false;
		HTMLToken token;
		
		do {
			token = lexer.lex(tokens);
			
			switch (token) {
			case TAG_SLASH:
				// End of tag marker
				done = true;
				break;
				
			case TAG_GREATER_THAN:
				// End tag
				done = true;
				break;
			
			case WS:
				// read next, still passes WS token as input but should not match
				break;
				
			case NONE:
				done = true;
				break;
				
			case CLASS:
				// Special-handling of class attribute since may contain multiple
				parseClassAttribute(token);
				break;
				
			case ATTR_STYLE:
				// Special handling of style attribute as we parse this as a CSS document
				parseStyleAttribute(documentElement);
				break;

			default:
				// This should be an attribute as is not any of the other tokens
				parseAttribute(token, element);
				break;
			}
		}
		while (!done);

		return token;
	}
	
	// Parse text elements that may contain other tags?
	private void parseText(HTMLToken elementToken) throws IOException, ParserException {
		// Scan for text or start-tag
		switch (lexer.lex(HTMLToken.TEXT, HTMLToken.TAG_LESS_THAN)) {
		case TEXT:
			debugText();
			htmlListener.onText(tokenizer, lexer.getStringRef(0, lexer.getEndSkip()));
			checkTagEnd(elementToken);
			break;

		case TAG_LESS_THAN:
			checkTagEndElement(elementToken);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private STYLE_DOCUMENT parseStyleContent(HTMLToken elementToken) throws IOException, ParserException {
		
		final STYLE_DOCUMENT styleDocument;
		
		// TODO perhaps reuse CharInput to avoid reading the whole text ? Rather just continue reading from buffer
		// TODO issue is XML comments
		final StringBuilder sb = new StringBuilder();
		// Scan for text or start-tag
		boolean done = false;
		do {
			switch (lexer.lex(HTMLToken.TEXT, HTMLToken.COMMENT_CONTENT, HTMLToken.TAG_LESS_THAN)) {
			case TEXT:
				debugText();
				
				final String styleText = tokenizer.asString(lexer.getStringRef(0, lexer.getEndSkip()));
				sb.append(styleText);
				
			
				// Parse this with CSS parser
				if (checkTagEndOrComment(elementToken) != HTMLToken.COMMENT_CONTENT) {
					done = true;
				}
				break;
	
			case TAG_LESS_THAN:
				if (checkTagEndElementOrComment(elementToken) != HTMLToken.COMMENT_CONTENT) {
					done = true;
				}
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
		} while(!done);
		
		final String text = sb.toString().trim();
		
		if (text.isEmpty()) {
			styleDocument = null;
		}
		else {
			final StringCharInput charInput = new StringCharInput(text);
			
			styleDocument = parseStyleDocument.parse(charInput, null);
		}

		return styleDocument;
	}


	// Common attribute parser for any input attribute
	
	private void parseAttribute(HTMLToken attributeToken, HTMLElement element) throws IOException, ParserException {
		
		// Attribute may have value, depending
		boolean done = false;
		
		do {
			final HTMLToken token = lexer.lex(HTMLToken.WS, HTMLToken.EQUALS);
			
			switch (token) {
			case WS:
				break;

			case EQUALS:
				parseAttributeValue(attributeToken, element);
				done = true;
				break;

			case NONE:
				htmlListener.onAttributeWithoutValue(tokenizer, attributeToken.getAttribute());
				done = true;
				break;

			default:
				throw lexer.unexpectedToken();
			}

		} while (!done);
	}

	private void parseAttributeValue(HTMLToken attributeToken, HTMLElement element) throws IOException, ParserException {
		
		HTMLToken token = lexSkipWS(HTMLToken.SINGLE_QUOTED_STRING, HTMLToken.DOUBLE_QUOTED_STRING);
		
		switch (token) {
		case SINGLE_QUOTED_STRING:
		case DOUBLE_QUOTED_STRING:
			// Read until end of quote or whitespace
			htmlListener.onAttributeWithValue(tokenizer, attributeToken.getAttribute(), lexer.getStringRef(1, 1), element);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}

	// class requires special parsing, since is space separated
	private void parseClassAttribute(HTMLToken attributeToken) throws IOException, ParserException {
		if (lexSkipWS(HTMLToken.EQUALS) != HTMLToken.EQUALS) {
			throw lexer.unexpectedToken();
		}

		final HTMLToken quoteToken = lexSkipWS(HTMLToken.SINGLE_QUOTE, HTMLToken.QUOTE);

		if (quoteToken == HTMLToken.NONE) {
			throw lexer.unexpectedToken();
		}
		
		boolean done = false;
		
		do {
			// Ignore WS at start and end of quotes
			final HTMLToken nextToken = lexer.lex(HTMLToken.WS, HTMLToken.CLASS_NAME, quoteToken);
			
			if (nextToken == quoteToken) {
                done = true;
			}
			else {
			    switch (nextToken) {
    			case WS:
    				// Skip to next
    				break;
    				
    			case CLASS_NAME:
    				// TODO: must skip 1 at end since lexer has read one past, find more elegant solution
    				htmlListener.onClassAttributeValue(tokenizer, lexer.getStringRef(0, lexer.getEndSkip()));
    				break;

    			default:
    				throw lexer.unexpectedToken();
    			}
	        }
		} while (!done);
	}
	
	// style attribute requires special parsing,  we parse utilizing the CSS parser for the style attribute
	private void parseStyleAttribute(ELEMENT documentElement) throws IOException, ParserException {

		// Parse style attribute contents using CSS parser, but we have to skip  ' and ;
		final HTMLToken equalsToken = lexSkipWS(HTMLToken.EQUALS);
		
		if (equalsToken != HTMLToken.EQUALS) {
			throw lexer.unexpectedToken();
		}
		
		final HTMLToken token = lexSkipWS(HTMLToken.QUOTE, HTMLToken.SINGLE_QUOTE);

		final HTMLToken peekToken;
		switch (token) {
		case QUOTE:
			peekToken = HTMLToken.UNTIL_QUOTE;
			break;
			
		case SINGLE_QUOTE:
			peekToken = HTMLToken.UNTIL_SINGLE_QUOTE;
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
		// Now parse CSS elements in a loop
		for (;;) {
			final Value<String> styleText = new Value<>();
			// We'd like to keep style text as well
			
			if (peekToken != lexer.peek(styleText, peekToken)) {
				throw new ParserException("Failed to find matching end of style attribute");
			}

			// Must set HTML element in CSS document so that we can later find back to CSS element from HTML element
			styleAttributeListener.startParseStyleElement(documentElement, styleText.get());
			
			// Call CSS parser to parse element
			styleAttributeParser.parseElement(null);
			
			final HTMLToken [] tokens;
			
			// TOO maybe move allocation outside loop
			tokens = new HTMLToken [] { token, HTMLToken.SEMICOLON };
			
			// Now expect end quote or semicolon
			final HTMLToken t = lexSkipWS(tokens);
			
			if (t == token) {
				break;
			}
			else if (t == HTMLToken.SEMICOLON) {
				// continue one more iteration
			}
			else {
				throw lexer.unexpectedToken();
			}
		}
	}

	
	private HTMLToken tags(HTMLToken ... htmlTokens) throws IOException, ParserException {

		final HTMLToken ret;
		
		HTMLToken token = lexer.lex(HTMLToken.TAG_LESS_THAN, HTMLToken.WS);
		
		if (token == HTMLToken.WS) {
			token = lexer.lex(HTMLToken.TAG_LESS_THAN);
		}

		if (token == HTMLToken.TAG_LESS_THAN) {

			final HTMLToken [] tokensPlusWS = Arrays.copyOf(htmlTokens, htmlTokens.length + 1);

			tokensPlusWS[htmlTokens.length] = HTMLToken.WS;

			token = lexer.lex(tokensPlusWS);

			if (token == HTMLToken.WS) {
				token = lexer.lex(htmlTokens);
			}
			
			ret = token;
		}
		else {
			ret = token;
		}


		return ret;
	}
	
	private void checkTagEnd(HTMLToken elementToken) throws IOException, ParserException {
		
		final HTMLToken token = lexSkipWS(HTMLToken.TAG_LESS_THAN);
		
		if (token != HTMLToken.TAG_LESS_THAN) {
			throw lexer.unexpectedToken();
		}
		
		checkTagEndElement(elementToken);
	}
		
	private void checkTagEndElement(HTMLToken elementToken) throws IOException, ParserException {

		HTMLToken token = lexSkipWS(HTMLToken.TAG_SLASH);
		if (token != HTMLToken.TAG_SLASH) {
			throw lexer.unexpectedToken();
		}

		checkElementEnd(elementToken);
	}

	private HTMLToken checkTagEndOrComment(HTMLToken elementToken) throws IOException, ParserException {
		
		final HTMLToken token = lexSkipWS(HTMLToken.TAG_LESS_THAN);
		
		if (token != HTMLToken.TAG_LESS_THAN) {
			throw lexer.unexpectedToken();
		}
		
		return checkTagEndElementOrComment(elementToken);
	}

	private HTMLToken checkTagEndElementOrComment(HTMLToken elementToken) throws IOException, ParserException {

		HTMLToken token = lexSkipWS(HTMLToken.TAG_SLASH, HTMLToken.COMMENT_CONTENT);
		if (token == HTMLToken.TAG_SLASH) {
			checkElementEnd(elementToken);
		}
		
		return token;
	}

	private void checkElementEnd(HTMLToken elementToken) throws IOException, ParserException {
		HTMLToken token = lexSkipWS(elementToken);
		if (token != elementToken) {
			throw lexer.unexpectedToken();
		}
		
		token = lexSkipWS(HTMLToken.TAG_GREATER_THAN);
		if (token != HTMLToken.TAG_GREATER_THAN) {
			throw lexer.unexpectedToken();
		}

		debugExit(elementToken.getElement().getName());

		htmlListener.onElementEnd(tokenizer, elementToken.getElement());
	}
}

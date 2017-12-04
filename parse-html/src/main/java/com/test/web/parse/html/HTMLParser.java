package com.test.web.parse.html;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import com.test.web.document.common.HTMLElement;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.types.IIndent;

public final class HTMLParser<ELEMENT, TOKENIZER extends Tokenizer>
	extends BaseParser<HTMLToken, CharInput>
	implements IIndent {
	
	private static final boolean DEBUG = true;
	private static final PrintStream DEBUG_OUT = System.out;
	
	private int debugStackLevel;
	
	private final TOKENIZER tokenizer;
	private final Lexer<HTMLToken, CharInput> lexer;
	private final IHTMLParserListener<ELEMENT, TOKENIZER> htmlListener;

	private final IHTMLStyleParserListener<ELEMENT, TOKENIZER> styleAttributeListener;
	private final CSSParser<TOKENIZER, Void> styleAttributeParser;
	
	private static Lexer<HTMLToken, CharInput> createLexer(CharInput input) {
		return new Lexer<HTMLToken, CharInput>(input, HTMLToken.class, HTMLToken.NONE, null);
	}

	public HTMLParser(
			CharInput input,
			TOKENIZER tokenizer,
			IHTMLParserListener<ELEMENT, TOKENIZER> htmlListener,
			IHTMLStyleParserListener<ELEMENT, TOKENIZER> styleAttributeListener) {

		super(createLexer(input), HTMLToken.WS);
	
		this.tokenizer = tokenizer;
		this.lexer = getLexer();
		this.htmlListener = htmlListener;
		
		this.styleAttributeListener = styleAttributeListener;
		this.styleAttributeParser = new CSSParser<>(input, styleAttributeListener);
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
					? merge(tagTokens, HTMLToken.COMMENT_CONTENT, HTMLToken.TAG_SLASH) 
					: merge(tagTokens, HTMLToken.COMMENT_CONTENT);
				
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

		indent(debugStackLevel, DEBUG_OUT);

		DEBUG_OUT.append("Start ").append(context).println();;

		++ debugStackLevel;
	}

	private void debugExit(String context) {
		-- debugStackLevel;
		indent(debugStackLevel, DEBUG_OUT);
		DEBUG_OUT.append("End ").append(context).println();;
	}

	private void debugText() {
		indent(debugStackLevel, DEBUG_OUT);
		DEBUG_OUT.append("Text: \"").append(tokenizer.asString(0, lexer.getEndSkip())).println("\"");
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
		if (lexSkipWS(HTMLToken.QUOTED_STRING) != HTMLToken.QUOTED_STRING) {
			throw lexer.unexpectedToken();
		}

		// URL
		if (lexSkipWS(HTMLToken.QUOTED_STRING) != HTMLToken.QUOTED_STRING) {
			throw lexer.unexpectedToken();
		}
		
		// End of tag
		if (lexSkipWS(HTMLToken.TAG_GREATER_THAN) != HTMLToken.TAG_GREATER_THAN) {
			throw lexer.unexpectedToken();
		}
		
		debugExit("DOCTYPE");
	}
	
	private void parseHTML() throws IOException, ParserException {
		
		final HTMLToken token = endTagOrSub(HTMLToken.HTML, true, HTMLToken.HEAD);

		if (token == HTMLToken.HEAD) {
			parseHead();
		}
		
		if (token != HTMLToken.TAG_END) {
			// Was not end of file, and may have parsed <head> tag, parse <body> if any
			switch (endTagOrSub(HTMLToken.HTML, true, HTMLToken.BODY)) {
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
			switch (endTagOrSub(HTMLToken.HEAD, false, HTMLToken.TITLE, HTMLToken.LINK, HTMLToken.SCRIPT)) {

			case TITLE:
				parseText(HTMLToken.TITLE);
				break;

			case SCRIPT:
				parseText(HTMLToken.SCRIPT);
				break;
				
			case LINK:
				break;

			case TAG_END:
				//end tag
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
	
	private void parseInput() throws IOException, ParserException {
		throw new UnsupportedOperationException("TODO");
	}
	

	private void parseContainer(HTMLToken curToken, boolean text) throws IOException, ParserException {
		
		boolean done = false;
		
		do {
		
			switch (endTagOrSubOrText(curToken, false, text, HTMLToken.DIV, HTMLToken.SPAN, HTMLToken.INPUT)) {
			case DIV:
				parseDiv();
				break;
				
			case SPAN:
				parseSpan();
				break;
				
			case INPUT:
				parseInput();
				break;
				
			case TEXT:
				htmlListener.onText(tokenizer, 0, lexer.getEndSkip());
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
				
			case STYLE:
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
			htmlListener.onText(tokenizer, 0, lexer.getEndSkip());
			checkTagEnd(elementToken);
			break;

		case TAG_LESS_THAN:
			checkTagEndElement(elementToken);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
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
		
		HTMLToken token = lexSkipWS(HTMLToken.QUOTED_STRING);
		
		switch (token) {
		case QUOTED_STRING:
			// Read until end of quote or whitespace
			htmlListener.onAttributeWithValue(tokenizer, attributeToken.getAttribute(), 1, 1, element);
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
		
		if (lexSkipWS(HTMLToken.QUOTE) != HTMLToken.QUOTE) {
			throw lexer.unexpectedToken();
		}
		
		boolean done = false;
		
		do {
			// Ignore WS at start and end of quotes
			switch (lexer.lex(HTMLToken.WS, HTMLToken.CLASS_NAME, HTMLToken.QUOTE)) {
			case WS:
				// Skip to next
				break;
				
			case CLASS_NAME:
				// TODO: must skip 1 at end since lexer has read one past, find more elegant solution
				htmlListener.onClassAttributeValue(tokenizer, 0, lexer.getEndSkip());
				break;
				
			case QUOTE:
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
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

		switch (token) {
		case QUOTE:
		case SINGLE_QUOTE:
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
		// Now parse CSS elements in a loop
		for (;;) {
			
			// Must set HTML element in CSS document so that we can later find back to CSS element from HTML element
			styleAttributeListener.startParseStyleElement(documentElement);
			
			// Call CSS parser to parse element
			final boolean semiColonRead = styleAttributeParser.parseElement(null);
			
			final HTMLToken [] tokens;
			
			// TOO maybe move allocation outside loop
			if (semiColonRead) {
				tokens = new HTMLToken [] { token };
			}
			else {
				tokens = new HTMLToken [] { token, HTMLToken.SEMICOLON };
			}
			
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

		token = lexSkipWS(elementToken);
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

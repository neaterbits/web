package com.test.web.parse.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.text.html.HTML;

import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.enums.HTMLElement;

public final class HTMLParser<TOKENIZER extends Tokenizer> extends BaseParser<HTMLToken, CharInput> {
	
	private final TOKENIZER tokenizer;
	private final Lexer<HTMLToken, CharInput> lexer;
	private final HTMLParserListener<TOKENIZER> listener;
	
	public HTMLParser(CharInput input, TOKENIZER tokenizer, HTMLParserListener<TOKENIZER> listener) {
		super(new Lexer<HTMLToken, CharInput>(input, HTMLToken.class, HTMLToken.NONE, null), HTMLToken.WS);
		
		this.tokenizer = tokenizer;
		this.lexer = getLexer();
		this.listener = listener;
	}
	
	
	
	private HTMLToken rootTag(HTMLToken ... tagTokens) throws IOException, ParserException {
		return endTagOrSub(null, tagTokens);
	}
	
	
	private HTMLToken endTagOrSub(HTMLToken currentTag, HTMLToken ... tagTokens) throws IOException, ParserException {
		
		HTMLToken found = null;

		do {
			switch (lexSkipWS(HTMLToken.TAG_LESS_THAN)) {
			case TAG_LESS_THAN: // May be start of end-tag 
				
				final HTMLToken [] tokens = currentTag != null
					? merge(tagTokens, HTMLToken.COMMENT_CONTENT, HTMLToken.TAG_SLASH) 
					: merge(HTMLToken.COMMENT_CONTENT, tagTokens);
				
				HTMLToken tagToken;
					
				switch ((tagToken = lexSkipWS(tokens))) {
				case NONE:
					throw lexer.unexpectedToken();
					
				case COMMENT_CONTENT:
					// Probably a comment
					found = null;
					break;
					
				case TAG_SLASH:
					// This is end-of-tag for current tag, skip tag
					if (lexSkipWS(currentTag) != currentTag) {
						throw lexer.unexpectedToken();
					}
	
					// Skip '>' too
					if (lexSkipWS(HTMLToken.TAG_GEATER_THAN) != HTMLToken.TAG_GEATER_THAN) {
						throw lexer.unexpectedToken();
					}
	
					found = HTMLToken.TAG_END;
					break;
	
				default:
					// This was start of sub-tag, skip '>'
					if (lexSkipWS(HTMLToken.TAG_GEATER_THAN) != HTMLToken.TAG_GEATER_THAN) {
						throw lexer.unexpectedToken();
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
		if (lexSkipWS(HTMLToken.TAG_GEATER_THAN) != HTMLToken.TAG_GEATER_THAN) {
			throw lexer.unexpectedToken();
		}
	}
	
	private void parseHTML() throws IOException, ParserException {
		
		switch (endTagOrSub(HTMLToken.HTML, HTMLToken.HEAD, HTMLToken.BODY)) {
		
		case HEAD:
			parseHead();
			break;
		
		case BODY:
			break;

		case TAG_END:
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
	}

	private void parseHead() throws IOException, ParserException {
		switch (endTagOrSub(HTMLToken.HEAD, HTMLToken.SCRIPT)) {
		case SCRIPT:
			break;

		case TAG_END:
			//end tag
			break;

		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private void parseBody() throws IOException, ParserException {
		
		parseContainer(HTMLElement.BODY);
		
	}
	
	private void parseContainerElements() throws IOException, ParserException {
	
		switch (tags(HTMLToken.DIV, HTMLToken.SPAN, HTMLToken.INPUT)) {
		case DIV:
			parseDiv();
			break;
			
		case SPAN:
			parseSpan();
			break;
			
		case INPUT:
			parseInput();
			break;

		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private void parseDiv() throws IOException, ParserException {
		
		parseContainerElements();
	}
	
	private void parseSpan() throws IOException, ParserException {
		parseContainerElements();
	}
	
	private void parseInput() throws IOException, ParserException {
		throw new UnsupportedOperationException("TODO");
	}
	

	private void parseContainer(HTMLElement element) throws IOException, ParserException {
		
		// Parse all attributed onto element
		listener.onElementStart(tokenizer, element);

		parseTagAttributes();
		
		parseContainerElements();
		
		listener.onElementEnd(tokenizer, element);
	}
	
	private HTMLToken parseTagAttributes(HTMLToken ... attributes) throws IOException, ParserException {

		// Look for any attributes or for tag start
		final HTMLToken [] tokens = Arrays.copyOf(attributes, attributes.length + 3);

		// Must support WS between attributes

		tokens[attributes.length] 	  = HTMLToken.WS;
		
		tokens[attributes.length + 1] = HTMLToken.TAG_SLASH; // In case start and end-tag in same eg <tag [attributes] /> 
		tokens[attributes.length + 2] = HTMLToken.TAG_GEATER_THAN; // In case end of tag eg <tag [attributes]>"
		
		HTMLToken token = lexer.lex(tokens); 
		
		boolean done = false;
		
		do {
			switch (token) {
			case TAG_SLASH:
				// End of tag marker
				done = true;
				break;
				
			case TAG_GEATER_THAN:
				// End tag
				done = true;
				break;
			
			case WS:
				// read next, still passes WS token as input but should not match
				token = lexer.lex(tokens); 
				break;
				
			case NONE:
				done = true;
				break;

			default:
				// This should be an attribute as is not any of the other tokens
				parseAttribute(token);
				break;
			}
		}
		while (!done);

		return token;
	}

	// Common attribute parser for any input attribute
	
	private void parseAttribute(HTMLToken attributeToken) throws IOException, ParserException {
		
		// Attribute may have value, depending
		
		for (;;) {
		
			final HTMLToken token = lexer.lex(HTMLToken.WS, HTMLToken.EQUALS);
			
			switch (token) {
			case WS:
				break;
				
			case EQUALS:
				parseAttributeValue(attributeToken);
				break;
				
			case NONE:
				break;
			
			default:
				if (token.getAttribute() != null) {
					listener.onAttribute(tokenizer, attributeToken.getAttribute());
				}
				else {
					throw lexer.unexpectedToken();
				}
			}
		}
	}

	private void parseAttributeValue(HTMLToken attributeToken) throws IOException, ParserException {
		
		HTMLToken token = lexer.lex(HTMLToken.QUOTE, HTMLToken.WS);
		
		if (token == HTMLToken.WS) {
			token = lexer.lex(HTMLToken.QUOTE);
		}
		
		switch (token) {
		case QUOTE:
			// Read until end of quote or whitespace
			break;
			
		default:
			throw lexer.unexpectedToken();
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
	
	private HTMLToken tagEnd() throws IOException, ParserException {
		
		HTMLToken token;
		
		token = lexer.lex(HTMLToken.TAG_GEATER_THAN, HTMLToken.WS);
		if (token == HTMLToken.WS) {
			// Read past WS
			token = lexer.lex(HTMLToken.TAG_GEATER_THAN);
		}
		
		return token;
	}
}

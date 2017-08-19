package com.test.web.parse.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.enums.HTMLElement;

final class HTMLParser<INPUT extends CharInput, TOKENIZER extends Tokenizer> extends BaseParser {
	
	private final INPUT input;
	private final TOKENIZER tokenizer;
	private final Lexer<HTMLToken, INPUT> lexer;
	private final HTMLParserListener<INPUT, TOKENIZER> listener;
	
	public HTMLParser(INPUT input, TOKENIZER tokenizer, HTMLParserListener<INPUT, TOKENIZER> listener) {
		
		this.input = input;
		this.tokenizer = tokenizer;
		this.lexer = new Lexer<HTMLToken, INPUT>(input, HTMLToken.class, HTMLToken.NONE);
		this.listener = listener;
	}
	
	
	public void parseHTMLFile() throws IOException, ParserException {
		
		// Try parse HTML from start
		
		switch (lexer.lex(HTMLToken.HTML)) {
		
		case HTML:
			
			parseHTML();
			
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private void parseHTML() throws IOException, ParserException {
		
		switch (lexer.lex(HTMLToken.HEAD, HTMLToken.BODY)) {
		
		case HEAD:
			parseHead();
			break;
		
		case BODY:
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
	}

	private void parseHead() throws IOException, ParserException {
		switch (tags(HTMLToken.SCRIPT)) {
		case SCRIPT:
			
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
		
		tokens[attributes.length + 1] = HTMLToken.END_TAG_MARKER; // In case start and end-tag in same eg <tag [attributes] /> 
		tokens[attributes.length + 2] = HTMLToken.TAG_END; // In case end of tag eg <tag [attributes]>"
		
		HTMLToken token = lexer.lex(tokens); 
		
		boolean done = false;
		
		do {
			switch (token) {
			case END_TAG_MARKER:
				// End of tag marker
				done = true;
				break;
				
			case TAG_END:
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
		
		HTMLToken token = lexer.lex(HTMLToken.TAG_START, HTMLToken.WS);
		
		if (token == HTMLToken.WS) {
			token = lexer.lex(HTMLToken.TAG_START);
		}

		if (token == HTMLToken.TAG_START) {

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
		
		token = lexer.lex(HTMLToken.TAG_END, HTMLToken.WS);
		if (token == HTMLToken.WS) {
			// Read past WS
			token = lexer.lex(HTMLToken.TAG_END);
		}
		
		return token;
	}
}

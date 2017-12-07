package com.test.web.parse.css;

import java.io.IOException;
import java.util.function.BiConsumer;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser.InitialMarginOrPaddingParser;
import com.test.web.types.DecimalSize;

class CSSParserHelperMargin {
	private static final CSSUnit marginOrPaddingDefaultUnit = CSSUnit.PX;

	private static final CSSToken [] autoOrInitialOrInheritTokens = new CSSToken[] { CSSToken.INTEGER, CSSToken.AUTO, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT };
	private static final CSSToken [] initialOrInheritTokens 			   = new CSSToken[] { CSSToken.INTEGER, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT };

	static boolean parseSizeOrAutoOrInitialOrInherit(Lexer<CSSToken, CharInput> lexer, IJustifyFunction toCall) throws IOException, ParserException {
		return parseSizeOrAutoOrInitialOrInherit(lexer, toCall, autoOrInitialOrInheritTokens);
	}

	static boolean parseSizeOrInitialOrInherit(Lexer<CSSToken, CharInput> lexer, IJustifyFunction toCall) throws IOException, ParserException {
		return parseSizeOrAutoOrInitialOrInherit(lexer, toCall, initialOrInheritTokens);
	}
	
	private static boolean parseSizeOrAutoOrInitialOrInherit(Lexer<CSSToken, CharInput> lexer, IJustifyFunction toCall, CSSToken [] tokens) throws IOException, ParserException {
		CSSToken token = CSSParserHelperWS.lexSkipWSAndComment(lexer, tokens);
		
		final boolean semiColonRead;
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onJustify(size, unit, CSSJustify.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			semiColonRead = CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, marginOrPaddingDefaultUnit, intValue, sizeCallback);
			break;
			
		case AUTO:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.AUTO);
			semiColonRead = false;
			break;

		case INITIAL:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.INITIAL);
			semiColonRead = false;
			break;
			
		case INHERIT:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.INHERIT);
			semiColonRead = false;
			break;

		case DOT:
			semiColonRead = CSSParserHelperSizeToSemicolon.parseDecimalAfterDot(lexer, marginOrPaddingDefaultUnit, 0, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}

	private static class MarginPart {
		private int size;
		private CSSUnit unit;
		private CSSJustify justify;
		private boolean initialized;
		
		void init(int size, CSSUnit unit, CSSJustify justify) {
			this.size = size;
			this.unit = unit;
			this.justify = justify;
			this.initialized = true;
		}
	}

	static <LISTENER_CONTEXT>boolean parseMarginOrPadding(Lexer<CSSToken, CharInput> lexer, LISTENER_CONTEXT context, IWrapping<LISTENER_CONTEXT> callback, InitialMarginOrPaddingParser parseInitial) throws IOException, ParserException {
		
		// TODO perhaps cache if parser is singlethreaded
		final MarginPart part1 = new MarginPart();
		
		// first parse one size or auto
		//boolean semiColonRead = parseSizeOrAuto((size, unit, justify) -> part1.init(size, unit, justify));
		boolean semiColonRead = parseInitial.parse(lexer, (size, unit, justify) -> part1.init(size, unit, justify));
		
		if (part1.justify != CSSJustify.NONE && part1.justify != CSSJustify.SIZE) {
			// margin: auto which is special-case
			callback.onWrapping(context,
					0, null, part1.justify,
					0, null, part1.justify,
					0, null, part1.justify,
					0, null, part1.justify);
		}
		else {
			
			// We have more than one part that we have to read, which is which depends on the number of sizes found
			// there ought to be max 4
			// TODO perhaps cache if parser is singlethreaded
			final MarginPart part2 = new MarginPart();
			final MarginPart part3 = new MarginPart();
			final MarginPart part4 = new MarginPart();
			
			if (!semiColonRead) {
				semiColonRead = CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, marginOrPaddingDefaultUnit, (size, unit) -> part2.init(size, unit, CSSJustify.SIZE));
				if (part2.initialized && !semiColonRead) {
					// read a value, try part3
					semiColonRead = CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, marginOrPaddingDefaultUnit, (size, unit) -> part3.init(size, unit, CSSJustify.SIZE));
					
					if (part3.initialized && !semiColonRead) {
						semiColonRead = CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, marginOrPaddingDefaultUnit, (size, unit) -> part4.init(size, unit, CSSJustify.SIZE));
					}
				}
			}
			
			if (part4.initialized) {
				// got 4 parts, pass them all
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part4.size, part4.unit, part4.justify);
			}
			else if (part3.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part2.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part1.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify);
			}
			else {
				throw new IllegalStateException("Should have at least one margin part");
			}
		}
			
		return semiColonRead;
	}
}

package com.test.web.parse.css;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.types.DecimalSize;
import com.test.web.types.HexUtils;

public class CSSParserHelperColor {
	static CSSToken parseColor(Lexer<CSSToken, CharInput> lexer, IColorRGBFunction rgbColor, ICSSColorFunction cssColor, CSSToken [] tokens) throws IOException, ParserException {

		CSSToken token = CSSParserHelperWS.lexSkipWSAndComment(lexer, tokens);
		
		switch (token) {
		case COLOR_MARKER:
			// Read color string
			token = parseHexColor(lexer, rgbColor);
			break;

		case FUNCTION_RGB:
			parseRGBFunction(lexer, rgbColor);
			break;
			
		case FUNCTION_RGBA:
			parseRGBAFunction(lexer, rgbColor);
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// a CSS standard color
			if (token.getColor() != null) {
				cssColor.onColor(token.getColor());
			}
			else {
				// One of the special enum values, like initial or inherit
				// handle this in the calling function
			}
			break;
		}
		
		return token;
	}
	
	static void parseRGBFunction(Lexer<CSSToken, CharInput> lexer, IColorRGBFunction rgbFunction) throws IOException, ParserException {
		final Object [] vals = CSSParserHelperFunction.parseFunctionParams(lexer, 3, paramIdx -> CSSParseHelperNumbers.parseInt(lexer));
		
		rgbFunction.onColor((int)vals[0], (int)vals[1], (int)vals[2], DecimalSize.NONE);
	}

	static void parseRGBAFunction(Lexer<CSSToken, CharInput> lexer, IColorRGBFunction rgbFunction) throws IOException, ParserException {
		
		final Object [] vals = CSSParserHelperFunction.parseFunctionParams(lexer, 4, paramIdx -> paramIdx == 3
				? CSSParseHelperNumbers.parseDecimal(lexer)
				: CSSParseHelperNumbers.parseInt(lexer));
		
		rgbFunction.onColor((int)vals[0], (int)vals[1], (int)vals[2], (int)vals[3]);
	}

	
	static CSSToken parseHexColor(Lexer<CSSToken, CharInput> lexer, IColorRGBFunction rgbColor) throws IOException, ParserException {
		CSSToken token = lexer.lex(CSSToken.HEXDIGITS);
		if (token != CSSToken.HEXDIGITS) {
			throw lexer.unexpectedToken();
		}
		
		final String hexString = lexer.get();
		
		if (hexString.length() != 6) {
			throw new ParserException("Unexpected length: " + hexString.length());
		}
		
		// Parse into hex values
		rgbColor.onColor(
				HexUtils.hexValue(hexString, 0, 2),
				HexUtils.hexValue(hexString, 2, 2),
				HexUtils.hexValue(hexString, 4, 2),
				DecimalSize.NONE
		);
		
		return token;
	}


}

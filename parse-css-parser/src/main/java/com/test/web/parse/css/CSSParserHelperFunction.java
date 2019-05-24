package com.test.web.parse.css;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

class CSSParserHelperFunction {

	@FunctionalInterface
	interface IParseParam {
		Object parse(int paramIdx) throws IOException, ParserException;
	}
	
	private static final Object [] EMPTY_ARRAY = new Object[0];
	
	static Object [] parseFunctionParams(Lexer<CSSToken, CharInput> lexer, int numParams, IParseParam parseParam) throws IOException, ParserException {
		// Parsed the function name already, parse the parameters
		CSSToken token = lexer.lexSkipWSAndComment(CSSToken.PARENTHESIS_START);
	
		if (token != CSSToken.PARENTHESIS_START) {
			throw lexer.unexpectedToken();
		}
		
		final Object [] ret;
		
		if (numParams == 0) {
			ret = EMPTY_ARRAY;
		}
		else {
			ret = new Object[numParams];
			
			for (int i = 0; i < numParams; ++ i) {
				
				lexer.skipAnyWS();
				
				ret[i] = parseParam.parse(i);

				// Comma or end parenthesis
				final CSSToken nextToken = i == numParams - 1
						? CSSToken.PARENTHESIS_END
						: CSSToken.COMMA;
				
				token = lexer.lexSkipWSAndComment(nextToken);

				if (token != nextToken) {
					throw lexer.unexpectedToken();
				}
			}
		}
		
		return ret;
	}

	private static final CSSToken [] NEXT_FUNCTION_PARAM = new CSSToken [] {
			CSSToken.COMMA,
			CSSToken.PARENTHESIS_END
	};
	
	static Object [] parseUnknownNumberOfFunctionParams(Lexer<CSSToken, CharInput> lexer, IParseParam parseParam) throws IOException, ParserException {
		// Parsed the function name already, parse the parameters
		CSSToken token = lexer.lexSkipWSAndComment(CSSToken.PARENTHESIS_START);
	
		if (token != CSSToken.PARENTHESIS_START) {
			throw lexer.unexpectedToken();
		}
		
		final List<Object> list = new ArrayList<>();
		
		final Object [] ret;

		boolean done = false;
		
		int paramIdx = 0;
		
		do {
				
			lexer.skipAnyWS();
			
			list.add(parseParam.parse(paramIdx));
			
			token = lexer.lexSkipWSAndComment(NEXT_FUNCTION_PARAM);

			switch (token) {
			case COMMA:
				break;
				
			case PARENTHESIS_END:
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			
			++ paramIdx;
		}
		while (!done);
		
		if (list.isEmpty()) {
			ret = EMPTY_ARRAY;
		}
		else {
			ret = list.toArray(new Object[list.size()]);
		}
		
		return ret;
	}
}

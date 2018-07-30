package com.test.web.parse.css;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;

class CSSParserHelperFunction {

	@FunctionalInterface
	interface IParseParam {
		Object parse(int paramIdx) throws IOException, ParserException;
	}
	
	private static final Object [] EMPTY_ARRAY = new Object[0];
	
	static Object [] parseFunctionParams(Lexer<CSSToken, CharInput> lexer, int numParams, IParseParam parseParam) throws IOException, ParserException {
		// Parsed the function name already, parse the parameters
		CSSToken token = CSSParserHelperWS.lexSkipWSAndComment(lexer, CSSToken.PARENTHESIS_START);
	
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
				
				CSSParserHelperWS.skipAnyWS(lexer);
				
				ret[i] = parseParam.parse(i);

				// Comma or end parenthesis
				final CSSToken nextToken = i == numParams - 1
						? CSSToken.PARENTHESIS_END
						: CSSToken.COMMA;
				
				token = CSSParserHelperWS.lexSkipWSAndComment(lexer, nextToken);

				if (token != nextToken) {
					throw lexer.unexpectedToken();
				}
			}
		}
		
		return ret;
	}

	static Object [] parseUnknownNumberOfFunctionParams(Lexer<CSSToken, CharInput> lexer, IParseParam parseParam) throws IOException, ParserException {
		// Parsed the function name already, parse the parameters
		CSSToken token = CSSParserHelperWS.lexSkipWSAndComment(lexer, CSSToken.PARENTHESIS_START);
	
		if (token != CSSToken.PARENTHESIS_START) {
			throw lexer.unexpectedToken();
		}
		
		final List<Object> list = new ArrayList<>();
		
		final Object [] ret;

		boolean done = false;
		
		int paramIdx = 0;
		
		do {
				
			CSSParserHelperWS.skipAnyWS(lexer);
			
			list.add(parseParam.parse(paramIdx));
			
			token = CSSParserHelperWS.lexSkipWSAndComment(lexer, CSSToken.COMMA, CSSToken.PARENTHESIS_END);

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

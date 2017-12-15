package com.test.web.io.common;

import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
import java.math.BigDecimal;
import com.test.web.types.IEnum;

/**
 * Marker interface for tokenizer that allows to read tokens
 * @author nhl
 *
 */

public interface Tokenizer {
	
	<E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, int startOffset, int endSkip, boolean caseSensitive);
	
	boolean equalsIgnoreCase(String s, int startOffset, int endSkip);
	
	int addToBuffer(DuplicateDetectingStringStorageBuffer buffer, int startOffset, int endSkip);
	
	String asString(int startOffset, int endSkip);

	Integer asInteger(int startOffset, int endSkip);

	int asDecimalSize(int startOffset, int endSkip);

	BigDecimal asBigDecimal(int startOffset, int endSkip);
}

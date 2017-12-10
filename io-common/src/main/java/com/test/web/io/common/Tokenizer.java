package com.test.web.io.common;

import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
import com.test.web.types.IEnum;

/**
 * Marker interface for tokenizer that allows to read tokens
 * @author nhl
 *
 */

public interface Tokenizer {
	
	<E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, boolean caseSensitive);
	
	boolean equalsIgnoreCase(String s);
	
	int addToBuffer(DuplicateDetectingStringStorageBuffer buffer, int startOffset, int endSkip);
	
	String asString(int startOffset, int endSkip);

	int asDecimalSize(int startOffset, int endSkip);
}

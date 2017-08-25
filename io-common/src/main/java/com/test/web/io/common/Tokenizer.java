package com.test.web.io.common;

import com.test.web.buffers.StringStorageBuffer;
import com.test.web.types.IEnum;

/**
 * Marker interface for tokenizer that allows to read tokens
 * @author nhl
 *
 */

public interface Tokenizer {
	
	<E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, boolean caseSensitive);
	
	boolean equalsIgnoreCase(String s);
	
	int addToBuffer(StringStorageBuffer buffer);
}

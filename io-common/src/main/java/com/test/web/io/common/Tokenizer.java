package com.test.web.io.common;

import com.test.web.buffers.DuplicateDetectingStringStorageBuffer;
import java.math.BigDecimal;
import com.test.web.types.IEnum;

/**
 * Marker interface for tokenizer that allows to read tokens. All strings are longs
 * @author nhl
 * 
 *
 */

public interface Tokenizer {
	
	<E extends Enum<E> & IEnum> E asEnum(Class<E> enumClass, long stringRef, boolean caseSensitive);
	
	boolean equalsIgnoreCase(String s, long stringRef);
	
	int addToBuffer(DuplicateDetectingStringStorageBuffer buffer, long stringRef);
	
	String asString(long stringRef);

	Integer asInteger(long stringRef);

	int asDecimalSize(long stringRef);

	BigDecimal asBigDecimal(long stringRef);
}

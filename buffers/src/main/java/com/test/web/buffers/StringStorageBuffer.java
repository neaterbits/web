package com.test.web.buffers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Buffer for keeping Strings that used often, eg element id or class.
 * May use some time for building, most of time is spent reading, but for most
 * element processing, the generated integer IDs will be utilized so no need to access the strings themselves.
 *  
 * References are start/length, encoded as integers
 *  - can retrieve original String
 *  - can use integers for lookup
 *  - only a single buffer
 *  - finds already added strings and during addition and returns same ID
 *  
 *   
 *  Only add strings since faster
 *  
 * @author nhl
 *
 */

public class StringStorageBuffer {

	// Stores everything in one buffer
	private char [] buffer;
	
	private int writePos;
	
	private final Map<String, Integer> map;
	
	public static final int NONE = 0;
	
	public StringStorageBuffer() {
		this.map = new HashMap<String, Integer>();
		this.buffer = new char[10000];
		
		// Start at 1 so not same as NONE
		this.writePos = 1;
	}
	
	public int add(String s) {
		
		if (s == null) {
			throw new IllegalArgumentException("s == 0");
		}
		
		final int len = s.length();
		
		if (len == 0) {
			throw new IllegalArgumentException("s.length() == 0");
		}
		
		final Integer idx = map.get(s);
		final int ret;
		
		if (idx == null) {
			// Not already added, add to buffer
			
			final int size = writePos + len + 1;
			
			if (size > Integer.MAX_VALUE) {
				throw new IllegalStateException("No more room in array");
			}
			
			if (size > buffer.length) {
				expandBuffer(size);
			}
		
			s.getChars(0, len, buffer, writePos);
			
			ret = writePos;
			
			writePos += len;
			
			buffer[writePos] = '\0';
			
			++ writePos;
		}
		else {
			ret = idx;
		}
		
		return ret;
	}
	
	private void expandBuffer(int size) {
		final int newSize = Math.max(size, buffer.length * 2);
		
		final char [] newBuffer = Arrays.copyOf(buffer, newSize);
		
		this.buffer = newBuffer;
	}
	
	
	public int add(char [] chars, int offset, int length) {
		return add(new String(chars, offset, length));
	}
	
	
	public String getString(int ref) {
		if (ref > writePos) {
			throw new IllegalArgumentException("ref > writePos");
		}
		
		if (ref == NONE) {
			throw new IllegalArgumentException("ref == NONE");
		}
		
		for (int i = ref; ; ++ i) {
			final char c = buffer[i];
			
			if (c == '\0') {
				// 0-termination
				if (i == 0) {
					throw new IllegalStateException("empty string for ref " + ref);
				}
				
				return new String(buffer, ref, i - ref);
			}
		}
	}
}

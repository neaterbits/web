package com.test.web.io.common;

import java.io.IOException;

/**
 * A stream that is being loaded from
 * 
 * @author nhl
 *
 */

public abstract class LoadStream {
	
	/**
	 * Reads data into buffer, may block if waiting for external IO
	 * @return status from StreamStatus class
	 */
	
	public abstract long read(char [] buffer, int offset, int length) throws IOException;
	
}

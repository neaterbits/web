package com.test.web.loadqueue.common;

import java.io.IOException;
import java.io.InputStream;

public interface IStreamFactory {

	InputStream getStream(String url) throws IOException;
	
}

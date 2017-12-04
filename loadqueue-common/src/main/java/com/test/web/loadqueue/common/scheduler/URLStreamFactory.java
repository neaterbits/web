package com.test.web.loadqueue.common.scheduler;

import java.io.IOException;
import java.io.InputStream;

import com.test.web.loadqueue.common.IStreamFactory;

public class URLStreamFactory implements IStreamFactory {

	@Override
	public InputStream getStream(String url) throws IOException {
		return new java.net.URL(url).openStream();
	}
}

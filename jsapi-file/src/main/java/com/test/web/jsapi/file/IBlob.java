package com.test.web.jsapi.file;

public interface IBlob {

	long getSize();

	String getType();
	
	IBlob slice(int start);
	
	IBlob slice(int start, int end);

	IBlob slice(int start, int end, String contentType);
}

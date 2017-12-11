package com.test.web.jsapi.file;

import com.test.web.jsapi.common.JSDate;

public class JSFile implements IBlob {
	private final String name;
	private final long size;
	private final long lastModified;
	private final JSDate lastModifiedDate;
	
	private final String type;

	public JSFile(String name, long size, long lastModified, JSDate lastModifiedDate, String type) {
		this.name = name;
		this.size = size;
		this.lastModified = lastModified;
		this.lastModifiedDate = lastModifiedDate;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public long getLastModified() {
		return lastModified;
	}

	public JSDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public IBlob slice(int start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBlob slice(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBlob slice(int start, int end, String contentType) {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.test.web.jsapi.file;

import java.util.List;

public class FileList {
	private final List<JSFile> files;

	public FileList(List<JSFile> files) {
		this.files = files;
	}

	public JSFile item(long index) {
		return files.get((int)index);
	}
	
	public long getLength() {
		return files.size();
	}
}

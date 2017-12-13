package com.test.web.jsapi.dom.dnd;

import com.test.web.jsapi.dom.JSEnum;
import com.test.web.jsapi.file.JSFile;

public class DataTransferItem {

	private final String kind;
	private final String type;
	
	private final String data;
	private final JSFile file;
	
	public DataTransferItem(String data, String type) {
		this.kind = DataTransferItemKind.STRING.getName();
		this.type = type;
		this.data = data;
		this.file = null;
	}
	
	public DataTransferItem(JSFile file) {
		// TODO Auto-generated constructor stub
		this.kind = DataTransferItemKind.FILE.getName();
		this.type = null; // TODO determine type
		this.data = null;
		this.file = file;
	}

	@JSEnumValues(DataTransferItemKind.class)
	public String getKind() {
		return kind;
	}

	DataTransferItemKind getKindEnum() {
		return JSEnum.asEnum(DataTransferItemKind.class, kind);
	}

	public String getType() {
		return type;
	}
	
	JSFile getFile() {
		return file;
	}
	
	String getString() {
		return data;
	}

	public JSFile getAsFile() {
		// TODO implement
		throw new UnsupportedOperationException();
	}

	public void getAsString(ICallback callback) {
		callback.call((String)data);
	}
}

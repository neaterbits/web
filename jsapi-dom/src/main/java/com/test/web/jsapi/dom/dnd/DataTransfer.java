package com.test.web.jsapi.dom.dnd;

import com.test.web.jsapi.file.FileList;

public class DataTransfer {
	
	private String dropEffect;
	private String effectAllowed;

	private DataTransferItemList items;

	@JSEnumValues(DropEffect.class)
	public String getDropEffect() {
		return dropEffect;
	}

	public void setDropEffect(String dropEffect) {
		this.dropEffect = dropEffect;
	}

	@JSEnumValues(EffectAllowed.class)
	public String getEffectAllowed() {
		return effectAllowed;
	}

	public void setEffectAllowed(String effectAllowed) {
		this.effectAllowed = effectAllowed;
	}

	public FileList getFiles() {
		return items.getFiles();
	}

	public void clearData(String format) {
		items.clearData(format);
	}
	
	public String getData(String format) {
		
		final String data = items.getData(format);
		
		return data != null ? data : "";
	}

	public void setData(String format, String data) {
		
		items.setData(format, data);

	}
}

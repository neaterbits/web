package com.test.web.jsapi.dom.dnd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.test.web.jsapi.dom.exceptions.InvalidState;
import com.test.web.jsapi.dom.exceptions.NotSupported;
import com.test.web.jsapi.file.FileList;
import com.test.web.jsapi.file.JSFile;
import com.test.web.jsengine.common.IJSObjectAsArray;

public class DataTransferItemList implements IJSObjectAsArray {

	// String or JSFile
	private List<DataTransferItem> items;

	private boolean isMutable;
	
	public DataTransferItemList() {
		this.items = new ArrayList<>();
	}

	boolean isMutable() {
		return isMutable;
	}

	void setMutable(boolean isMutable) {
		this.isMutable = isMutable;
	}

	public long getLength() {
		return items.size();
	}
	
	public void add(String data, String type) {
		if (items.stream().anyMatch(i -> i.getKindEnum() == DataTransferItemKind.STRING)) {
			throw new NotSupported("Already contains string");
		}
		
		items.add(new DataTransferItem(data, type));
	}

	public void add(JSFile file) {
		items.add(new DataTransferItem(file));
	}
	
	public void remove(int index) {
		
		if (!isMutable) {
			throw new InvalidState("Not in a mutable state");
		}

		items.remove(index);
	}
	
	public void clear() {
		items.clear();
	}
	
	FileList getFiles() {
		final List<JSFile> files = items.stream()
		.filter(i -> i.getKindEnum() == DataTransferItemKind.FILE)
		.map(i -> i.getFile())
		.collect(Collectors.toList());
		
		return new FileList(files);
	}
	
	void clearData(String format) {
		items.removeIf(i -> i.getType().equals(format));
	}
	
	String getData(String format) {
		return items.stream()
				.filter(i -> i.getType().equals(format) && i.getKindEnum() == DataTransferItemKind.STRING)
				.map(i -> i.getString())
				.findFirst()
				.orElse(null);
	}

	void setData(String format, String data) {
		
		boolean replaced = false;
		
		final DataTransferItem newItem = new DataTransferItem(data, format);
		
		for (int i = 0; i < items.size(); ++ i) {
			final DataTransferItem item = items.get(i);
			
			if (item.getType().equals(format) && item.getKindEnum() == DataTransferItemKind.STRING) {
				items.set(i, newItem);
				replaced = true;
				break;
			}
		}
		
		if ( ! replaced ) {
			items.add(newItem);
		}
	}
	
	@Override
	public Object getArrayElem(int index) {
		return items.get(index);
	}

	@Override
	public void setArrayElem(int index, Object value) {
		throw new UnsupportedOperationException("read-only");
	}

	@Override
	public long getArrayLength() {
		return items.size();
	}
}

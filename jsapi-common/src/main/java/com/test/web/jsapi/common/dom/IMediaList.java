package com.test.web.jsapi.common.dom;

public interface IMediaList {

	void appendMedium(String medium);
	
	void deleteMedium(String medium);
	
	int getLength();
	
	String getMediaText();
	
	String item(int index);
	
	void setMediaText(String mediaText);
}

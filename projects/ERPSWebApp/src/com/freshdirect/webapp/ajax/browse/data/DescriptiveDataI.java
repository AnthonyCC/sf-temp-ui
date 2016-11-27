package com.freshdirect.webapp.ajax.browse.data;

/**
 * common interface for descriptive fields of BrowseData and SectionData
 */
public interface DescriptiveDataI{

//these sections will be added once super departments are introduced	
//	String getHeaderText();
//	void setHeaderText(String headerText);
//	
//	String getHeaderImage();
//	void setHeaderImage(String headerImage);
	
	String getMedia();
	void setMedia(String media);
	
	String getMediaLocation();
	void setMediaLocation(String mediaLocation);
	
	String getMiddleMedia();
	void setMiddleMedia(String middleMedia);
}

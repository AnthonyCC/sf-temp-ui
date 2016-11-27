package com.freshdirect.mobileapi.model.data;

import java.util.ArrayList;
import java.util.List;

public class HelpTopic {
    private String title;
    private String path;
    private List<Object> entries = new ArrayList<Object>();
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
    public void setEntries(List<Object> entries) {
        this.entries = entries;
    }

    public List<Object> getEntries() {
		return entries;
	}
	
	public void addEntries(Object entries) {
	  this.entries.add(entries);
	}
}

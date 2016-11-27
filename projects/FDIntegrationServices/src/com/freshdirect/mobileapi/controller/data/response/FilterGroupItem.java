package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;

public class FilterGroupItem {
    private String label;

    private String id;
    
    private String subGroupName;
    private List<FilterGroupItem> subGroups;
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setSubGroupName (String subGString){
    	this.subGroupName = subGString;
    }
    
    public String getSubGroupName(){
    	return this.subGroupName;
    }
    
    public List<FilterGroupItem> getSubGroups(){
    	return subGroups;
    }
    
    public void setSubGroups(List<FilterGroupItem> newList){
    	this.subGroups = newList;
    }
    
    public void addItemToSubGroup(FilterGroupItem item){
    	if(this.subGroups == null)
    		this.subGroups = new ArrayList<FilterGroupItem>();
		this.subGroups.add(item);
    }
    
}

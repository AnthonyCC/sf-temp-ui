package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;



public class DepartmentSection  {

	private List<Category> categories = new ArrayList<Category>();
	
	private String sectionHeader;

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getSectionHeader() {
		return sectionHeader;
	}

	public void setSectionHeader(String sectionHeader) {
		this.sectionHeader = sectionHeader;
	}
	
	

}

package com.freshdirect.fdstore.coremetrics.tagmodel;


public class PageViewTagModel extends AbstractTagModel  {
	private String pageId; 
	private String categoryId;
	private String searchTerm; 
	private String searchResults;
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getSearchResults() {
		return searchResults;
	}
	public void setSearchResults(String searchResults) {
		this.searchResults = searchResults;
	} 
}
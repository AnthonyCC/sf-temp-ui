package com.freshdirect.fdstore.content;


public abstract class SearchQuery {

	private String originalTerm;
	
	protected SearchQuery(String originalTerm) {
		this.originalTerm = originalTerm;
		breakUp();
	}
	
	protected abstract void breakUp();
	
	public abstract String[] getTokens();
	
	public abstract String getNormalizedTerm();
	
	public abstract String getSearchTerm();
		
	public String getOriginalTerm() {
		return originalTerm;
	}
}

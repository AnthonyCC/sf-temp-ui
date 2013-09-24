package com.freshdirect.fdstore.content;

public interface FilteringValue {
	
	public String getName();
	
	public Integer getPosition();
	
	public boolean isMultiSelect();
	
	public boolean isShowIfEmpty();
	
	public String getDisplayName();
	
	public boolean isTempMenuNeeded();
	
	public boolean isMidProcessingNeeded();

}

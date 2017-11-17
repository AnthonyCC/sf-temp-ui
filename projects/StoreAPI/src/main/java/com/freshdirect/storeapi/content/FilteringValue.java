package com.freshdirect.storeapi.content;

import java.io.Serializable;

public interface FilteringValue extends Serializable {
	
	public String getName();
	
	public Integer getPosition();
	
	public boolean isMultiSelect();
	
	public boolean isShowIfEmpty();
	
	public String getDisplayName();
	
	public boolean isTempMenuNeeded();
	
	public boolean isMidProcessingNeeded();

}

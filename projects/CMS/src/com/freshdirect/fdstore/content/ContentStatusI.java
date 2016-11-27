package com.freshdirect.fdstore.content;

import java.util.Date;


/**
 * Interface for content nodes subject to currency and workflow status.
 * 
 */
public interface ContentStatusI {
	
	
	/**
	 * Get availability status.
	 * @return if node is available.
	 */
	public boolean isAvailable();
	
	/** 
	 * Get end date.
	 * @return end date.
	 */
	public Date getEndDate();
	
	/**
	 * Get start date.
	 * @return start date.
	 */
	public Date getStartDate();
	
};

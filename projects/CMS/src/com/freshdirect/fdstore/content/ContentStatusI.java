package com.freshdirect.fdstore.content;

import java.util.Date;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CmsContentNodeAdapter;


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

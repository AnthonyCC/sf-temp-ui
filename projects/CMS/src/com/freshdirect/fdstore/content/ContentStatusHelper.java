package com.freshdirect.fdstore.content;

import java.util.Date;

import com.freshdirect.fdstore.FDStoreProperties;


/**
 * Helper class to calculate content status.
 * Assumes CMS Atttributes
 * <ol>
 * <li>startDate
 * <li>endDate
 * <li>productionStatus
 * </ol>
 */
public class ContentStatusHelper implements ContentStatusI {
	
    private ContentNodeModelImpl node;
    
    private final static Date MAX_DATE = new Date(Long.MAX_VALUE);
    private final static Date MIN_DATE = new Date(0L);
    
    /**
     * Constructor.
     * @param adapter the content node using this helper
     */
	public ContentStatusHelper (ContentNodeModelImpl adapter) {
		node = adapter;
	}
	
    /**
     * Get start date.
     * Get start date from CMS "startDate" attribute. If null return Date(0).
     * @return end date
     * @see com.freshdirect.fdstore.content.ContentStatusI#getEndDate()
     */
	public Date getStartDate() {
	    Object a = node.getCmsAttributeValue("startDate");
	    return a == null ? MIN_DATE : (Date) a;
	}
	    
	/**
	 * Get end date.
	 * Get end date from CMS "endDate" attribute. If null return Date(Long.MAX_VALUE).
	 * @ return end date
	 */
	public Date getEndDate() {
		Object a = node.getCmsAttributeValue("endDate");
		return a == null ? MAX_DATE : (Date) a;
	}	
		
	/** 
	 * Get availability.
	 * 
	 * The node is available if preview mode is set or it is within start date and end date
	 * and its "productionStatus" attributes is active. 
	 * @return if node is available.
	 */
	public boolean isAvailable() {
		if (FDStoreProperties.getPreviewMode()) return true;
		Date now = new Date();
		return !now.before(getStartDate()) &&
		       !now.after(getEndDate()) &&
		       node.getAttribute("productionStatus", EnumProductionStatus.PENDING).equals(EnumProductionStatus.ACTIVE);
	}

}

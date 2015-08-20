package com.freshdirect.erp.model;

import com.freshdirect.erp.*;


/**
 * @author kkanuganti
 */
public class ErpMaterialBatchHistoryModel implements java.io.Serializable {
  
	private static final long serialVersionUID = -4241861289547682233L;

	/** Holds value of property approvalStatus. */
    private EnumApprovalStatus status = null;
    
    /** Holds value of property date created. */
    private java.sql.Timestamp createdDate = null;
   
    private int version;

	/**
	 * @return the status
	 */
	public EnumApprovalStatus getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EnumApprovalStatus status)
	{
		this.status = status;
	}

	/**
	 * @return the createdDate
	 */
	public java.sql.Timestamp getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(java.sql.Timestamp createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * @return the version
	 */
	public int getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version)
	{
		this.version = version;
	}
}

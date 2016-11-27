package com.freshdirect.referral.extole.model;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.referral.extole.EnumRafTransactionStatus;
import com.freshdirect.referral.extole.EnumRafTransactionType;

public class FDRafTransModel extends ModelSupport {
	
	/**
	 * Model class for CUST.RAF_TRANS table
	 * We store friend's transactions in this table and 
	 * update the trans_status depending on extole conversion response
	 */
	private static final long serialVersionUID = -3464740964354414465L;

	private String id;
	
	private String salesActionId;
	
	private EnumRafTransactionStatus transStatus;
	
	private EnumRafTransactionType transType;
	
	private String errorMessage;
	
	private String errorDetails;
	
	private String extoleEventId;
	
	private Date createTime;
	
	private Date transTime;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the salesActionId
	 */
	public String getSalesActionId() {
		return salesActionId;
	}

	/**
	 * @param salesActionId the salesActionId to set
	 */
	public void setSalesActionId(String salesActionId) {
		this.salesActionId = salesActionId;
	}

	/**
	 * @return the transStatus
	 */
	public EnumRafTransactionStatus getTransStatus() {
		return transStatus;
	}

	/**
	 * @param transStatus the transStatus to set
	 */
	public void setTransStatus(EnumRafTransactionStatus transStatus) {
		this.transStatus = transStatus;
	}

	/**
	 * @return the transType
	 */
	public EnumRafTransactionType getTransType() {
		return transType;
	}

	/**
	 * @param transType the transType to set
	 */
	public void setTransType(EnumRafTransactionType transType) {
		this.transType = transType;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorDetails
	 */
	public String getErrorDetails() {
		return errorDetails;
	}

	/**
	 * @param errorDetails the errorDetails to set
	 */
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	/**
	 * @return the extoleEventId
	 */
	public String getExtoleEventId() {
		return extoleEventId;
	}

	/**
	 * @param extoleEventId the extoleEventId to set
	 */
	public void setExtoleEventId(String extoleEventId) {
		this.extoleEventId = extoleEventId;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the transTime
	 */
	public Date getTransTime() {
		return transTime;
	}

	/**
	 * @param transTime the transTime to set
	 */
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	
	
	

}

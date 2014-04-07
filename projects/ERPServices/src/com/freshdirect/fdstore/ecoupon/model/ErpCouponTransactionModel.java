package com.freshdirect.fdstore.ecoupon.model;

import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.framework.core.ModelSupport;

public class ErpCouponTransactionModel extends ModelSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8428909030644301540L;
	private String saleActionId;
	private EnumCouponTransactionStatus tranStatus;
	private EnumCouponTransactionType tranType;
	private String errorMessage;
	private String errorDetails;
	private Date tranTime;
	private Date createTime;
	private String saleId;//non-persistant
	private List<ErpCouponTransactionDetailModel> tranDetails;
	
	/**
	 * @return the saleActionId
	 */
	public String getSaleActionId() {
		return saleActionId;
	}
	/**
	 * @param saleActionId the saleActionId to set
	 */
	public void setSaleActionId(String saleActionId) {
		this.saleActionId = saleActionId;
	}
	/**
	 * @return the tranStatus
	 */
	public EnumCouponTransactionStatus getTranStatus() {
		return tranStatus;
	}
	/**
	 * @param tranStatus the tranStatus to set
	 */
	public void setTranStatus(EnumCouponTransactionStatus tranStatus) {
		this.tranStatus = tranStatus;
	}
	/**
	 * @return the tranType
	 */
	public EnumCouponTransactionType getTranType() {
		return tranType;
	}
	/**
	 * @param tranType the tranType to set
	 */
	public void setTranType(EnumCouponTransactionType tranType) {
		this.tranType = tranType;
	}
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return (null !=errorMessage && errorMessage.length() > 255) ? errorMessage.substring(0, 254) : errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * @return the tranTime
	 */
	public Date getTranTime() {
		return tranTime;
	}
	/**
	 * @param tranTime the tranTime to set
	 */
	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
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
	 * @return the saleId
	 */
	public String getSaleId() {
		return saleId;
	}
	/**
	 * @param saleId the saleId to set
	 */
	public void setSaleId(String saleId) {
		this.saleId = saleId;
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
	 * @return the tranDetails
	 */
	public List<ErpCouponTransactionDetailModel> getTranDetails() {
		return tranDetails;
	}
	/**
	 * @param tranDetails the tranDetails to set
	 */
	public void setTranDetails(List<ErpCouponTransactionDetailModel> tranDetails) {
		this.tranDetails = tranDetails;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErpCouponTransactionModel [saleActionId=" + saleActionId
				+ ", tranStatus=" + tranStatus + ", tranType=" + tranType
				+ ", errorMessage=" + errorMessage + ", errorDetails="
				+ errorDetails + ", tranTime=" + tranTime + ", createTime="
				+ createTime + ", saleId=" + saleId + "]";
	}
	
	
}

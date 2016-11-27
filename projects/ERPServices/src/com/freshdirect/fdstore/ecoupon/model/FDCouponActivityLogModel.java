package com.freshdirect.fdstore.ecoupon.model;

import java.util.Date;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.framework.core.ModelSupport;

public class FDCouponActivityLogModel extends ModelSupport {

	private static final long serialVersionUID = -6104794435332971476L;
	
	private String customerId;
	private String fdUserId;
	private EnumCouponTransactionType transType;
	private Date startTime;
	private Date endTime;
	private String saleId;
	private String couponId;
	private String details;
	private EnumTransactionSource source;
	private String initiator;
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the fdUserId
	 */
	public String getFdUserId() {
		return fdUserId;
	}
	/**
	 * @param fdUserId the fdUserId to set
	 */
	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}
	/**
	 * @return the transType
	 */
	public EnumCouponTransactionType getTransType() {
		return transType;
	}
	/**
	 * @param transType the transType to set
	 */
	public void setTransType(EnumCouponTransactionType transType) {
		this.transType = transType;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
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
	 * @return the couponId
	 */
	public String getCouponId() {
		return couponId;
	}
	/**
	 * @param couponId the couponId to set
	 */
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	/**
	 * @return the source
	 */
	public EnumTransactionSource getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(EnumTransactionSource source) {
		this.source = source;
	}
	/**
	 * @return the initiator
	 */
	public String getInitiator() {
		return initiator;
	}
	/**
	 * @param initiator the initiator to set
	 */
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FDCouponActivityLogModel [customerId=" + customerId
				+ ", fdUserId=" + fdUserId + ", transType=" + transType
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", saleId=" + saleId + ", couponId=" + couponId
				+ ", details=" + details + ", source=" + source
				+ ", initiator=" + initiator + "]";
	}
	
	
}

package com.freshdirect.fdstore.ecoupon.model;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;

public class ErpCouponTransactionDetailModel extends ModelSupport {
	
	private static final long serialVersionUID = -3838597243137845098L;
	
	private String couponTransId;
	private String couponId;
	private Date transTime;
	private String discountAmt;
	private String couponLineId;
	
	/**
	 * @return the couponTransId
	 */
	public String getCouponTransId() {
		return couponTransId;
	}
	/**
	 * @param couponTransId the couponTransId to set
	 */
	public void setCouponTransId(String couponTransId) {
		this.couponTransId = couponTransId;
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
	/**
	 * @return the discount
	 */
	public String getDiscountAmt() {
		return discountAmt;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscountAmt(String discountAmt) {
		this.discountAmt = discountAmt;
	}
	/**
	 * @return the couponLineId
	 */
	public String getCouponLineId() {
		return couponLineId;
	}
	/**
	 * @param couponLineId the couponLineId to set
	 */
	public void setCouponLineId(String couponLineId) {
		this.couponLineId = couponLineId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErpCouponTransactionDetailModel [couponTransId="
				+ couponTransId + ", couponId=" + couponId + ", transTime="
				+ transTime + ", discountAmt=" + discountAmt
				+ ", couponLineId=" + couponLineId + "]";
	}
	
	
	

}

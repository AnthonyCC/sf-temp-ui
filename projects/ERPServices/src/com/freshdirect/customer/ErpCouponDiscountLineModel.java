package com.freshdirect.customer;

import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.framework.core.ModelSupport;

public class ErpCouponDiscountLineModel extends ModelSupport {

	private static final long serialVersionUID = -8327663166284185693L;
	
    private String orderLineId;
    private String couponId;
    private Integer version;
    private Integer requiredQuantity;
    private String couponDesc;
    private double discountAmt;
    private EnumCouponOfferType discountType;
    
    public ErpCouponDiscountLineModel(String couponId, double amount, Integer version,
			Integer requiredQuantity, String couponDesc,
			EnumCouponOfferType discountType) {
		super();
		this.couponId = couponId;
		this.discountAmt = amount;
		this.version = version;
		this.requiredQuantity = requiredQuantity;
		this.couponDesc = couponDesc;
		this.discountType = discountType;
	}
    
	public ErpCouponDiscountLineModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId() {
		return orderLineId;
	}
	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
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
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * @return the requiredQuantity
	 */
	public Integer getRequiredQuantity() {
		return requiredQuantity;
	}
	/**
	 * @param requiredQuantity the requiredQuantity to set
	 */
	public void setRequiredQuantity(Integer requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}
	/**
	 * @return the couponDesc
	 */
	public String getCouponDesc() {
		return couponDesc;
	}
	/**
	 * @param couponDesc the couponDesc to set
	 */
	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}
	/**
	 * @return the discountAmt
	 */
	public double getDiscountAmt() {
		return discountAmt;
	}
	/**
	 * @param discountAmt the discountAmt to set
	 */
	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}
	/**
	 * @return the discountType
	 */
	public EnumCouponOfferType getDiscountType() {
		return discountType;
	}
	/**
	 * @param discountType the discountType to set
	 */
	public void setDiscountType(EnumCouponOfferType discountType) {
		this.discountType = discountType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErpCouponDiscountLineModel [orderLineId=" + orderLineId
				+ ", couponId=" + couponId + ", version=" + version
				+ ", requiredQuantity=" + requiredQuantity + ", couponDesc="
				+ couponDesc + ", discountAmt=" + discountAmt
				+ ", discountType=" + discountType + "]";
	}
    
    
}



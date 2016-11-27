package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;

public class FDCustomerCouponHistoryInfo implements Serializable {

	private String orderLineId;
    private String couponId;
    private Integer version;
    private Integer requiredQuantity;
    private String couponDesc;
    private double discountAmt;
    private EnumCouponOfferType discountType;
    private String saleId;
    private EnumSaleStatus saleStatus;
    private Date saleDate;
    private Date deliveryDate;
    private String couponStatus;
    
	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getRequiredQuantity() {
		return requiredQuantity;
	}
	public void setRequiredQuantity(Integer requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}
	public String getCouponDesc() {
		return couponDesc;
	}
	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}
	public double getDiscountAmt() {
		return discountAmt;
	}
	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}
	public EnumCouponOfferType getDiscountType() {
		return discountType;
	}
	public void setDiscountType(EnumCouponOfferType discountType) {
		this.discountType = discountType;
	}
	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	public EnumSaleStatus getSaleStatus() {
		return saleStatus;
	}
	public void setSaleStatus(EnumSaleStatus saleStatus) {
		this.saleStatus = saleStatus;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}
    
    
}

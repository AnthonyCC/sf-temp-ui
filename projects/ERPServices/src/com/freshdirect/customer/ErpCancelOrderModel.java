package com.freshdirect.customer;

import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;

public class ErpCancelOrderModel extends ErpTransactionModel{
	
	double amount;
	private ErpCouponTransactionModel couponTransModel;
	
	public ErpCancelOrderModel(){
		super(EnumTransactionType.CANCEL_ORDER);
		this.amount = 0.0;
	}
	
	public double getAmount(){
		return this.amount;
	}
	
	public void setAmount(double amount){
		this.amount = amount;
	}

	/**
	 * @return the couponTransModel
	 */
	public ErpCouponTransactionModel getCouponTransModel() {
		return couponTransModel;
	}

	/**
	 * @param couponTransModel the couponTransModel to set
	 */
	public void setCouponTransModel(ErpCouponTransactionModel couponTransModel) {
		this.couponTransModel = couponTransModel;
	}

}

package com.freshdirect.customer;

import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;

public class ErpDeliveryConfirmModel extends ErpTransactionModel {
	
	private ErpCouponTransactionModel couponTransModel;
	
	public ErpDeliveryConfirmModel(){
		super(EnumTransactionType.DELIVERY_CONFIRM);
		super.setTransactionSource(EnumTransactionSource.TRANSPORTATION);
	}
	
    public double getAmount() {
        return 0.0;
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

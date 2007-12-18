package com.freshdirect.customer;

public class ErpDeliveryConfirmModel extends ErpTransactionModel {
	
	public ErpDeliveryConfirmModel(){
		super(EnumTransactionType.DELIVERY_CONFIRM);
		super.setTransactionSource(EnumTransactionSource.TRANSPORTATION);
	}
	
    public double getAmount() {
        return 0.0;
    }

}

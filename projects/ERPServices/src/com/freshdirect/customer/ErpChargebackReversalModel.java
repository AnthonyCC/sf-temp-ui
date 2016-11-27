package com.freshdirect.customer;

public class ErpChargebackReversalModel extends ErpChargebackModel{

	public ErpChargebackReversalModel() {
		super(EnumTransactionType.CHARGEBACK_REVERSAL);
	}
}

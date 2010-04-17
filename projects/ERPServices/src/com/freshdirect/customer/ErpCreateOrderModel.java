package com.freshdirect.customer;

public class ErpCreateOrderModel extends ErpAbstractOrderModel {

	private static final long	serialVersionUID	= -696173400744857436L;

	public ErpCreateOrderModel() {
        super(EnumTransactionType.CREATE_ORDER);
    }

}


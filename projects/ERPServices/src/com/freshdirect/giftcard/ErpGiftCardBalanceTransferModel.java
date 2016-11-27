package com.freshdirect.giftcard;

import com.freshdirect.customer.EnumTransactionType;

public class ErpGiftCardBalanceTransferModel extends ErpGiftCardTransModel {
	

	public ErpGiftCardTransactionModel getBalanceTransfer() {
		return balanceTransfer;
	}

	public void setBalanceTransfer(ErpGiftCardTransactionModel balanceTransfer) {
		this.balanceTransfer = balanceTransfer;
	}

	private ErpGiftCardTransactionModel balanceTransfer;
	
	public ErpGiftCardBalanceTransferModel()
	{
		super(EnumTransactionType.BALANCETRANSFER_GIFTCARD);
	}
					
}

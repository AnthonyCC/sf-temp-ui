/*
 * Created on Mar 31, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 *
 */
public class ErpFundsRedepositModel extends ErpAbstractSettlementModel {

	/** 
	 * Creates new ErpSettlementTransaction 
	 */
    public ErpFundsRedepositModel() {
		super(EnumTransactionType.FUNDS_REDEPOSIT);
    }

}

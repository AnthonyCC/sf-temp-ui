/*
 * Created on Mar 31, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 *
 */
public class ErpFailedChargeSettlementModel extends ErpAbstractSettlementModel {

    public ErpFailedChargeSettlementModel() {
		super(EnumTransactionType.SETTLEMENT_CHARGE_FAILED);
    }
	
}

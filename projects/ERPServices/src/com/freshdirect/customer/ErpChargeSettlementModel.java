/*
 * Created on Mar 31, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 *
 */
public class ErpChargeSettlementModel extends ErpAbstractSettlementModel {

	/** 
	 * Creates new ErpSettlementTransaction 
	 */
    public ErpChargeSettlementModel() {
		super(EnumTransactionType.SETTLEMENT_CHARGE);
    }
	
}

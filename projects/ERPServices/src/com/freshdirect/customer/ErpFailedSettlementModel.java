/*
 * Created on Mar 28, 2005
 *
 */
package com.freshdirect.customer;

/**
 * @author jng
 *
 */
public class ErpFailedSettlementModel extends ErpAbstractSettlementModel {

	/** 
	 * Creates new ErpSettlementTransaction 
	 */
    public ErpFailedSettlementModel() {
		super(EnumTransactionType.SETTLEMENT_FAILED);
    }
    
}

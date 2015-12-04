package com.freshdirect.customer;

import com.freshdirect.referral.extole.model.FDRafTransModel;

/**
 *
 * @author  knadeem
 * @version 
 */ 

public class ErpSettlementModel extends ErpAbstractSettlementModel {
	
	private FDRafTransModel rafTransModel;
	
    public ErpSettlementModel() {
		super(EnumTransactionType.SETTLEMENT);
    }

	/**
	 * @return the rafTransModel
	 */
	public FDRafTransModel getRafTransModel() {
		return rafTransModel;
	}

	/**
	 * @param rafTransModel the rafTransModel to set
	 */
	public void setRafTransModel(FDRafTransModel rafTransModel) {
		this.rafTransModel = rafTransModel;
	}
    
    
}

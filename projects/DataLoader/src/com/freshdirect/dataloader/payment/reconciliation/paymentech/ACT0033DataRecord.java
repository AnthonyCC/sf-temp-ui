package com.freshdirect.dataloader.payment.reconciliation.paymentech;

public class ACT0033DataRecord extends DFRDataRecord {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String actionCode;

	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
}

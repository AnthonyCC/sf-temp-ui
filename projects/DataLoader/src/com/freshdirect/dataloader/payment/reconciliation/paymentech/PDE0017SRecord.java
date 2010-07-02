package com.freshdirect.dataloader.payment.reconciliation.paymentech;

public class PDE0017SRecord extends DFRDataRecord {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String financialNonFinancial;

	public String getFinancialNonFinancial() {
		return financialNonFinancial;
	}
	public void setFinancialNonFinancial(String financialNonFinancial) {
		this.financialNonFinancial = financialNonFinancial;
	}
}

/*
 * Created on Mar 29, 2005
 */
package com.freshdirect.payment;


/**
 * @author jng
 */
public class EFTTransaction extends AbstractPaylinxTransaction {

	private String bankAccountNumber;
	private String bankId;
	private String accountType;

	public String getBankAccountNumber() {return this.bankAccountNumber;}
	public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
	
	public String getBankId() {return this.bankId;}
	public void setBankId(String bankId) { this.bankId = bankId; }

	public String getAccountType() {return this.accountType;}
	public void setAccountType(String accountType) { this.accountType = accountType; }
	
}


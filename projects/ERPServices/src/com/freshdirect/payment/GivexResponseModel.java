package com.freshdirect.payment;

import java.util.Date;

public class GivexResponseModel {

	public long getAuthCode() {
		return authCode;
	}
	public void setAuthCode(long authCode) {
		this.authCode = authCode;
	}
	public String getGivexNumber() {
		return givexNumber;
	}
	public void setGivexNumber(String givexNumber) {
		this.givexNumber = givexNumber;
	}
	public double getCertBalance() {
		return certBalance;
	}
	public void setCertBalance(double certBalance) {
		this.certBalance = certBalance;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	private long authCode;     
    private String givexNumber;     
    private double certBalance;     
    private Date expiryDate;
    private String securityCode;
    private double amount;
	
    
    public GivexResponseModel()
    {    	
    }
    
    public GivexResponseModel(long p_authCode
    		,     java.lang.String p_givexNumber
    		,     double p_certBalance
    		,     Date p_expiryDate
    		,     java.lang.String p_securityCode
    		,     double p_amount
    		)
    {	
    this.authCode = p_authCode;     
    this.givexNumber = p_givexNumber;     
    this.certBalance = p_certBalance;     
    this.expiryDate = p_expiryDate;
    this.securityCode = p_securityCode;
    this.amount = p_amount;
    }
	
}

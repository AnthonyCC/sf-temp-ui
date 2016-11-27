/*
 * FileHeader.java
 *
 * Created on January 21, 2002, 1:46 PM
 */

package com.freshdirect.payment.reconciliation.detail;

/**
 *
 * @author  mrose
 * @version 
 */
public class CCDetailOne extends DetailRecord {
    
    /** Holds value of property transactionCode. */
    private String transactionCode;
    
    /** Holds value of property productCode. */
    private String productCode;
    
    /** Holds value of property accountNumber. */
    private String accountNumber;
    
    /** Holds value of property FDMSReferenceNumber. */
    private String FDMSReferenceNumber;
    
    /** Holds value of property merchantReferenceNumber. */
    private String merchantReferenceNumber;
    
    /** Holds value of property transactionAmount. */
    private double transactionAmount;
    
    /** Holds value of property merchantNumber. */
    private String merchantNumber;
    
    /** Holds value of property mediaIndicator. */
    private String mediaIndicator;
    
    /** Holds value of property authCode. */
    private String authCode;
    
    /** Holds value of property authResponseCode. */
    private String authResponseCode;

    /** Creates new FileHeader */
    public CCDetailOne() {
        super(EnumDetailRecordType.CC_DETAIL_ONE);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Credit Card Detail One Record");
        sb.append("\nTransaction Code : ");
        sb.append(this.getTransactionCode());
        sb.append("\nProduct Code : ");
        sb.append(this.getProductCode());
        sb.append("\nAccount Number : ");
        sb.append(this.getAccountNumber());
        sb.append("\nFDMS Reference Number : ");
        sb.append(this.getFDMSReferenceNumber());
        sb.append("\nMerchant Reference Number : ");
        sb.append(this.getMerchantReferenceNumber());
        sb.append("\nTransaction Amount : ");
        sb.append(currencyFormatter.format(this.getTransactionAmount()));
        sb.append("\nMerchant Number : ");
        sb.append(this.getMerchantNumber());
        sb.append("\nMedia Indicator : ");
        sb.append(this.getMediaIndicator());
        sb.append("\nAuth Code : ");
        sb.append(this.getAuthCode());
        return sb.toString();
    }
    
    /** Getter for property transactionCode.
     * @return Value of property transactionCode.
     */
    public String getTransactionCode() {
        return transactionCode;
    }    
    
    /** Setter for property transactionCode.
     * @param transactionCode New value of property transactionCode.
     */
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
    
    /** Getter for property productCode.
     * @return Value of property productCode.
     */
    public String getProductCode() {
        return productCode;
    }
    
    /** Setter for property productCode.
     * @param productCode New value of property productCode.
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    /** Getter for property accountNumber.
     * @return Value of property accountNumber.
     */
    public String getAccountNumber() {
        return accountNumber;
    }
    
    /** Setter for property accountNumber.
     * @param accountNumber New value of property accountNumber.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    /** Getter for property FDMSReferenceNumber.
     * @return Value of property FDMSReferenceNumber.
     */
    public String getFDMSReferenceNumber() {
        return FDMSReferenceNumber;
    }
    
    /** Setter for property FDMSReferenceNumber.
     * @param FDMSReferenceNumber New value of property FDMSReferenceNumber.
     */
    public void setFDMSReferenceNumber(String FDMSReferenceNumber) {
        this.FDMSReferenceNumber = FDMSReferenceNumber;
    }
        
    /** Getter for property merchantReferenceNumber.
     * @return Value of property merchantReferenceNumber.
     */
    public String getMerchantReferenceNumber() {
        return merchantReferenceNumber;
    }
    
    /** Setter for property merchantReferenceNumber.
     * @param merchantReferenceNumber New value of property merchantReferenceNumber.
     */
    public void setMerchantReferenceNumber(String merchantReferenceNumber) {
        this.merchantReferenceNumber = merchantReferenceNumber;
    }
    
    /** Getter for property transactionAmount.
     * @return Value of property transactionAmount.
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }
    
    /** Setter for property transactionAmount.
     * @param transactionAmount New value of property transactionAmount.
     */
    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    
    /** Getter for property merchantNumber.
     * @return Value of property merchantNumber.
     */
    public String getMerchantNumber() {
        return merchantNumber;
    }
    
    /** Setter for property merchantNumber.
     * @param merchantNumber New value of property merchantNumber.
     */
    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }
    
    /** Getter for property mediaIndicator.
     * @return Value of property mediaIndicator.
     */
    public String getMediaIndicator() {
        return mediaIndicator;
    }
    
    /** Setter for property mediaIndicator.
     * @param mediaIndicator New value of property mediaIndicator.
     */
    public void setMediaIndicator(String mediaIndicator) {
        this.mediaIndicator = mediaIndicator;
    }
    
    /** Getter for property authCode.
     * @return Value of property authCode.
     */
    public String getAuthCode() {
        return authCode;
    }
    
    /** Setter for property authCode.
     * @param authCode New value of property authCode.
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
    
    /** Getter for property authResponseCode.
     * @return Value of property authResponseCode.
     */
    public String getAuthResponseCode() {
        return authResponseCode;
    }
    
    /** Setter for property authResponseCode.
     * @param authCode New value of property authResponseCode.
     */
    public void setAuthResponseCode(String authResponseCode) {
        this.authResponseCode = authResponseCode;
    }

}

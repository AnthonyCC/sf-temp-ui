/*
 * VisaMCSummary.java
 *
 * Created on January 21, 2002, 1:46 PM
 */

package com.freshdirect.payment.reconciliation.summary;

/**
 *
 * @author  mrose
 * @version 
 */
public class Chargeback extends SummaryRecord {
    
    /** Holds value of property merchantNumber. */
    private String merchantNumber;
    
    /** Holds value of property batchDate. */
    private java.util.Date batchDate;
    
    /** Holds value of property batchNumber. */
    private String batchNumber;
    
    /** Holds value of property chargebackAmount. */
    private double chargebackAmount;
    
    /** Holds value of property chargebackDiscount. */
    private double chargebackDiscount;
    
    /** Holds value of property chargebackReasonCode. */
    private String chargebackReasonCode;
    
    /** Holds value of property chargebackControlNumber. */
    private String chargebackControlNumber;
    
    /** Holds value of property cardholderNumber. */
    private String cardholderNumber;
    
    /** Holds value of property chargebackReferenceNumber. */
    private String chargebackReferenceNumber;
    
    /** Creates new FileHeader */
    public Chargeback() {
        super(EnumSummaryRecordType.CHARGEBACK);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Chargeback Record");
        sb.append("\nMerchant Number : ");
        sb.append(this.getMerchantNumber());
        sb.append("\nBatch Date : ");
        sb.append(this.getBatchDate());
        sb.append("\nBatch Number : ");
        sb.append(this.getBatchNumber());
        sb.append("\nChargeback Amount: ");
        sb.append(this.getChargebackAmount());
        sb.append("\nChargeback Control Number : ");
        sb.append(this.getChargebackControlNumber());
        sb.append("\nChargeback Discount : ");
        sb.append(this.getChargebackDiscount());
        sb.append("\nChargeback Reason Code : ");
        sb.append(this.getChargebackReasonCode());
        sb.append("\nCardholder Number : ");
        sb.append(this.getCardholderNumber());
        sb.append("\nChargeback Reference Number : ");
        sb.append(this.getChargebackReferenceNumber());
        return sb.toString();
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
    
    /** Getter for property batchDate.
     * @return Value of property batchDate.
     */
    public java.util.Date getBatchDate() {
        return batchDate;
    }
    
    /** Setter for property batchDate.
     * @param batchDate New value of property batchDate.
     */
    public void setBatchDate(java.util.Date batchDate) {
        this.batchDate = batchDate;
    }
    
    /** Getter for property batchNumber.
     * @return Value of property batchNumber.
     */
    public String getBatchNumber() {
        return batchNumber;
    }
    
    /** Setter for property batchNumber.
     * @param batchNumber New value of property batchNumber.
     */
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    /** Getter for property chargebackAmount.
     * @return Value of property chargebackAmount.
     */
    public double getChargebackAmount() {
        return chargebackAmount;
    }
    
    /** Setter for property chargebackAmount.
     * @param chargebackAmount New value of property chargebackAmount.
     */
    public void setChargebackAmount(double chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }
    
    /** Getter for property chargebackDiscount.
     * @return Value of property chargebackDiscount.
     */
    public double getChargebackDiscount() {
        return chargebackDiscount;
    }
    
    /** Setter for property chargebackDiscount.
     * @param chargebackDiscount New value of property chargebackDiscount.
     */
    public void setChargebackDiscount(double chargebackDiscount) {
        this.chargebackDiscount = chargebackDiscount;
    }
    
    /** Getter for property chargebackReasonCode.
     * @return Value of property chargebackReasonCode.
     */
    public String getChargebackReasonCode() {
        return chargebackReasonCode;
    }
    
    /** Setter for property chargebackReasonCode.
     * @param chargebackReasonCode New value of property chargebackReasonCode.
     */
    public void setChargebackReasonCode(String chargebackReasonCode) {
        this.chargebackReasonCode = chargebackReasonCode;
    }
    
    /** Getter for property chargebackControlNumber.
     * @return Value of property chargebackControlNumber.
     */
    public String getChargebackControlNumber() {
        return chargebackControlNumber;
    }
    
    /** Setter for property chargebackControlNumber.
     * @param chargebackControlNumber New value of property chargebackControlNumber.
     */
    public void setChargebackControlNumber(String chargebackControlNumber) {
        this.chargebackControlNumber = chargebackControlNumber;
    }
    
    /** Getter for property cardholderNumber.
     * @return Value of property cardholderNumber.
     */
    public String getCardholderNumber() {
        return cardholderNumber;
    }
    
    /** Setter for property cardholderNumber.
     * @param cardholderNumber New value of property cardholderNumber.
     */
    public void setCardholderNumber(String cardholderNumber) {
        this.cardholderNumber = cardholderNumber;
    }
    
    /** Getter for property chargebackReferenceNumber.
     * @return Value of property chargebackReferenceNumber.
     */
    public String getChargebackReferenceNumber() {
        return chargebackReferenceNumber;
    }
    
    /** Setter for property chargebackReferenceNumber.
     * @param chargebackReferenceNumber New value of property chargebackReferenceNumber.
     */
    public void setChargebackReferenceNumber(String chargebackReferenceNumber) {
        this.chargebackReferenceNumber = chargebackReferenceNumber;
    }
    
}

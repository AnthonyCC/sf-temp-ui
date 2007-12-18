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
public class ChargebackAddendum extends SummaryRecord {
    
    /** Holds value of property merchantRefernceNumber. */
    private String merchantReferenceNumber;
    
    /** Holds value of property chargebackDescription. */
    private String chargebackDescription;
    
    /** Holds value of property originalTransactionDate. */
    private java.util.Date originalTransactionDate;
    
    /** Holds value of property chargebackWorkDate. */
    private java.util.Date chargebackWorkDate;
    
    /** Holds value of property chargebackRespondDate. */
    private java.util.Date chargebackRespondDate;
    
    /** Holds value of property originalTransactionAmount. */
    private double originalTransactionAmount;
    
    /** Creates new FileHeader */
    public ChargebackAddendum() {
        super(EnumSummaryRecordType.CHARGEBACK_ADDENDUM);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Chargeback Addendum Record");
        sb.append("\nMerchant Reference Number : ");
        sb.append(this.getMerchantReferenceNumber());
        sb.append("\nChargeback Description : ");
        sb.append(this.getChargebackDescription());
        sb.append("\nOriginal Transaction Date : ");
        sb.append(this.getOriginalTransactionDate());
        sb.append("\nChargeback Work Date : ");
        sb.append(this.getChargebackWorkDate());
        sb.append("\nChargeback Respond Date : ");
        sb.append(this.getChargebackRespondDate());
        sb.append("\nOriginal Transaction Amount : ");
        sb.append(currencyFormatter.format(this.getOriginalTransactionAmount()));
        return sb.toString();
    }
    
    /** Getter for property merchantRefernceNumber.
     * @return Value of property merchantRefernceNumber.
     */
    public String getMerchantReferenceNumber() {
        return merchantReferenceNumber;
    }    
    
    /** Setter for property merchantRefernceNumber.
     * @param merchantRefernceNumber New value of property merchantRefernceNumber.
     */
    public void setMerchantReferenceNumber(String merchantReferenceNumber) {
        this.merchantReferenceNumber = merchantReferenceNumber;
    }    
    
    /** Getter for property chargebackDescription.
     * @return Value of property chargebackDescription.
     */
    public String getChargebackDescription() {
        return chargebackDescription;
    }
    
    /** Setter for property chargebackDescription.
     * @param chargebackDescription New value of property chargebackDescription.
     */
    public void setChargebackDescription(String chargebackDescription) {
        this.chargebackDescription = chargebackDescription;
    }
    
    /** Getter for property originalTransactionDate.
     * @return Value of property originalTransactionDate.
     */
    public java.util.Date getOriginalTransactionDate() {
        return originalTransactionDate;
    }
    
    /** Setter for property originalTransactionDate.
     * @param originalTransactionDate New value of property originalTransactionDate.
     */
    public void setOriginalTransactionDate(java.util.Date originalTransactionDate) {
        this.originalTransactionDate = originalTransactionDate;
    }
    
    /** Getter for property chargebackWorkDate.
     * @return Value of property chargebackWorkDate.
     */
    public java.util.Date getChargebackWorkDate() {
        return chargebackWorkDate;
    }
    
    /** Setter for property chargebackWorkDate.
     * @param chargebackWorkDate New value of property chargebackWorkDate.
     */
    public void setChargebackWorkDate(java.util.Date chargebackWorkDate) {
        this.chargebackWorkDate = chargebackWorkDate;
    }
    
    /** Getter for property chargebackRespondDate.
     * @return Value of property chargebackRespondDate.
     */
    public java.util.Date getChargebackRespondDate() {
        return chargebackRespondDate;
    }
    
    /** Setter for property chargebackRespondDate.
     * @param chargebackRespondDate New value of property chargebackRespondDate.
     */
    public void setChargebackRespondDate(java.util.Date chargebackRespondDate) {
        this.chargebackRespondDate = chargebackRespondDate;
    }
    
    /** Getter for property originalTransactionAmount.
     * @return Value of property originalTransactionAmount.
     */
    public double getOriginalTransactionAmount() {
        return originalTransactionAmount;
    }
    
    /** Setter for property originalTransactionAmount.
     * @param originalTransactionAmount New value of property originalTransactionAmount.
     */
    public void setOriginalTransactionAmount(double originalTransactionAmount) {
        this.originalTransactionAmount = originalTransactionAmount;
    }
    
}

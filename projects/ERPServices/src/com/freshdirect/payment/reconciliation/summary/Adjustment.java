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
public class Adjustment extends SummaryRecord {
    
    /** Holds value of property merchantNumber. */
    private String merchantNumber;
    
    /** Holds value of property batchDate. */
    private java.util.Date batchDate;
    
    /** Holds value of property batchNumber. */
    private String batchNumber;
    
    /** Holds value of property depositDate. */
    private java.util.Date depositDate;
    
    /** Holds value of property processDate. */
    private java.util.Date processDate;
    
    /** Holds value of property adjustmentAmount. */
    private double adjustmentAmount;
    
    /** Holds value of property cardholderNumber. */
    private String cardholderNumber;
    
    /** Holds value of property transactionDate. */
    private java.util.Date transactionDate;
    
    /** Holds value of property adjustmentDescription. */
    private String adjustmentDescription;
    
    /** Creates new FileHeader */
    public Adjustment() {
        super(EnumSummaryRecordType.ADJUSTMENT);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Adjustment Record");
        sb.append("\nMerchant Number : ");
        sb.append(this.getMerchantNumber());
        sb.append("\nBatch Date : ");
        sb.append(this.getBatchDate());
        sb.append("\nBatch Number : ");
        sb.append(this.getBatchNumber());
        sb.append("\nDeposit Date : ");
        sb.append(this.getDepositDate());
        sb.append("\nProcess Date : ");
        sb.append(this.getProcessDate());
        sb.append("\nAdjustment Amount : ");
        sb.append(currencyFormatter.format(this.getAdjustmentAmount()));
        sb.append("\nCardholder Number : ");
        sb.append(this.getCardholderNumber());
        sb.append("\nTransaction Date : ");
        sb.append(this.getTransactionDate());
        sb.append("\nAdjustment Description : ");
        sb.append(this.getAdjustmentDescription());
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
    
    /** Getter for property depositDate.
     * @return Value of property depositDate.
     */
    public java.util.Date getDepositDate() {
        return depositDate;
    }
    
    /** Setter for property depositDate.
     * @param depositDate New value of property depositDate.
     */
    public void setDepositDate(java.util.Date depositDate) {
        this.depositDate = depositDate;
    }
    
    /** Getter for property processDate.
     * @return Value of property processDate.
     */
    public java.util.Date getProcessDate() {
        return processDate;
    }
    
    /** Setter for property processDate.
     * @param processDate New value of property processDate.
     */
    public void setProcessDate(java.util.Date processDate) {
        this.processDate = processDate;
    }
    
    /** Getter for property adjustmentAmount.
     * @return Value of property adjustmentAmount.
     */
    public double getAdjustmentAmount() {
        return adjustmentAmount;
    }
    
    /** Setter for property adjustmentAmount.
     * @param adjustmentAmount New value of property adjustmentAmount.
     */
    public void setAdjustmentAmount(double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
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
    
    /** Getter for property transactionDate.
     * @return Value of property transactionDate.
     */
    public java.util.Date getTransactionDate() {
        return transactionDate;
    }
    
    /** Setter for property transactionDate.
     * @param transactionDate New value of property transactionDate.
     */
    public void setTransactionDate(java.util.Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    /** Getter for property adjustmentDescription.
     * @return Value of property adjustmentDescription.
     */
    public String getAdjustmentDescription() {
        return adjustmentDescription;
    }
    
    /** Setter for property adjustmentDescription.
     * @param adjustmentDescription New value of property adjustmentDescription.
     */
    public void setAdjustmentDescription(String adjustmentDescription) {
        this.adjustmentDescription = adjustmentDescription;
    }
    
}

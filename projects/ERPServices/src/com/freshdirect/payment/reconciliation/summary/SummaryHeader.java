/*
 * SummaryHeader.java
 *
 * Created on January 21, 2002, 1:46 PM
 */

package com.freshdirect.payment.reconciliation.summary;

/**
 *
 * @author  mrose
 * @version 
 */
public class SummaryHeader extends SummaryRecord {

    /** Holds value of property merchantNumber. */
    private String merchantNumber;    
    
    /** Holds value of property batchDate. */
    private java.util.Date batchDate;
    
    /** Holds value of property batchNumber. */
    private String batchNumber;
    
    /** Holds value of property processDate. */
    private java.util.Date processDate;
    
    /** Holds value of property depositDate. */
    private java.util.Date depositDate;
    
    /** Holds value of property netSalesAmount. */
    private double netSalesAmount;
    
    /** Holds value of property numberOfAdjustments. */
    private int numberOfAdjustments;
    
    /** Holds value of property adjustmentAmount. */
    private double adjustmentAmount;
    
    /** Holds value of property settlementFileCreationDate. */
    private java.util.Date settlementFileCreationDate;
    
    /** Creates new FileHeader */
    public SummaryHeader() {
        super(EnumSummaryRecordType.SUMMARY_HEADER);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Summary Header Record");
        sb.append("\nFDMS Merchant Number : ");
        sb.append(this.getMerchantNumber());
        sb.append("\nBatch Date : ");
        sb.append(this.getBatchDate());
        sb.append("\nBatch Number : ");
        sb.append(this.getBatchNumber());
        sb.append("\nProcess Date : ");
        sb.append(this.getProcessDate());
        sb.append("\nDeposit Date : ");
        sb.append(this.getDepositDate());
        sb.append("\nNet Sales Amount : ");
        sb.append(currencyFormatter.format(this.getNetSalesAmount()));
        sb.append("\nNumber of Adjustments : ");
        sb.append(this.getNumberOfAdjustments());
        sb.append("\nAdjustment Amount : ");
        sb.append(currencyFormatter.format(this.getAdjustmentAmount()));
        sb.append("\nSettlement File Creation Date : ");
        sb.append(this.getSettlementFileCreationDate());
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
    
    /** Getter for property netSalesAmount.
     * @return Value of property netSalesAmount.
     */
    public double getNetSalesAmount() {
        return netSalesAmount;
    }
    
    /** Setter for property netSalesAmount.
     * @param netSalesAmount New value of property netSalesAmount.
     */
    public void setNetSalesAmount(double netSalesAmount) {
        this.netSalesAmount = netSalesAmount;
    }
    
    /** Getter for property numberOfAdjustments.
     * @return Value of property numberOfAdjustments.
     */
    public int getNumberOfAdjustments() {
        return numberOfAdjustments;
    }
    
    /** Setter for property numberOfAdjustments.
     * @param numberOfAdjustments New value of property numberOfAdjustments.
     */
    public void setNumberOfAdjustments(int numberOfAdjustments) {
        this.numberOfAdjustments = numberOfAdjustments;
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
    
    /** Getter for property settlementFileCreationDate.
     * @return Value of property settlementFileCreationDate.
     */
    public java.util.Date getSettlementFileCreationDate() {
        return settlementFileCreationDate;
    }
    
    /** Setter for property settlementFileCreationDate.
     * @param settlementFileCreationDate New value of property settlementFileCreationDate.
     */
    public void setSettlementFileCreationDate(java.util.Date settlementFileCreationDate) {
        this.settlementFileCreationDate = settlementFileCreationDate;
    }
    
}

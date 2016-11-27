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
public class CCDetailTwo extends DetailRecord {
    
    /** Holds value of property transactionDate. */
    private java.util.Date transactionDate;
    
    /** Holds value of property adjustmentReason. */
    private String adjustmentReason;
    
    /** Holds value of property terminalId. */
    private String terminalId;
    
    /** Holds value of property settlementFileCreationDate. */
    private java.util.Date settlementFileCreationDate;
    
    /** Creates new FileHeader */
    public CCDetailTwo() {
        super(EnumDetailRecordType.CC_DETAIL_TWO);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Credit Card Detail Two Record");
        sb.append("\nTransaction Date : ");
        sb.append(this.getTransactionDate());
        sb.append("\nAdjustment Reason : ");
        sb.append(this.getAdjustmentReason());
        sb.append("\nTerminal ID : ");
        sb.append(this.getTerminalId());
        sb.append("\nSettlement File Creation Date : ");
        sb.append(this.getSettlementFileCreationDate());
        return sb.toString();
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
    
    /** Getter for property adjustmentReason.
     * @return Value of property adjustmentReason.
     */
    public String getAdjustmentReason() {
        return adjustmentReason;
    }
    
    /** Setter for property adjustmentReason.
     * @param adjustmentReason New value of property adjustmentReason.
     */
    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }
    
    /** Getter for property terminalId.
     * @return Value of property terminalId.
     */
    public String getTerminalId() {
        return terminalId;
    }
    
    /** Setter for property terminalId.
     * @param terminalId New value of property terminalId.
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

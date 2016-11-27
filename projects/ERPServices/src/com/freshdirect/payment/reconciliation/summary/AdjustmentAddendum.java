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
public class AdjustmentAddendum extends SummaryRecord {
    
    /** Holds value of property referenceNumber. */
    private String referenceNumber;    
    
    /** Holds value of property sequenceNumber. */
    private String sequenceNumber;
    
    /** Holds value of property netSalesAmount. */
    private double netSalesAmount;
    
    /** Creates new FileHeader */
    public AdjustmentAddendum() {
        super(EnumSummaryRecordType.ADJUSTMENT_ADDENDUM);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Adjustment Addenudum Record");
        sb.append("\nReference Number : ");
        sb.append(this.getReferenceNumber());
        sb.append("\nSequence Number : ");
        sb.append(this.getSequenceNumber());
        sb.append("\nNet Sales Amount : ");
        sb.append(currencyFormatter.format(this.getNetSalesAmount()));
        return sb.toString();
    }
    
    /** Getter for property referenceNumber.
     * @return Value of property referenceNumber.
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }    
    
    /** Setter for property referenceNumber.
     * @param referenceNumber New value of property referenceNumber.
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
    
}

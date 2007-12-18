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
public class AmexJCBUnbundled extends SummaryRecord {
    
    /** Holds value of property unbundledOption. */
    private String unbundledOption;
    
    /** Holds value of property amexInterchangeFee. */
    private double amexInterchangeFee;
    
    /** Holds value of property amexAssessmentFee. */
    private double amexAssessmentFee;
    
    /** Holds value of property amexTransactionFee. */
    private double amexTransactionFee;
    
    /** Holds value of property JCBInterchangeFee. */
    private double JCBInterchangeFee;
    
    /** Holds value of property JCBAssessmentFee. */
    private double JCBAssessmentFee;
    
    /** Holds value of property JCBTransactionFee. */
    private double JCBTransactionFee;
    
    /** Creates new FileHeader */
    public AmexJCBUnbundled() {
        super(EnumSummaryRecordType.AMEX_JCB_UNBUNDLED);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Amex-JCB Unbundled Record");
        sb.append("\nUnbundled Option : ");
        sb.append(this.getUnbundledOption());
        sb.append("\nAmex Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getAmexInterchangeFee()));
        sb.append("\nAmex Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getAmexAssessmentFee()));
        sb.append("\nAmex Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getAmexTransactionFee()));
        sb.append("\nJCB Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getJCBInterchangeFee()));
        sb.append("\nJCB Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getJCBAssessmentFee()));
        sb.append("\nJCB Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getJCBTransactionFee()));
        return sb.toString();
    }
    
    /** Getter for property unbundledOption.
     * @return Value of property unbundledOption.
     */
    public String getUnbundledOption() {
        return unbundledOption;
    }    
    
    /** Setter for property unbundledOption.
     * @param unbundledOption New value of property unbundledOption.
     */
    public void setUnbundledOption(String unbundledOption) {
        this.unbundledOption = unbundledOption;
    }    
    
    /** Getter for property amexInterchangeFee.
     * @return Value of property amexInterchangeFee.
     */
    public double getAmexInterchangeFee() {
        return amexInterchangeFee;
    }
    
    /** Setter for property amexInterchangeFee.
     * @param amexInterchangeFee New value of property amexInterchangeFee.
     */
    public void setAmexInterchangeFee(double amexInterchangeFee) {
        this.amexInterchangeFee = amexInterchangeFee;
    }
    
    /** Getter for property amexAssessmentFee.
     * @return Value of property amexAssessmentFee.
     */
    public double getAmexAssessmentFee() {
        return amexAssessmentFee;
    }
    
    /** Setter for property amexAssessmentFee.
     * @param amexAssessmentFee New value of property amexAssessmentFee.
     */
    public void setAmexAssessmentFee(double amexAssessmentFee) {
        this.amexAssessmentFee = amexAssessmentFee;
    }
    
    /** Getter for property amexTransactionFee.
     * @return Value of property amexTransactionFee.
     */
    public double getAmexTransactionFee() {
        return amexTransactionFee;
    }
    
    /** Setter for property amexTransactionFee.
     * @param amexTransactionFee New value of property amexTransactionFee.
     */
    public void setAmexTransactionFee(double amexTransactionFee) {
        this.amexTransactionFee = amexTransactionFee;
    }
    
    /** Getter for property JCBInterchangeFee.
     * @return Value of property JCBInterchangeFee.
     */
    public double getJCBInterchangeFee() {
        return JCBInterchangeFee;
    }
    
    /** Setter for property JCBInterchangeFee.
     * @param JCBInterchangeFee New value of property JCBInterchangeFee.
     */
    public void setJCBInterchangeFee(double JCBInterchangeFee) {
        this.JCBInterchangeFee = JCBInterchangeFee;
    }
    
    /** Getter for property JCBAssessmentFee.
     * @return Value of property JCBAssessmentFee.
     */
    public double getJCBAssessmentFee() {
        return JCBAssessmentFee;
    }
    
    /** Setter for property JCBAssessmentFee.
     * @param JCBAssessmentFee New value of property JCBAssessmentFee.
     */
    public void setJCBAssessmentFee(double JCBAssessmentFee) {
        this.JCBAssessmentFee = JCBAssessmentFee;
    }
    
    /** Getter for property JCBTransactionFee.
     * @return Value of property JCBTransactionFee.
     */
    public double getJCBTransactionFee() {
        return JCBTransactionFee;
    }
    
    /** Setter for property JCBTransactionFee.
     * @param JCBTransactionFee New value of property JCBTransactionFee.
     */
    public void setJCBTransactionFee(double JCBTransactionFee) {
        this.JCBTransactionFee = JCBTransactionFee;
    }
    
}

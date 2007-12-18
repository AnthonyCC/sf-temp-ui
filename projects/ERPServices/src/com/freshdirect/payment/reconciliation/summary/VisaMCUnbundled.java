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
public class VisaMCUnbundled extends SummaryRecord {
    
    /** Holds value of property unbundledOption. */
    private String unbundledOption;
    
    /** Holds value of property visaInterchangeFee. */
    private double visaInterchangeFee;
    
    /** Holds value of property visaAssessmentFee. */
    private double visaAssessmentFee;
    
    /** Holds value of property visaTransactionFee. */
    private double visaTransactionFee;
    
    /** Holds value of property mcInterchangeFee. */
    private double mcInterchangeFee;
    
    /** Holds value of property mcAssessmentFee. */
    private double mcAssessmentFee;
    
    /** Holds value of property mcTransactionFee. */
    private double mcTransactionFee;
    
    /** Creates new FileHeader */
    public VisaMCUnbundled() {
        super(EnumSummaryRecordType.VISA_MC_UNBUNDLED);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Visa-MasterCard Unbundled Record");
        sb.append("\nUnbundled Option : ");
        sb.append(this.getUnbundledOption());
        sb.append("\nVisa Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getVisaInterchangeFee()));
        sb.append("\nVisa Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getVisaAssessmentFee()));
        sb.append("\nVisa Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getVisaTransactionFee()));
        sb.append("\nMC Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getMCInterchangeFee()));
        sb.append("\nMC Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getMCAssessmentFee()));
        sb.append("\nMC Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getMCTransactionFee()));
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
    
    /** Getter for property visaInterchangeFee.
     * @return Value of property visaInterchangeFee.
     */
    public double getVisaInterchangeFee() {
        return visaInterchangeFee;
    }
    
    /** Setter for property visaInterchangeFee.
     * @param visaInterchangeFee New value of property visaInterchangeFee.
     */
    public void setVisaInterchangeFee(double visaInterchangeFee) {
        this.visaInterchangeFee = visaInterchangeFee;
    }
    
    /** Getter for property visaAssessmentFee.
     * @return Value of property visaAssessmentFee.
     */
    public double getVisaAssessmentFee() {
        return visaAssessmentFee;
    }
    
    /** Setter for property visaAssessmentFee.
     * @param visaAssessmentFee New value of property visaAssessmentFee.
     */
    public void setVisaAssessmentFee(double visaAssessmentFee) {
        this.visaAssessmentFee = visaAssessmentFee;
    }
    
    /** Getter for property visaTransactionFee.
     * @return Value of property visaTransactionFee.
     */
    public double getVisaTransactionFee() {
        return visaTransactionFee;
    }
    
    /** Setter for property visaTransactionFee.
     * @param visaTransactionFee New value of property visaTransactionFee.
     */
    public void setVisaTransactionFee(double visaTransactionFee) {
        this.visaTransactionFee = visaTransactionFee;
    }
    
    /** Getter for property mcInterchangeFee.
     * @return Value of property mcInterchangeFee.
     */
    public double getMCInterchangeFee() {
        return mcInterchangeFee;
    }
    
    /** Setter for property mcInterchangeFee.
     * @param mcInterchangeFee New value of property mcInterchangeFee.
     */
    public void setMCInterchangeFee(double mcInterchangeFee) {
        this.mcInterchangeFee = mcInterchangeFee;
    }
    
    /** Getter for property mcAssessmentFee.
     * @return Value of property mcAssessmentFee.
     */
    public double getMCAssessmentFee() {
        return mcAssessmentFee;
    }
    
    /** Setter for property mcAssessmentFee.
     * @param mcAssessmentFee New value of property mcAssessmentFee.
     */
    public void setMCAssessmentFee(double mcAssessmentFee) {
        this.mcAssessmentFee = mcAssessmentFee;
    }
    
    /** Getter for property mcTransactionFee.
     * @return Value of property mcTransactionFee.
     */
    public double getMCTransactionFee() {
        return mcTransactionFee;
    }
    
    /** Setter for property mcTransactionFee.
     * @param mcTransactionFee New value of property mcTransactionFee.
     */
    public void setMCTransactionFee(double mcTransactionFee) {
        this.mcTransactionFee = mcTransactionFee;
    }
    
}

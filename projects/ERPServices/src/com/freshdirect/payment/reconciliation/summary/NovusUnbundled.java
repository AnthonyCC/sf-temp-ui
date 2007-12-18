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
public class NovusUnbundled extends SummaryRecord {
    
    /** Holds value of property unbundledOption. */
    private String unbundledOption;
    
    /** Holds value of property novusInterchangeFee. */
    private double novusInterchangeFee;
    
    /** Holds value of property novusAssessmentFee. */
    private double novusAssessmentFee;
    
    /** Holds value of property novusTransactionFee. */
    private double novusTransactionFee;
    
    /** Creates new FileHeader */
    public NovusUnbundled() {
        super(EnumSummaryRecordType.NOVUS_UNBUNDLED);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> NOVUS Unbundled Record");
        sb.append("\nUnbundled Option : ");
        sb.append(this.getUnbundledOption());
        sb.append("\nNovus Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getNovusInterchangeFee()));
        sb.append("\nNovus Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getNovusAssessmentFee()));
        sb.append("\nNovus Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getNovusTransactionFee()));
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
    
    /** Getter for property novusInterchangeFee.
     * @return Value of property novusInterchangeFee.
     */
    public double getNovusInterchangeFee() {
        return novusInterchangeFee;
    }
    
    /** Setter for property novusInterchangeFee.
     * @param novusInterchangeFee New value of property novusInterchangeFee.
     */
    public void setNovusInterchangeFee(double novusInterchangeFee) {
        this.novusInterchangeFee = novusInterchangeFee;
    }
    
    /** Getter for property novusAssessmentFee.
     * @return Value of property novusAssessmentFee.
     */
    public double getNovusAssessmentFee() {
        return novusAssessmentFee;
    }
    
    /** Setter for property novusAssessmentFee.
     * @param novusAssessmentFee New value of property novusAssessmentFee.
     */
    public void setNovusAssessmentFee(double novusAssessmentFee) {
        this.novusAssessmentFee = novusAssessmentFee;
    }
    
    /** Getter for property novusTransactionFee.
     * @return Value of property novusTransactionFee.
     */
    public double getNovusTransactionFee() {
        return novusTransactionFee;
    }
    
    /** Setter for property novusTransactionFee.
     * @param novusTransactionFee New value of property novusTransactionFee.
     */
    public void setNovusTransactionFee(double novusTransactionFee) {
        this.novusTransactionFee = novusTransactionFee;
    }
    
}

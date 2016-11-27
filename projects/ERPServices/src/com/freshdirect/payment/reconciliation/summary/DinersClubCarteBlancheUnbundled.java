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
public class DinersClubCarteBlancheUnbundled extends SummaryRecord {
    
    /** Holds value of property unbundledOption. */
    private String unbundledOption;
    
    /** Holds value of property dinersClubInterchangeFee. */
    private double dinersClubInterchangeFee;
    
    /** Holds value of property dinersClubAssessmentFee. */
    private double dinersClubAssessmentFee;
    
    /** Holds value of property dinersClubTransactionFee. */
    private double dinersClubTransactionFee;
    
    /** Holds value of property carteBlancheInterchangeFee. */
    private double carteBlancheInterchangeFee;
    
    /** Holds value of property carteBlancheAssessmentFee. */
    private double carteBlancheAssessmentFee;
    
    /** Holds value of property carteBlancheTransactionFee. */
    private double carteBlancheTransactionFee;
    
    /** Creates new FileHeader */
    public DinersClubCarteBlancheUnbundled() {
        super(EnumSummaryRecordType.DC_CB_UNBUNDLED);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Diners Club - Carte Blanche Unbundled Record");
        sb.append("\nUnbundled Option : ");
        sb.append(this.getUnbundledOption());
        sb.append("\nDiners Club Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getDinersClubInterchangeFee()));
        sb.append("\nDiners Club Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getDinersClubAssessmentFee()));
        sb.append("\nDiners Club Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getDinersClubTransactionFee()));
        sb.append("\nCarte Blanche Interchange Fee : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheInterchangeFee()));
        sb.append("\nCarte Blanche Assessment Fee : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheAssessmentFee()));
        sb.append("\nCarte Blanche Transaction Fee : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheTransactionFee()));
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
    
    /** Getter for property dinersClubInterchangeFee.
     * @return Value of property dinersClubInterchangeFee.
     */
    public double getDinersClubInterchangeFee() {
        return dinersClubInterchangeFee;
    }
    
    /** Setter for property dinersClubInterchangeFee.
     * @param dinersClubInterchangeFee New value of property dinersClubInterchangeFee.
     */
    public void setDinersClubInterchangeFee(double dinersClubInterchangeFee) {
        this.dinersClubInterchangeFee = dinersClubInterchangeFee;
    }
    
    /** Getter for property dinersClubAssessmentFee.
     * @return Value of property dinersClubAssessmentFee.
     */
    public double getDinersClubAssessmentFee() {
        return dinersClubAssessmentFee;
    }
    
    /** Setter for property dinersClubAssessmentFee.
     * @param dinersClubAssessmentFee New value of property dinersClubAssessmentFee.
     */
    public void setDinersClubAssessmentFee(double dinersClubAssessmentFee) {
        this.dinersClubAssessmentFee = dinersClubAssessmentFee;
    }
    
    /** Getter for property dinersClubTransactionFee.
     * @return Value of property dinersClubTransactionFee.
     */
    public double getDinersClubTransactionFee() {
        return dinersClubTransactionFee;
    }
    
    /** Setter for property dinersClubTransactionFee.
     * @param dinersClubTransactionFee New value of property dinersClubTransactionFee.
     */
    public void setDinersClubTransactionFee(double dinersClubTransactionFee) {
        this.dinersClubTransactionFee = dinersClubTransactionFee;
    }
    
    /** Getter for property carteBlancheInterchangeFee.
     * @return Value of property carteBlancheInterchangeFee.
     */
    public double getCarteBlancheInterchangeFee() {
        return carteBlancheInterchangeFee;
    }
    
    /** Setter for property carteBlancheInterchangeFee.
     * @param carteBlancheInterchangeFee New value of property carteBlancheInterchangeFee.
     */
    public void setCarteBlancheInterchangeFee(double carteBlancheInterchangeFee) {
        this.carteBlancheInterchangeFee = carteBlancheInterchangeFee;
    }
    
    /** Getter for property carteBlancheAssessmentFee.
     * @return Value of property carteBlancheAssessmentFee.
     */
    public double getCarteBlancheAssessmentFee() {
        return carteBlancheAssessmentFee;
    }
    
    /** Setter for property carteBlancheAssessmentFee.
     * @param carteBlancheAssessmentFee New value of property carteBlancheAssessmentFee.
     */
    public void setCarteBlancheAssessmentFee(double carteBlancheAssessmentFee) {
        this.carteBlancheAssessmentFee = carteBlancheAssessmentFee;
    }
    
    /** Getter for property carteBlancheTransactionFee.
     * @return Value of property carteBlancheTransactionFee.
     */
    public double getCarteBlancheTransactionFee() {
        return carteBlancheTransactionFee;
    }
    
    /** Setter for property carteBlancheTransactionFee.
     * @param carteBlancheTransactionFee New value of property carteBlancheTransactionFee.
     */
    public void setCarteBlancheTransactionFee(double carteBlancheTransactionFee) {
        this.carteBlancheTransactionFee = carteBlancheTransactionFee;
    }
    
}

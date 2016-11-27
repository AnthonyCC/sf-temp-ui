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
public class DinersClubCarteBlancheSummary extends SummaryRecord {
    
    /** Holds value of property numberOfCarteBlancheItems. */
    private int numberOfCarteBlancheItems;
    
    /** Holds value of property carteBlancheNetSalesAmount. */
    private double carteBlancheNetSalesAmount;
    
    /** Holds value of property carteBlancheFDMSDiscountAmount. */
    private double carteBlancheFDMSDiscountAmount;
    
    /** Holds value of property carteBlancheIssuerDiscountAmount. */
    private double carteBlancheIssuerDiscountAmount;
    
    /** Holds value of property numberOfJDBItems. */
    private int numberOfDinersClubItems;
    
    /** Holds value of property DinersClubNetSalesAmount. */
    private double dinersClubNetSalesAmount;
    
    /** Holds value of property DinersClubFDMSDiscountAmount. */
    private double dinersClubFDMSDiscountAmount;
    
    /** Holds value of property DinersClubIssuerDiscountAmount. */
    private double dinersClubIssuerDiscountAmount;
    
    /** Creates new FileHeader */
    public DinersClubCarteBlancheSummary() {
        super(EnumSummaryRecordType.DC_CB_SUMMARY);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Diners Club - Carte Blanche Summary Record");
        sb.append("\nNumber of Diners Club Items : ");
        sb.append(this.getNumberOfDinersClubItems());
        sb.append("\nDiners Club Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getDinersClubNetSalesAmount()));
        sb.append("\nDiners Club FDMS Discount Amount : ");
        sb.append(currencyFormatter.format(this.getDinersClubFDMSDiscountAmount()));
        sb.append("\nDiners Club Issuer Discount Amount : ");
        sb.append(currencyFormatter.format(this.getDinersClubIssuerDiscountAmount()));
        sb.append("\nNumber Of carteBlanche Items : ");
        sb.append(this.getNumberOfCarteBlancheItems());
        sb.append("\nCarte Blanche Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheNetSalesAmount()));
        sb.append("\nCarte Blanche FDMS Discount Amount : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheFDMSDiscountAmount()));
        sb.append("\nCarte Blanche Issuer Discount Amount : ");
        sb.append(currencyFormatter.format(this.getCarteBlancheIssuerDiscountAmount()));
        return sb.toString();
    }
    
    /** Getter for property numberOfCarteBlancheItems.
     * @return Value of property numberOfCarteBlancheItems.
     */
    public int getNumberOfCarteBlancheItems() {
        return numberOfCarteBlancheItems;
    }    
    
    /** Setter for property numberOfCarteBlancheItems.
     * @param numberOfCarteBlancheItems New value of property numberOfCarteBlancheItems.
     */
    public void setNumberOfCarteBlancheItems(int numberOfCarteBlancheItems) {
        this.numberOfCarteBlancheItems = numberOfCarteBlancheItems;
    }    
    
    /** Getter for property carteBlancheNetSalesAmount.
     * @return Value of property carteBlancheNetSalesAmount.
     */
    public double getCarteBlancheNetSalesAmount() {
        return carteBlancheNetSalesAmount;
    }
    
    /** Setter for property carteBlancheNetSalesAmount.
     * @param carteBlancheNetSalesAmount New value of property carteBlancheNetSalesAmount.
     */
    public void setCarteBlancheNetSalesAmount(double carteBlancheNetSalesAmount) {
        this.carteBlancheNetSalesAmount = carteBlancheNetSalesAmount;
    }
    
    /** Getter for property carteBlancheFDMSDiscountAmount.
     * @return Value of property carteBlancheFDMSDiscountAmount.
     */
    public double getCarteBlancheFDMSDiscountAmount() {
        return carteBlancheFDMSDiscountAmount;
    }
    
    /** Setter for property carteBlancheFDMSDiscountAmount.
     * @param carteBlancheFDMSDiscountAmount New value of property carteBlancheFDMSDiscountAmount.
     */
    public void setCarteBlancheFDMSDiscountAmount(double carteBlancheFDMSDiscountAmount) {
        this.carteBlancheFDMSDiscountAmount = carteBlancheFDMSDiscountAmount;
    }
    
    /** Getter for property carteBlancheIssuerDiscountAmount.
     * @return Value of property carteBlancheIssuerDiscountAmount.
     */
    public double getCarteBlancheIssuerDiscountAmount() {
        return carteBlancheIssuerDiscountAmount;
    }
    
    /** Setter for property carteBlancheIssuerDiscountAmount.
     * @param carteBlancheIssuerDiscountAmount New value of property carteBlancheIssuerDiscountAmount.
     */
    public void setCarteBlancheIssuerDiscountAmount(double carteBlancheIssuerDiscountAmount) {
        this.carteBlancheIssuerDiscountAmount = carteBlancheIssuerDiscountAmount;
    }
    
    /** Getter for property numberOfJDBItems.
     * @return Value of property numberOfJDBItems.
     */
    public int getNumberOfDinersClubItems() {
        return numberOfDinersClubItems;
    }
    
    /** Setter for property numberOfDinersClubItems.
     * @param numberOfDinersClubItems New value of property numberOfDinersClubItems.
     */
    public void setNumberOfDinersClubItems(int numberOfDinersClubItems) {
        this.numberOfDinersClubItems = numberOfDinersClubItems;
    }
    
    /** Getter for property DinersClubNetSalesAmount.
     * @return Value of property DinersClubNetSalesAmount.
     */
    public double getDinersClubNetSalesAmount() {
        return dinersClubNetSalesAmount;
    }
    
    /** Setter for property DinersClubNetSalesAmount.
     * @param DinersClubNetSalesAmount New value of property DinersClubNetSalesAmount.
     */
    public void setDinersClubNetSalesAmount(double dinersClubNetSalesAmount) {
        this.dinersClubNetSalesAmount = dinersClubNetSalesAmount;
    }
    
    /** Getter for property DinersClubFDMSDiscountAmount.
     * @return Value of property DinersClubFDMSDiscountAmount.
     */
    public double getDinersClubFDMSDiscountAmount() {
        return dinersClubFDMSDiscountAmount;
    }
    
    /** Setter for property DinersClubFDMSDiscountAmount.
     * @param DinersClubFDMSDiscountAmount New value of property DinersClubFDMSDiscountAmount.
     */
    public void setDinersClubFDMSDiscountAmount(double dinersClubFDMSDiscountAmount) {
        this.dinersClubFDMSDiscountAmount = dinersClubFDMSDiscountAmount;
    }
    
    /** Getter for property DinersClubIssuerDiscountAmount.
     * @return Value of property DinersClubIssuerDiscountAmount.
     */
    public double getDinersClubIssuerDiscountAmount() {
        return dinersClubIssuerDiscountAmount;
    }
    
    /** Setter for property DinersClubIssuerDiscountAmount.
     * @param DinersClubIssuerDiscountAmount New value of property DinersClubIssuerDiscountAmount.
     */
    public void setDinersClubIssuerDiscountAmount(double dinersClubIssuerDiscountAmount) {
        this.dinersClubIssuerDiscountAmount = dinersClubIssuerDiscountAmount;
    }
    
}

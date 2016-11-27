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
public class AmexJCBSummary extends SummaryRecord {
    
    /** Holds value of property numberOfAmexItems. */
    private int numberOfAmexItems;
    
    /** Holds value of property amexNetSalesAmount. */
    private double amexNetSalesAmount;
    
    /** Holds value of property amexFDMSDiscountAmount. */
    private double amexFDMSDiscountAmount;
    
    /** Holds value of property amexIssuerDiscountAmount. */
    private double amexIssuerDiscountAmount;
    
    /** Holds value of property numberOfJDBItems. */
    private int numberOfJCBItems;
    
    /** Holds value of property JCBNetSalesAmount. */
    private double JCBNetSalesAmount;
    
    /** Holds value of property JCBFDMSDiscountAmount. */
    private double JCBFDMSDiscountAmount;
    
    /** Holds value of property JCBIssuerDiscountAmount. */
    private double JCBIssuerDiscountAmount;
    
    /** Creates new FileHeader */
    public AmexJCBSummary() {
        super(EnumSummaryRecordType.AMEX_JCB_SUMMARY);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Amex-JCB Summary Record");
        sb.append("\nNumber Of Amex Items : ");
        sb.append(this.getNumberOfAmexItems());
        sb.append("\nAmex Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getAmexNetSalesAmount()));
        sb.append("\nAmex FDMS Discount Amount : ");
        sb.append(currencyFormatter.format(this.getAmexFDMSDiscountAmount()));
        sb.append("\nAmex Issuer Discount Amount : ");
        sb.append(currencyFormatter.format(this.getAmexIssuerDiscountAmount()));
        sb.append("\nNumber of JCB Items : ");
        sb.append(this.getNumberOfJCBItems());
        sb.append("\nJCB Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getJCBNetSalesAmount()));
        sb.append("\nJCB FDMS Discount Amount : ");
        sb.append(currencyFormatter.format(this.getJCBFDMSDiscountAmount()));
        sb.append("\nJCB Issuer Discount Amount : ");
        sb.append(currencyFormatter.format(this.getJCBIssuerDiscountAmount()));
        return sb.toString();
    }
    
    /** Getter for property numberOfAmexItems.
     * @return Value of property numberOfAmexItems.
     */
    public int getNumberOfAmexItems() {
        return numberOfAmexItems;
    }    
    
    /** Setter for property numberOfAmexItems.
     * @param numberOfAmexItems New value of property numberOfAmexItems.
     */
    public void setNumberOfAmexItems(int numberOfAmexItems) {
        this.numberOfAmexItems = numberOfAmexItems;
    }    
    
    /** Getter for property amexNetSalesAmount.
     * @return Value of property amexNetSalesAmount.
     */
    public double getAmexNetSalesAmount() {
        return amexNetSalesAmount;
    }
    
    /** Setter for property amexNetSalesAmount.
     * @param amexNetSalesAmount New value of property amexNetSalesAmount.
     */
    public void setAmexNetSalesAmount(double amexNetSalesAmount) {
        this.amexNetSalesAmount = amexNetSalesAmount;
    }
    
    /** Getter for property amexFDMSDiscountAmount.
     * @return Value of property amexFDMSDiscountAmount.
     */
    public double getAmexFDMSDiscountAmount() {
        return amexFDMSDiscountAmount;
    }
    
    /** Setter for property amexFDMSDiscountAmount.
     * @param amexFDMSDiscountAmount New value of property amexFDMSDiscountAmount.
     */
    public void setAmexFDMSDiscountAmount(double amexFDMSDiscountAmount) {
        this.amexFDMSDiscountAmount = amexFDMSDiscountAmount;
    }
    
    /** Getter for property amexIssuerDiscountAmount.
     * @return Value of property amexIssuerDiscountAmount.
     */
    public double getAmexIssuerDiscountAmount() {
        return amexIssuerDiscountAmount;
    }
    
    /** Setter for property amexIssuerDiscountAmount.
     * @param amexIssuerDiscountAmount New value of property amexIssuerDiscountAmount.
     */
    public void setAmexIssuerDiscountAmount(double amexIssuerDiscountAmount) {
        this.amexIssuerDiscountAmount = amexIssuerDiscountAmount;
    }
    
    /** Getter for property numberOfJDBItems.
     * @return Value of property numberOfJDBItems.
     */
    public int getNumberOfJCBItems() {
        return numberOfJCBItems;
    }
    
    /** Setter for property numberOfJCBItems.
     * @param numberOfJCBItems New value of property numberOfJCBItems.
     */
    public void setNumberOfJCBItems(int numberOfJCBItems) {
        this.numberOfJCBItems = numberOfJCBItems;
    }
    
    /** Getter for property JCBNetSalesAmount.
     * @return Value of property JCBNetSalesAmount.
     */
    public double getJCBNetSalesAmount() {
        return JCBNetSalesAmount;
    }
    
    /** Setter for property JCBNetSalesAmount.
     * @param JCBNetSalesAmount New value of property JCBNetSalesAmount.
     */
    public void setJCBNetSalesAmount(double JCBNetSalesAmount) {
        this.JCBNetSalesAmount = JCBNetSalesAmount;
    }
    
    /** Getter for property JCBFDMSDiscountAmount.
     * @return Value of property JCBFDMSDiscountAmount.
     */
    public double getJCBFDMSDiscountAmount() {
        return JCBFDMSDiscountAmount;
    }
    
    /** Setter for property JCBFDMSDiscountAmount.
     * @param JCBFDMSDiscountAmount New value of property JCBFDMSDiscountAmount.
     */
    public void setJCBFDMSDiscountAmount(double JCBFDMSDiscountAmount) {
        this.JCBFDMSDiscountAmount = JCBFDMSDiscountAmount;
    }
    
    /** Getter for property JCBIssuerDiscountAmount.
     * @return Value of property JCBIssuerDiscountAmount.
     */
    public double getJCBIssuerDiscountAmount() {
        return JCBIssuerDiscountAmount;
    }
    
    /** Setter for property JCBIssuerDiscountAmount.
     * @param JCBIssuerDiscountAmount New value of property JCBIssuerDiscountAmount.
     */
    public void setJCBIssuerDiscountAmount(double JCBIssuerDiscountAmount) {
        this.JCBIssuerDiscountAmount = JCBIssuerDiscountAmount;
    }
    
}

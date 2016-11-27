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
public class NovusSummary extends SummaryRecord {
    
    /** Holds value of property numberOfNovusItems. */
    private int numberOfNovusItems;
    
    /** Holds value of property novusNetSalesAmount. */
    private double novusNetSalesAmount;
    
    /** Holds value of property novusFDMSDiscountAmount. */
    private double novusFDMSDiscountAmount;
    
    /** Holds value of property novusIssuerDiscountAmount. */
    private double novusIssuerDiscountAmount;
    
    /** Creates new FileHeader */
    public NovusSummary() {
        super(EnumSummaryRecordType.NOVUS_SUMMARY);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> NOVUS Summary Record");
        sb.append("\nNumber Of Novus Items : ");
        sb.append(this.getNumberOfNovusItems());
        sb.append("\nNovus Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getNovusNetSalesAmount()));
        sb.append("\nNovus FDMS Discount Amount : ");
        sb.append(currencyFormatter.format(this.getNovusFDMSDiscountAmount()));
        sb.append("\nNovus Issuer Discount Amount : ");
        sb.append(currencyFormatter.format(this.getNovusIssuerDiscountAmount()));
        return sb.toString();
    }
    
    /** Getter for property numberOfNovusItems.
     * @return Value of property numberOfNovusItems.
     */
    public int getNumberOfNovusItems() {
        return numberOfNovusItems;
    }    
    
    /** Setter for property numberOfNovusItems.
     * @param numberOfNovusItems New value of property numberOfNovusItems.
     */
    public void setNumberOfNovusItems(int numberOfNovusItems) {
        this.numberOfNovusItems = numberOfNovusItems;
    }    
    
    /** Getter for property novusNetSalesAmount.
     * @return Value of property novusNetSalesAmount.
     */
    public double getNovusNetSalesAmount() {
        return novusNetSalesAmount;
    }
    
    /** Setter for property novusNetSalesAmount.
     * @param novusNetSalesAmount New value of property novusNetSalesAmount.
     */
    public void setNovusNetSalesAmount(double novusNetSalesAmount) {
        this.novusNetSalesAmount = novusNetSalesAmount;
    }
    
    /** Getter for property novusFDMSDiscountAmount.
     * @return Value of property novusFDMSDiscountAmount.
     */
    public double getNovusFDMSDiscountAmount() {
        return novusFDMSDiscountAmount;
    }
    
    /** Setter for property novusFDMSDiscountAmount.
     * @param novusFDMSDiscountAmount New value of property novusFDMSDiscountAmount.
     */
    public void setNovusFDMSDiscountAmount(double novusFDMSDiscountAmount) {
        this.novusFDMSDiscountAmount = novusFDMSDiscountAmount;
    }
    
    /** Getter for property novusIssuerDiscountAmount.
     * @return Value of property novusIssuerDiscountAmount.
     */
    public double getNovusIssuerDiscountAmount() {
        return novusIssuerDiscountAmount;
    }
    
    /** Setter for property novusIssuerDiscountAmount.
     * @param novusIssuerDiscountAmount New value of property novusIssuerDiscountAmount.
     */
    public void setNovusIssuerDiscountAmount(double novusIssuerDiscountAmount) {
        this.novusIssuerDiscountAmount = novusIssuerDiscountAmount;
    }
    
}

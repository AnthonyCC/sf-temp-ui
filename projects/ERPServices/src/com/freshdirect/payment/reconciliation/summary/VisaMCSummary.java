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
public class VisaMCSummary extends SummaryRecord {
    
    /** Holds value of property numberOfItems. */
    private int numberOfVisaItems;
    
    /** Holds value of property visaNetSalesAmount. */
    private double visaNetSalesAmount;
    
    /** Holds value of property visaDiscount. */
    private double visaDiscountAmount;
    
    /** Holds value of property numberOfMCItems. */
    private int numberOfMCItems;
    
    /** Holds value of property mcNetSalesAmount. */
    private double mcNetSalesAmount;
    
    /** Holds value of property mcDiscountAmount. */
    private double mcDiscountAmount;
    
    /** Creates new FileHeader */
    public VisaMCSummary() {
        super(EnumSummaryRecordType.VISA_MC_SUMMARY);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Visa-MasterCard Summary Record");
        sb.append("\nNumber of Visa items : ");
        sb.append(this.getNumberOfVisaItems());
        sb.append("\nVisa Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getVisaNetSalesAmount()));
        sb.append("\nVisa Discount Amount : ");
        sb.append(currencyFormatter.format(this.getVisaDiscountAmount()));
        sb.append("\nNumber of MasterCard items : ");
        sb.append(this.getNumberOfMCItems());
        sb.append("\nMC Net Sales Amount : ");
        sb.append(currencyFormatter.format(this.getMCNetSalesAmount()));
        sb.append("\nMC Discount Amount : ");
        sb.append(currencyFormatter.format(this.getMCDiscountAmount()));
        return sb.toString();
    }
    
    /** Getter for property numberOfItems.
     * @return Value of property numberOfItems.
     */
    public int getNumberOfVisaItems() {
        return numberOfVisaItems;
    }    
    
    /** Setter for property numberOfItems.
     * @param numberOfItems New value of property numberOfItems.
     */
    public void setNumberOfVisaItems(int numberOfVisaItems) {
        this.numberOfVisaItems = numberOfVisaItems;
    }    
    
    /** Getter for property visaNetSalesAmount.
     * @return Value of property visaNetSalesAmount.
     */
    public double getVisaNetSalesAmount() {
        return visaNetSalesAmount;
    }
    
    /** Setter for property visaNetSalesAmount.
     * @param visaNetSalesAmount New value of property visaNetSalesAmount.
     */
    public void setVisaNetSalesAmount(double visaNetSalesAmount) {
        this.visaNetSalesAmount = visaNetSalesAmount;
    }
    
    /** Getter for property visaDiscount.
     * @return Value of property visaDiscount.
     */
    public double getVisaDiscountAmount() {
        return visaDiscountAmount;
    }
    
    /** Setter for property visaDiscount.
     * @param visaDiscount New value of property visaDiscount.
     */
    public void setVisaDiscountAmount(double visaDiscountAmount) {
        this.visaDiscountAmount = visaDiscountAmount;
    }
    
    /** Getter for property numberOfMCItems.
     * @return Value of property numberOfMCItems.
     */
    public int getNumberOfMCItems() {
        return numberOfMCItems;
    }
    
    /** Setter for property numberOfMCItems.
     * @param numberOfMCItems New value of property numberOfMCItems.
     */
    public void setNumberOfMCItems(int numberOfMCItems) {
        this.numberOfMCItems = numberOfMCItems;
    }
    
    /** Getter for property mcNetSalesAmount.
     * @return Value of property mcNetSalesAmount.
     */
    public double getMCNetSalesAmount() {
        return mcNetSalesAmount;
    }
    
    /** Setter for property mcNetSalesAmount.
     * @param mcNetSalesAmount New value of property mcNetSalesAmount.
     */
    public void setMCNetSalesAmount(double mcNetSalesAmount) {
        this.mcNetSalesAmount = mcNetSalesAmount;
    }
    
    /** Getter for property mcDiscountAmount.
     * @return Value of property mcDiscountAmount.
     */
    public double getMCDiscountAmount() {
        return mcDiscountAmount;
    }
    
    /** Setter for property mcDiscountAmount.
     * @param mcDiscountAmount New value of property mcDiscountAmount.
     */
    public void setMCDiscountAmount(double mcDiscountAmount) {
        this.mcDiscountAmount = mcDiscountAmount;
    }
    
}

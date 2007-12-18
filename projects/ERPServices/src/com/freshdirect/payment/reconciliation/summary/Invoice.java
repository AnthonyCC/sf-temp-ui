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
public class Invoice extends SummaryRecord {
    
    /** Holds value of property merchantNumber. */
    private String merchantNumber;    
    
    /** Holds value of property invoiceDate. */
    private java.util.Date invoiceDate;
    
    /** Holds value of property invoiceAmount. */
    private double invoiceAmount;
    
    /** Holds value of property invoiceNumber. */
    private String invoiceNumber;
    
    /** Holds value of property invoiceDescription. */
    private String invoiceDescription;
    
    /** Creates new FileHeader */
    public Invoice() {
        super(EnumSummaryRecordType.INVOICE);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> Invoice Record");
        sb.append("\nMerchant Number : ");
        sb.append(this.getMerchantNumber());
        sb.append("\nInvoice Date : ");
        sb.append(this.getInvoiceDate());
        sb.append("\nInvoice Amount : ");
        sb.append(currencyFormatter.format(this.getInvoiceAmount()));
        sb.append("\nInvoice Number : ");
        sb.append(this.getInvoiceNumber());
        sb.append("\nInvoice Description : ");
        sb.append(this.getInvoiceDescription());
        return sb.toString();
    }
    
    /** Getter for property merchantNumber.
     * @return Value of property merchantNumber.
     */
    public String getMerchantNumber() {
        return merchantNumber;
    }    
    
    /** Setter for property merchantNumber.
     * @param merchantNumber New value of property merchantNumber.
     */
    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }    
    
    /** Getter for property invoiceDate.
     * @return Value of property invoiceDate.
     */
    public java.util.Date getInvoiceDate() {
        return invoiceDate;
    }
    
    /** Setter for property invoiceDate.
     * @param invoiceDate New value of property invoiceDate.
     */
    public void setInvoiceDate(java.util.Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    /** Getter for property invoiceAmount.
     * @return Value of property invoiceAmount.
     */
    public double getInvoiceAmount() {
        return invoiceAmount;
    }
    
    /** Setter for property invoiceAmount.
     * @param invoiceAmount New value of property invoiceAmount.
     */
    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }
    
    /** Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    /** Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    /** Getter for property invoiceDescription.
     * @return Value of property invoiceDescription.
     */
    public String getInvoiceDescription() {
        return invoiceDescription;
    }
    
    /** Setter for property invoiceDescription.
     * @param invoiceDescription New value of property invoiceDescription.
     */
    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }
    
}

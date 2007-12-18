/*
 * FileHeader.java
 *
 * Created on January 21, 2002, 1:46 PM
 */

package com.freshdirect.payment.reconciliation.summary;

/**
 *
 * @author  mrose
 * @version 
 */
public class FileTrailer extends SummaryRecord {

    /** Holds value of property fileRecordCount. */
    private int fileRecordCount;    
    
    /** Holds value of property fileNetAmount. */
    private double fileNetAmount;
    
    /** Holds value of property fileNetDeposit. */
    private double fileNetDeposit;
    
    /** Holds value for the actual deposited amount in our bank */
    private double filePostedAmount;
    
    /** Creates new FileHeader */
    public FileTrailer() {
        super(EnumSummaryRecordType.FILE_TRAILER);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> File Trailer Record");
        sb.append("\nFile Record Count : ");
        sb.append(this.getFileRecordCount());
        sb.append("\nFile Net Amount : ");
        sb.append(currencyFormatter.format(this.getFileNetAmount()));
        sb.append("\nFile Net Deposit : ");
        sb.append(currencyFormatter.format(this.getFileNetDeposit()));
        sb.append("\nFile Posted Amount : ");
        sb.append(currencyFormatter.format(this.getFilePostedAmount()));
        return sb.toString();
    }
    
    /** Getter for property fileRecordCount.
     * @return Value of property fileRecordCount.
     */
    public int getFileRecordCount() {
        return fileRecordCount;
    }
    
    /** Setter for property fileRecordCount.
     * @param fileRecordCount New value of property fileRecordCount.
     */
    public void setFileRecordCount(int fileRecordCount) {
        this.fileRecordCount = fileRecordCount;
    }
    
    /** Getter for property fileNetAmount.
     * @return Value of property fileNetAmount.
     */
    public double getFileNetAmount() {
        return fileNetAmount;
    }
    
    /** Setter for property fileNetAmount.
     * @param fileNetAmount New value of property fileNetAmount.
     */
    public void setFileNetAmount(double fileNetAmount) {
        this.fileNetAmount = fileNetAmount;
    }
    
    /** Getter for property fileNetDeposit.
     * @return Value of property fileNetDeposit.
     */
    public double getFileNetDeposit() {
        return fileNetDeposit;
    }
    
    /** Setter for property fileNetDeposit.
     * @param fileNetDeposit New value of property fileNetDeposit.
     */
    public void setFileNetDeposit(double fileNetDeposit) {
        this.fileNetDeposit = fileNetDeposit;
    }
    
    public double getFilePostedAmount(){
    	return this.filePostedAmount;
    }
    
    public void setFilePostedAmount(double filePostedAmount){
    	this.filePostedAmount = filePostedAmount;
    }
    
}

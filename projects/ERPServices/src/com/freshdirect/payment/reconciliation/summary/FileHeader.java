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
public class FileHeader extends SummaryRecord {

    /** Holds value of property processingPeriodStartDate. */
    private java.util.Date processingPeriodStartDate;
    
    /** Holds value of property processingPeriodEndDate. */
    private java.util.Date processingPeriodEndDate;
    
    /** Holds value of property fileID. */
    private String fileID;
    
    /** Creates new FileHeader */
    public FileHeader() {
        super(EnumSummaryRecordType.FILE_HEADER);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> File Header Record");
        sb.append("\nFile ID : ");
        sb.append(this.getFileID());
        sb.append("\nProcessing Start Date : ");
        sb.append(this.getProcessingPeriodStartDate());
        sb.append("\nProcessing End Date : ");
        sb.append(this.getProcessingPeriodEndDate());
        return sb.toString();
    }
    
    /** Getter for property processingPeriodStartDate.
     * @return Value of property processingPeriodStartDate.
     */
    public java.util.Date getProcessingPeriodStartDate() {
        return processingPeriodStartDate;
    }    
    
    /** Setter for property processingPeriodStartDate.
     * @param processingPeriodStartDate New value of property processingPeriodStartDate.
     */
    public void setProcessingPeriodStartDate(java.util.Date processingPeriodStartDate) {
        this.processingPeriodStartDate = processingPeriodStartDate;
    }    

    /** Getter for property processingPeriodEndDate.
     * @return Value of property processingPeriodEndDate.
     */
    public java.util.Date getProcessingPeriodEndDate() {
        return processingPeriodEndDate;
    }
    
    /** Setter for property processingPeriodEndDate.
     * @param processingPeriodEndDate New value of property processingPeriodEndDate.
     */
    public void setProcessingPeriodEndDate(java.util.Date processingPeriodEndDate) {
        this.processingPeriodEndDate = processingPeriodEndDate;
    }
    
    /** Getter for property fileID.
     * @return Value of property fileID.
     */
    public String getFileID() {
        return fileID;
    }
    
    /** Setter for property fileID.
     * @param fileID New value of property fileID.
     */
    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
    
    
    
}

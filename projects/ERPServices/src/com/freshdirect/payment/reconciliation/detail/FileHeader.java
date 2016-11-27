/*
 * FileHeader.java
 *
 * Created on January 21, 2002, 1:46 PM
 */

package com.freshdirect.payment.reconciliation.detail;

/**
 *
 * @author  mrose
 * @version 
 */
public class FileHeader extends DetailRecord {

    /** Holds value of property fileCreationDate. */
    private java.util.Date fileCreationDate;    
    
    /** Holds value of property fileFormatCode. */
    private String fileFormatCode;
    
    /** Holds value of property processingMessage. */
    private String processingMessage;
    
    /** Holds value of property fileId. */
    private String fileId;
    
    /** Creates new FileHeader */
    public FileHeader() {
        super(EnumDetailRecordType.FILE_HEADER);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> File Header Record");
        sb.append("\nFile ID : ");
        sb.append(this.getFileId());
        sb.append("\nFile Creation Date : ");
        sb.append(this.getFileCreationDate());
        sb.append("\nFile Format Code : ");
        sb.append(this.getFileFormatCode());
        sb.append("\nProcessing Message : ");
        sb.append(this.getProcessingMessage());
        return sb.toString();
    }
    
    /** Getter for property fileCreationDate.
     * @return Value of property fileCreationDate.
     */
    public java.util.Date getFileCreationDate() {
        return fileCreationDate;
    }    
    
    /** Setter for property fileCreationDate.
     * @param fileCreationDate New value of property fileCreationDate.
     */
    public void setFileCreationDate(java.util.Date fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }    
    
    /** Getter for property fileFormatCode.
     * @return Value of property fileFormatCode.
     */
    public String getFileFormatCode() {
        return fileFormatCode;
    }
    
    /** Setter for property fileFormatCode.
     * @param fileFormatCode New value of property fileFormatCode.
     */
    public void setFileFormatCode(String fileFormatCode) {
        this.fileFormatCode = fileFormatCode;
    }
    
    /** Getter for property processingMessage.
     * @return Value of property processingMessage.
     */
    public String getProcessingMessage() {
        return processingMessage;
    }
    
    /** Setter for property processingMessage.
     * @param processingMessage New value of property processingMessage.
     */
    public void setProcessingMessage(String processingMessage) {
        this.processingMessage = processingMessage;
    }
    
    /** Getter for property fileId.
     * @return Value of property fileId.
     */
    public String getFileId() {
        return fileId;
    }
    
    /** Setter for property fileId.
     * @param fileId New value of property fileId.
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
}

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
public class FileTrailer extends DetailRecord {
    
    /** Holds value of property recordCount. */
    private int recordCount;
    
    /** Creates new FileHeader */
    public FileTrailer() {
        super(EnumDetailRecordType.FILE_TRAILER);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n====> File Trailer Record");
        sb.append("\nRecord Count : ");
        sb.append(this.getRecordCount());
        return sb.toString();
    }
    
    /** Getter for property recordCount.
     * @return Value of property recordCount.
     */
    public int getRecordCount() {
        return recordCount;
    }    
    
    /** Setter for property recordCount.
     * @param recordCount New value of property recordCount.
     */
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }    
    
}

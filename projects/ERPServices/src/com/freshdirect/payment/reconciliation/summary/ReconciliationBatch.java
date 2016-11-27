/*
 * ReconciliationBatch.java
 *
 * Created on January 21, 2002, 3:02 PM
 */

package com.freshdirect.payment.reconciliation.summary;

/**
 *
 * @author  mrose
 * @version 
 */
public class ReconciliationBatch {

    /** Holds value of property header. */
    private FileHeader header;
    
    /** Holds value of property summary. */
    private SummaryHeader summary;
    
    /** Creates new ReconciliationBatch */
    public ReconciliationBatch() {
    }

    /** Getter for property header.
     * @return Value of property header.
     */
    public FileHeader getHeader() {
        return header;
    }
    
    /** Setter for property header.
     * @param header New value of property header.
     */
    public void setHeader(FileHeader header) {
        this.header = header;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Batch Header:\n");
        sb.append(header.toString());
        sb.append("Batch Summary:\n");
        sb.append(summary.toString());
        
        return sb.toString();
    }
    
    /** Getter for property summary.
     * @return Value of property summary.
     */
    public SummaryHeader getSummary() {
        return summary;
    }
    
    /** Setter for property summary.
     * @param summary New value of property summary.
     */
    public void setSummary(SummaryHeader summary) {
        this.summary = summary;
    }
    
}

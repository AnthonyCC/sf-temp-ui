/*
 * BatchModel.java
 *
 * Created on September 4, 2001, 2:02 PM
 */

package com.freshdirect.erp.model;

import com.freshdirect.erp.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class BatchHistoryModel implements java.io.Serializable {

    /** Holds value of property approvalStatus. */
    private EnumBatchStatus status = null;
    
    /** Holds value of property dateApproved. */
    private java.sql.Timestamp statusDate = null;
    
    /** Holds value of property approvedBy. */
    private String user = null;
    
    /** Holds value of property description. */
    private String notes = null;
    
    public BatchHistoryModel() {
    }
    
    /** Creates new BatchModel */
    public BatchHistoryModel(EnumBatchStatus status, java.sql.Timestamp statusDate, String user, String notes) {
        this.status = status;
        this.statusDate = statusDate;
        this.user = user;
        this.notes = notes;
    }

    /** Getter for property approvalStatus.
     * @return Value of property approvalStatus.
     */
    public EnumBatchStatus getStatus() {
        return status;
    }
    
    /** Setter for property approvalStatus.
     * @param approvalStatus New value of property approvalStatus.
     */
    public void setStatus(EnumBatchStatus status) {
        this.status = status;
    }
    
    /** Getter for property dateApproved.
     * @return Value of property dateApproved.
     */
    public java.sql.Timestamp getStatusDate() {
        return statusDate;
    }
    
    public void setStatusDate(java.sql.Timestamp statusDate) {
        this.statusDate =statusDate;
    }
    
    /** Getter for property approvedBy.
     * @return Value of property approvedBy.
     */
    public String getUser() {
        return user;
    }
    
    /** Setter for property approvedBy.
     * @param approvedBy New value of property approvedBy.
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public String getNotes() {
        return notes;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}

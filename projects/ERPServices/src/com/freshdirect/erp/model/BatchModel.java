/*
 * BatchModel.java
 *
 * Created on September 4, 2001, 2:02 PM
 */

package com.freshdirect.erp.model;

import java.util.List;
import java.util.ArrayList;

import com.freshdirect.erp.*;
 
/**
 *
 * @author  mrose
 * @version 
 */
public class BatchModel implements java.io.Serializable {

    /** Holds value of property batchNumber. */
    private int batchNumber;
    
    /** Holds value of property history. */
    private List history;
    
    /** Holds value of property description. */
    private String description;
    
    /** Creates new BatchModel */
    public BatchModel() {
        this.history = new ArrayList();
    }

    /** Getter for property batchNumber.
     * @return Value of property batchNumber.
     */
    public int getBatchNumber() {
        return this.batchNumber;
    }
    
    /** Setter for property batchNumber.
     * @param batchNumber New value of property batchNumber.
     */
    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    /** Getter for property history.
     * @return Value of property history.
     */
    public List getHistory() {
        return this.history;
    }
    
    public void setHistory(List history) {
        this.history = history;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void updateStatus(BatchHistoryModel bhm) {
        this.history.add(bhm);
    }
    
    public java.util.Date getDateCreated() {
        if (this.history.size() < 1)
            return null;
        else
            return ((BatchHistoryModel)this.history.get(0)).getStatusDate();
    }
    
    public EnumBatchStatus getStatus() {
        if (this.history.size() < 1)
            return null;
        else
            return ((BatchHistoryModel)this.history.get(this.history.size()-1)).getStatus();
    }
    
}

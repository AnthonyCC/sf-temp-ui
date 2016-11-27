/*
 * EnumBatchStatus.java
 *
 * Created on September 4, 2001, 2:07 PM
 */

package com.freshdirect.erp;

/**
 *
 * @author  mrose
 * @version 
 */
public class EnumBatchStatus implements java.io.Serializable {
    
    public final static EnumBatchStatus EMS_LOADING        =   new EnumBatchStatus("LD", "Batch load in progress");
    public final static EnumBatchStatus EMS_NEW            =   new EnumBatchStatus("NB", "New batch in EMS");
    public final static EnumBatchStatus EMS_REJ            =   new EnumBatchStatus("ER", "Error in batch, automatically rejected");
    public final static EnumBatchStatus EMS_APP_DATA       =   new EnumBatchStatus("AD", "Data from SAP approved in EMS");
    public final static EnumBatchStatus EMS_APP_CONTENT    =   new EnumBatchStatus("AC", "Content approved in EMS");
    
    public final static EnumBatchStatus STG_NEW            =   new EnumBatchStatus("PS", "Promoted from EMS to Stage");
    public final static EnumBatchStatus STG_REJ_DATA       =   new EnumBatchStatus("QD", "Rejected by QA for SAP Data in Stage");
    public final static EnumBatchStatus STG_REJ_CONTENT    =   new EnumBatchStatus("QC", "Rejected by QA for Content in Stage");
    public final static EnumBatchStatus STG_APP            =   new EnumBatchStatus("QA", "Approved by QA in Stage");
    
    public final static EnumBatchStatus PROD               =   new EnumBatchStatus("PP", "Promoted to Production");
    public final static EnumBatchStatus PROD_REJ           =   new EnumBatchStatus("PR", "Rejected in Production");
    
    public final static EnumBatchStatus getStatus(String stat) {
        if (EMS_LOADING.getCode().equalsIgnoreCase(stat))
            return EMS_LOADING;
        else if (EMS_NEW.getCode().equalsIgnoreCase(stat))
            return EMS_NEW;
        else if (EMS_REJ.getCode().equalsIgnoreCase(stat))
            return EMS_REJ;
        else if (EMS_APP_DATA.getCode().equalsIgnoreCase(stat))
            return EMS_APP_DATA;
        else if (EMS_APP_CONTENT.getCode().equalsIgnoreCase(stat))
            return EMS_APP_CONTENT;
        else if (STG_NEW.getCode().equalsIgnoreCase(stat))
            return STG_NEW;
        else if (STG_REJ_DATA.getCode().equalsIgnoreCase(stat))
            return STG_REJ_DATA;
        else if (STG_REJ_CONTENT.getCode().equalsIgnoreCase(stat))
            return STG_REJ_CONTENT;
        else if (STG_APP.getCode().equalsIgnoreCase(stat))
            return STG_APP;
        else if (PROD.getCode().equalsIgnoreCase(stat))
            return PROD;
        else if (PROD_REJ.getCode().equalsIgnoreCase(stat))
            return PROD_REJ;
        else
            throw new IllegalArgumentException("No such batch status \"" + stat + "\"");
    }

    private String code;
    private String description;
    
    /** Creates new EnumBatchStatus */
    private EnumBatchStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public String getDescription() {
        return this.description;
    }

}

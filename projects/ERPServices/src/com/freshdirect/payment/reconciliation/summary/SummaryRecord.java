/*
 * ReconciliationRecord.java
 *
 * Created on January 19, 2002, 2:27 PM
 */

package com.freshdirect.payment.reconciliation.summary;

/**
 *
 * @author  mrose
 * @version 
 */
public abstract class SummaryRecord implements java.io.Serializable {

    /** Holds value of property recordType. */
    protected EnumSummaryRecordType recordType;
    
    /** Creates new ReconciliationRecord */
    public SummaryRecord(EnumSummaryRecordType recordType) {
        super();
        this.recordType = recordType;
    }

    /** Getter for property recordType.
     * @return Value of property recordType.
     */
    public EnumSummaryRecordType getRecordType() {
        return recordType;
    }
    
    protected static java.text.DecimalFormat currencyFormatter = new java.text.DecimalFormat("$###,###0.00;($###,###0.00)");
    
}

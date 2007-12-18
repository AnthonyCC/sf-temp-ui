/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.util.*;

import com.freshdirect.dataloader.*;

import com.freshdirect.payment.reconciliation.detail.*;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;

/**
 *
 * @author  mrose
 * @version
 */
public class CCDetailTwoParser extends SettlementParser {
	
	public SynchronousParserClient client = null;
    
    public final static String TRAN_DATE                = "TRAN_DATE";
    public final static String ADJ_REASON               = "ADJ_REASON";
    public final static String TERM_ID                  = "TERM_ID";
    public final static String SETT_FILE_CREATION_DATE  = "SETT_FILE_CREATION_DATE";
    
    private CCDetailTwo record = null;
    
    /** Creates new ReconciliationParser */
    public CCDetailTwoParser() {
        super();
        
        fields.add(new Field(TRAN_DATE,                 6, true));
        fields.add(new Field(ADJ_REASON,               38, false));
        fields.add(new Field(TERM_ID,                   4, false));
        fields.add(new Field(SETT_FILE_CREATION_DATE,   4, true));
        
    }
    
    /**
     * a template method that must be defined by implementors
     * subclasses will know how to assemble model objects
     * from a a hash of tokens
     * @param tokens a HashMap containing parsed tokens from a single line
     * of a text file, keyed by their field names
     * @throws BadDataException an problems while trying to assemble objects from the
     * supplied tokens
     */
    protected void makeObjects(HashMap tokens) throws BadDataException {
        record = new CCDetailTwo();
        record.setTransactionDate(getDate(tokens, TRAN_DATE, "MMddyy"));
        record.setAdjustmentReason(getString(tokens, ADJ_REASON));
        record.setTerminalId(getString(tokens, TERM_ID));
        record.setSettlementFileCreationDate(getDate(tokens, SETT_FILE_CREATION_DATE, "MMdd"));
        
        this.client.accept(record);
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
    public String debug() {
        return this.record.toString();
    }
    
    
}
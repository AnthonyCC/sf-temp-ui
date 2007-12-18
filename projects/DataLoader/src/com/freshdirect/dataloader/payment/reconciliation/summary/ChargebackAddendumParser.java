/*
 * SummaryHeaderParser.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.util.*;

import com.freshdirect.dataloader.*;

import com.freshdirect.payment.reconciliation.summary.*;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;

/**
 *
 * @author  mrose
 * @version
 */
public class ChargebackAddendumParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String REFERENCE_NUMBER         = "REFERENCE_NUMBER";
    public final static String CHARGEBACK_DESCR         = "CHARGEBACK_DESCR";
    public final static String ORIG_TRANS_DATE          = "ORIG_TRANS_DATE";
    public final static String CHARGEBACK_WORK_DATE     = "CHARGEBACK_WORK_DATE";
    public final static String CHARGEBACK_RESPOND_DATE  = "CHARGEBACK_RESPOND_DATE";
    public final static String ORIG_TRANS_YEAR          = "ORIG_TRANS_YEAR";
    public final static String ORIG_TRANS_AMOUNT        = "ORIG_TRANS_AMOUNT";
    
    private ChargebackAddendum record = null;
    
    /** Creates new ReconciliationParser */
    public ChargebackAddendumParser() {
        super();
        
        fields.add(new Field(REFERENCE_NUMBER,         14, true));
        fields.add(new Field(CHARGEBACK_DESCR,         30, true));
        fields.add(new Field(ORIG_TRANS_DATE,           4, true));
        fields.add(new Field(CHARGEBACK_WORK_DATE,      6, true));
        fields.add(new Field(CHARGEBACK_RESPOND_DATE,   6, true));
        fields.add(new Field(ORIG_TRANS_YEAR,           2, true));
        fields.add(new Field(ORIG_TRANS_AMOUNT,         7, true));
        
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
        record = new ChargebackAddendum();
        record.setMerchantReferenceNumber(getString(tokens, REFERENCE_NUMBER));
        record.setChargebackDescription(getString(tokens, CHARGEBACK_DESCR));
        record.setOriginalTransactionDate(getDate(getString(tokens, ORIG_TRANS_DATE) + getString(tokens, ORIG_TRANS_YEAR), "MMddyy"));
        record.setChargebackWorkDate(getDate(tokens, CHARGEBACK_WORK_DATE, "MMddyy"));
        record.setChargebackRespondDate(getDate(tokens, CHARGEBACK_RESPOND_DATE, "MMddyy"));
        record.setOriginalTransactionAmount(getSignedDouble(tokens, ORIG_TRANS_AMOUNT, 2));
        
        client.accept(record);
    }
    
    public String debug() {
        return this.record.toString();
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
}
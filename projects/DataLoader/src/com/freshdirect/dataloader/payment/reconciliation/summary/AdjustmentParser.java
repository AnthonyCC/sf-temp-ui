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
public class AdjustmentParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String MERCHANT_NUMBER      = "MERCHANT_NUMBER";
    public final static String BATCH_DATE           = "BATCH_DATE";
    public final static String BATCH_NUMBER         = "BATCH_NUMBER";
    public final static String DEPOSIT_DATE         = "DEPOSIT_DATE";
    public final static String PROCESS_DATE         = "PROCESS_DATE";
    public final static String ADJ_AMOUNT           = "ADJ_AMOUNT";
    public final static String FILLER               = "FILLER";
    public final static String CARD_NUMBER          = "CARD_NUMBER";
    public final static String TRANS_DATE           = "TRANS_DATE";
    public final static String ADJ_DESCR            = "ADJ_DESCR";
    
    private Adjustment record = null;
    
    /** Creates new ReconciliationParser */
    public AdjustmentParser() {
        super();
        
        fields.add(new Field(MERCHANT_NUMBER,      11, true));
        fields.add(new Field(BATCH_DATE,            4, true));
        fields.add(new Field(BATCH_NUMBER,          5, true));
        fields.add(new Field(DEPOSIT_DATE,          4, true));
        fields.add(new Field(PROCESS_DATE,          4, true));
        fields.add(new Field(ADJ_AMOUNT,            7, true));
        fields.add(new Field(FILLER,                1, true));
        fields.add(new Field(CARD_NUMBER,          16, true));
        fields.add(new Field(TRANS_DATE,            4, true));
        fields.add(new Field(ADJ_DESCR,            -1, true));
        
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
        record = new Adjustment();
        record.setMerchantNumber(getString(tokens, MERCHANT_NUMBER));
        record.setBatchDate(getDate(tokens, BATCH_DATE, "MMdd"));
        record.setBatchNumber(getString(tokens, BATCH_NUMBER));
        record.setDepositDate(getDate(tokens, DEPOSIT_DATE, "MMdd"));
        record.setProcessDate(getDate(tokens, PROCESS_DATE, "MMdd"));
        record.setAdjustmentAmount(getSignedDouble(tokens, ADJ_AMOUNT, 2));
        record.setCardholderNumber(getString(tokens, CARD_NUMBER));
        record.setTransactionDate(getDate(tokens, TRANS_DATE, "MMdd"));
        record.setAdjustmentDescription(getString(tokens, ADJ_DESCR));
        
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
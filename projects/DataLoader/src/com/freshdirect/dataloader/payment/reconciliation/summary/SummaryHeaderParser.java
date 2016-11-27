/*
 * SummaryHeaderParser.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;
import com.freshdirect.payment.reconciliation.summary.SummaryHeader;

/**
 *
 * @author  mrose
 * @version
 */
public class SummaryHeaderParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String MERCHANT_NUMBER                  = "MERCHANT_NUMBER";
    public final static String BATCH_DATE                       = "BATCH_DATE";
    public final static String BATCH_NUMBER                     = "BATCH_NUMBER";
    public final static String PROCESS_DATE                     = "PROCESS_DATE";
    public final static String DEPOSIT_DATE                     = "DEPOSIT_DATE";
    public final static String NET_SALES_AMOUNT                 = "NET_SALES_AMOUNT";
    public final static String NUM_ADJUSTMENTS                  = "NUM_ADJUSTMENTS";
    public final static String ADJUSTMENT_AMOUNT                = "ADJUSTMENT_AMOUNT";
    public final static String SETTLEMENT_FILE_CREATION_DATE    = "SETTLEMENT_FILE_CREATION_DATE";
    
    private SummaryHeader summary = null;
    
    /** Creates new ReconciliationParser */
    public SummaryHeaderParser() {
        super();
        
        fields.add(new Field(MERCHANT_NUMBER,              11, true));
        fields.add(new Field(BATCH_DATE,                    4, true));
        fields.add(new Field(BATCH_NUMBER,                  5, true));
        fields.add(new Field(PROCESS_DATE,                  4, true));
        fields.add(new Field(DEPOSIT_DATE,                  4, true));
        fields.add(new Field(NET_SALES_AMOUNT,              7, true));
        fields.add(new Field(NUM_ADJUSTMENTS,               3, true));
        fields.add(new Field(ADJUSTMENT_AMOUNT,             7, true));
        fields.add(new Field(SETTLEMENT_FILE_CREATION_DATE, 4, true));
        
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
    @Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
        summary = new SummaryHeader();
        summary.setMerchantNumber(getString(tokens, MERCHANT_NUMBER));
        summary.setBatchDate(getDate(tokens, BATCH_DATE, "MMdd"));
        summary.setBatchNumber(getString(tokens, BATCH_NUMBER));
        summary.setProcessDate(getDate(tokens, PROCESS_DATE, "MMdd"));
        summary.setDepositDate(getDate(tokens, DEPOSIT_DATE, "MMdd"));
        summary.setNetSalesAmount(getSignedDouble(tokens, NET_SALES_AMOUNT, 2));
        summary.setNumberOfAdjustments(getInt(tokens, NUM_ADJUSTMENTS));
        summary.setAdjustmentAmount(getSignedDouble(tokens, ADJUSTMENT_AMOUNT, 2));
        summary.setSettlementFileCreationDate(getDate(tokens, SETTLEMENT_FILE_CREATION_DATE, "MMdd"));
        
        client.accept(summary);
    }
    
    @Override
    public String debug() {
        return this.summary.toString();
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
}
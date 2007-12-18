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
public class ChargebackParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String MERCHANT_NUMBER              = "MERCHANT_NUMBER";
    public final static String BATCH_DATE                   = "BATCH_DATE";
    public final static String BATCH_NUMBER                 = "BATCH_NUMBER";
    public final static String CHARGEBACK_AMOUNT            = "CHARGEBACK_AMOUNT";
    public final static String CHARGEBACK_DISCOUNT          = "CHARGEBACK_DISCOUNT";
    public final static String CHARGEBACK_REASON_CODE       = "CHARGEBACK_REASON_CODE";
    public final static String CHARGEBACK_CONTROL_NUMBER    = "CHARGEBACK_CONTROL_NUMBER";
    public final static String CARDHOLDER_NUMBER            = "CARDHOLDER_NUMBER";
    public final static String CHARGEBACK_REFERENCE_NUMBER  = "CHARGEBACK_REFERENCE_NUMBER";
    
    private Chargeback record = null;
    
    /** Creates new ReconciliationParser */
    public ChargebackParser() {
        super();
        
        fields.add(new Field(MERCHANT_NUMBER,              11, true));
        fields.add(new Field(BATCH_DATE,                    4, true));
        fields.add(new Field(BATCH_NUMBER,                  5, true));
        fields.add(new Field(CHARGEBACK_AMOUNT,             7, true));
        fields.add(new Field(CHARGEBACK_DISCOUNT,           6, true));
        fields.add(new Field(CHARGEBACK_REASON_CODE,        2, true));
        fields.add(new Field(CHARGEBACK_CONTROL_NUMBER,     9, true));
        fields.add(new Field(CARDHOLDER_NUMBER,            16, true));
        fields.add(new Field(CHARGEBACK_REFERENCE_NUMBER,  15, true));
        
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
        record = new Chargeback();
        String saleId = getString(tokens, MERCHANT_NUMBER).trim();
        if(saleId.indexOf('X') > 0) {
			saleId = saleId.substring(0, saleId.indexOf('X'));
		}
        record.setMerchantNumber(saleId);
        record.setBatchDate(getDate(tokens, BATCH_DATE, "MMdd"));
        record.setBatchNumber(getString(tokens, BATCH_NUMBER));
        record.setChargebackAmount(getSignedDouble(tokens, CHARGEBACK_AMOUNT, 2));
        record.setChargebackDiscount(getSignedDouble(tokens, CHARGEBACK_DISCOUNT, 2));
        record.setChargebackReasonCode(getString(tokens, CHARGEBACK_REASON_CODE));
        record.setChargebackControlNumber(getString(tokens, CHARGEBACK_CONTROL_NUMBER));
        record.setCardholderNumber(getString(tokens, CARDHOLDER_NUMBER));
        record.setChargebackReferenceNumber(getString(tokens, CHARGEBACK_REFERENCE_NUMBER));
        
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
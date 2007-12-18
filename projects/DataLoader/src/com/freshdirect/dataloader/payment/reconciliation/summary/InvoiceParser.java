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
public class InvoiceParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String MERCHANT_NUMBER              = "MERCHANT_NUMBER";
    public final static String INVOICE_DATE                 = "INVOICE_DATE";
    public final static String INVOICE_AMOUNT               = "INVOICE_AMOUNT";
    public final static String INVOICE_NUMBER               = "INVOICE_NUMBER";
    public final static String INVOICE_DESCR                = "INVOICE_DESCR";
    
    private Invoice record = null;
    
    /** Creates new ReconciliationParser */
    public InvoiceParser() {
        super();
        
        fields.add(new Field(MERCHANT_NUMBER,              11, true));
        fields.add(new Field(INVOICE_DATE,                  4, true));
        fields.add(new Field(INVOICE_AMOUNT,               11, true));
        fields.add(new Field(INVOICE_NUMBER,                9, true));
        fields.add(new Field(INVOICE_DESCR,                -1, true));
        
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
        record = new Invoice();
        record.setMerchantNumber(getString(tokens, MERCHANT_NUMBER));
        record.setInvoiceDate(getDate(tokens, INVOICE_DATE, "MMdd"));
        record.setInvoiceAmount(getSignedDouble(tokens, INVOICE_AMOUNT, 2));
        record.setInvoiceNumber(getString(tokens, INVOICE_NUMBER));
        record.setInvoiceDescription(getString(tokens, INVOICE_DESCR));
        
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
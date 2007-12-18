/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.util.*;

import com.freshdirect.dataloader.*;
import com.freshdirect.dataloader.payment.reconciliation.*;

import com.freshdirect.payment.reconciliation.detail.*;

/**
 *
 * @author  mrose
 * @version
 */
public class CCDetailOneParser extends SettlementParser  {
    
    public SynchronousParserClient client = null;
    
    public final static String TRAN_CODE            = "TRAN_CODE";
    public final static String PROD_CODE            = "PROD_CODE";
    public final static String ACCT_NUMBER          = "ACCT_NUMBER";
    public final static String FDMS_REF_NUMBER      = "FDMS_REF_NUMBER";
    public final static String MERCH_REF_NUMBER     = "MERCH_REF_NUMBER";
    public final static String TRAN_AMOUNT          = "TRAN_AMOUNT";
    public final static String MERCH_NUMBER         = "MERCH_NUMBER";
    public final static String MEDIA_IND            = "MEDIA_IND";
    public final static String AUTH_CODE            = "AUTH_CODE";
    
    private CCDetailOne record = null;
    
    /** Creates new ReconciliationParser */
    public CCDetailOneParser() {
        super();
        
        fields.add(new Field(TRAN_CODE,         2, true));
        fields.add(new Field(PROD_CODE,         1, false));
        fields.add(new Field(ACCT_NUMBER,      19, true));
        fields.add(new Field(FDMS_REF_NUMBER,  14, true));
        fields.add(new Field(MERCH_REF_NUMBER, 15, false));
        fields.add(new Field(TRAN_AMOUNT,       7, true));
        fields.add(new Field(MERCH_NUMBER,     11, true));
        fields.add(new Field(MEDIA_IND,         1, true));
        fields.add(new Field(AUTH_CODE,        -1, false));
        
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
        record = new CCDetailOne();
        record.setTransactionCode(getString(tokens, TRAN_CODE));
        record.setProductCode(getString(tokens, PROD_CODE));
        record.setAccountNumber(getString(tokens, ACCT_NUMBER).trim());
        record.setFDMSReferenceNumber(getString(tokens, FDMS_REF_NUMBER));
        String saleId = getString(tokens, MERCH_REF_NUMBER).trim();
        if(saleId.indexOf('X') > 0) {
			saleId = saleId.substring(0, saleId.indexOf('X'));
		}
        record.setMerchantReferenceNumber(saleId);
        record.setTransactionAmount(getDouble(tokens, TRAN_AMOUNT, 2));
        record.setMerchantNumber(getString(tokens, MERCH_NUMBER));
        record.setMediaIndicator(getString(tokens, MEDIA_IND));
        record.setAuthCode(getString(tokens, AUTH_CODE).trim());
        
        client.accept(record);
    }
    
    public String debug() {
        return this.record.toString();
    }
    
    public DetailRecord getRecord(){
    	return this.record;
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
}
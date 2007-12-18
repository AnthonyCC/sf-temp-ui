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
public class AmexJCBSummaryParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String NUMBER_AMEX_ITEMS            = "NUMBER_AMEX_ITEMS";
    public final static String AMEX_NET_SALES_AMOUNT        = "AMEX_NET_SALES_AMOUNT";
    public final static String AMEX_FDMS_DISCOUNT_AMOUNT    = "AMEX_FDMS_DISCOUNT_AMOUNT";
    public final static String AMEX_ISSUER_DISCOUNT_AMOUNT  = "AMEX_ISSUER_DISCOUNT_AMOUNT";
    public final static String NUMBER_JCB_ITEMS             = "NUMBER_JCB_ITEMS";
    public final static String JCB_NET_SALES_AMOUNT         = "JCB_NET_SALES_AMOUNT";
    public final static String JCB_FDMS_DISCOUNT_AMOUNT     = "JCB_FDMS_DISCOUNT_AMOUNT";
    public final static String JCB_ISSUER_DISCOUNT_AMOUNT   = "JCB_ISSUER_DISCOUNT_AMOUNT";
    
    private AmexJCBSummary summary = null;
    
    /** Creates new ReconciliationParser */
    public AmexJCBSummaryParser() {
        super();
        
        fields.add(new Field(NUMBER_AMEX_ITEMS,             3, true));
        fields.add(new Field(AMEX_NET_SALES_AMOUNT,         7, true));
        fields.add(new Field(AMEX_FDMS_DISCOUNT_AMOUNT,     7, true));
        fields.add(new Field(AMEX_ISSUER_DISCOUNT_AMOUNT,   7, true));
        fields.add(new Field(NUMBER_JCB_ITEMS,              3, true));
        fields.add(new Field(JCB_NET_SALES_AMOUNT,          7, true));
        fields.add(new Field(JCB_FDMS_DISCOUNT_AMOUNT,      7, true));
        fields.add(new Field(JCB_ISSUER_DISCOUNT_AMOUNT,    7, true));
        
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
        summary = new AmexJCBSummary();
        summary.setNumberOfAmexItems(getInt(tokens, NUMBER_AMEX_ITEMS));
        summary.setAmexNetSalesAmount(getSignedDouble(tokens, AMEX_NET_SALES_AMOUNT, 2));
        summary.setAmexFDMSDiscountAmount(getSignedDouble(tokens, AMEX_FDMS_DISCOUNT_AMOUNT, 2));
        summary.setAmexIssuerDiscountAmount(getSignedDouble(tokens, AMEX_ISSUER_DISCOUNT_AMOUNT, 2));
        summary.setNumberOfJCBItems(getInt(tokens, NUMBER_JCB_ITEMS));
        summary.setJCBNetSalesAmount(getSignedDouble(tokens, JCB_NET_SALES_AMOUNT, 2));
        summary.setJCBFDMSDiscountAmount(getSignedDouble(tokens, JCB_FDMS_DISCOUNT_AMOUNT, 2));
        summary.setJCBIssuerDiscountAmount(getSignedDouble(tokens, JCB_ISSUER_DISCOUNT_AMOUNT, 2));
        
        client.accept(summary);
    }
    
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
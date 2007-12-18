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
public class VisaMCSummaryParser extends SettlementParser{
	
	private SynchronousParserClient client = null;
    
    public final static String NUMBER_VISA_ITEMS            = "NUMBER_VISA_ITEMS";
    public final static String VISA_NET_SALES_AMOUNT        = "VISA_NET_SALES_AMOUNT";
    public final static String VISA_DISCOUNT_AMOUNT         = "VISA_DISCOUNT_AMOUNT";
    public final static String NUMBER_MC_ITEMS              = "NUMBER_MC_ITEMS";
    public final static String MC_NET_SALES_AMOUNT          = "MC_NET_SALES_AMOUNT";
    public final static String MC_DISCOUNT_AMOUNT           = "MC_DISCOUNT_AMOUNT";
    
    private VisaMCSummary summary = null;
    
    /** Creates new ReconciliationParser */
    public VisaMCSummaryParser() {
        super();
        
        fields.add(new Field(NUMBER_VISA_ITEMS,             3, true));
        fields.add(new Field(VISA_NET_SALES_AMOUNT,         7, true));
        fields.add(new Field(VISA_DISCOUNT_AMOUNT,          7, true));
        fields.add(new Field(NUMBER_MC_ITEMS,               3, true));
        fields.add(new Field(MC_NET_SALES_AMOUNT,           7, true));
        fields.add(new Field(MC_DISCOUNT_AMOUNT,            7, true));
        
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
        summary = new VisaMCSummary();
        summary.setNumberOfVisaItems(getInt(tokens, NUMBER_VISA_ITEMS));
        summary.setVisaNetSalesAmount(getSignedDouble(tokens, VISA_NET_SALES_AMOUNT, 2));
        summary.setVisaDiscountAmount(getSignedDouble(tokens, VISA_DISCOUNT_AMOUNT, 2));
        summary.setNumberOfMCItems(getInt(tokens, NUMBER_MC_ITEMS));
        summary.setMCNetSalesAmount(getSignedDouble(tokens, MC_NET_SALES_AMOUNT, 2));
        summary.setMCDiscountAmount(getSignedDouble(tokens, MC_DISCOUNT_AMOUNT, 2));
        
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
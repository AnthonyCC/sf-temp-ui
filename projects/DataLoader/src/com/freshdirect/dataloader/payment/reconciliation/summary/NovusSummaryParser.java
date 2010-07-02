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
import com.freshdirect.payment.reconciliation.summary.NovusSummary;

/**
 *
 * @author  mrose
 * @version
 */
public class NovusSummaryParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String NUMBER_NOVUS_ITEMS            = "NUMBER_NOVUS_ITEMS";
    public final static String NOVUS_NET_SALES_AMOUNT        = "NOVUS_NET_SALES_AMOUNT";
    public final static String NOVUS_FDMS_DISCOUNT_AMOUNT    = "NOVUS_FDMS_DISCOUNT_AMOUNT";
    public final static String NOVUS_ISSUER_DISCOUNT_AMOUNT  = "NOVUS_ISSUER_DISCOUNT_AMOUNT";
    
    private NovusSummary summary = null;
    
    /** Creates new ReconciliationParser */
    public NovusSummaryParser() {
        super();
        
        fields.add(new Field(NUMBER_NOVUS_ITEMS,             3, true));
        fields.add(new Field(NOVUS_NET_SALES_AMOUNT,         7, true));
        fields.add(new Field(NOVUS_FDMS_DISCOUNT_AMOUNT,     7, true));
        fields.add(new Field(NOVUS_ISSUER_DISCOUNT_AMOUNT,   7, true));
        
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
        summary = new NovusSummary();
        summary.setNumberOfNovusItems(getInt(tokens, NUMBER_NOVUS_ITEMS));
        summary.setNovusNetSalesAmount(getSignedDouble(tokens, NOVUS_NET_SALES_AMOUNT, 2));
        summary.setNovusFDMSDiscountAmount(getSignedDouble(tokens, NOVUS_FDMS_DISCOUNT_AMOUNT, 2));
        summary.setNovusIssuerDiscountAmount(getSignedDouble(tokens, NOVUS_ISSUER_DISCOUNT_AMOUNT, 2));
        
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